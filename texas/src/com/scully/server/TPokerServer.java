package com.scully.server;

import com.scully.cards.Card;
import com.scully.cards.Deck;
import com.scully.enums.Round;
import com.scully.enums.TAction;
import com.scully.game.Player;
import com.scully.game.TexasTable;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Server program for Texas Hold'em
 */
public class TPokerServer implements Runnable {


    /**
     * Server's socket players connect to
     */
    protected ServerSocket serverSocket = null;

    protected boolean isJoinStage = false;
    protected boolean isPlayStage = false;

    protected Thread       ourThread    = null;
    protected ThreadPoolExecutor pool      = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);

    ArrayList<Player> players = new ArrayList<>();

    /** Counter for new incoming clients */
    protected static int  clientID      = 0;

    /** Port number for the server to host on */
    public static final int PORT = 1337;

    /** Maximum numbers of players */
    public static final int MAX_PLAYERS = 2;

    /** Reconnection timeout in milliseconds */
    public static final int RECONNECT_TIMEOUT = 15 * 1000;

    public static final String MSG_NEXT = "NEXT";
    public static final String MSG_STAY = "STAY";

    Deck deck = Deck.getInstance();

    TexasTable table = new TexasTable();

    HashMap<Player, TAction>       playerActions    = new HashMap<>();
    HashMap<Player, TIdentityFile> playerIdentities = new HashMap<>();

    public boolean SHUTDOWN = false;

    // add a texas Game object here; we can sync potentially.

    @Override
    public void run() {
        System.out.println("TPokerServer: Server started");

        synchronized (this) {
            this.ourThread = Thread.currentThread();
            deck = Deck.getInstance();
        }

        // attempt to open our com.scully.server socket
        try {
            serverSocket = new ServerSocket(PORT);

            isJoinStage = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(!SHUTDOWN) {
            waitForConnections();

            round = Round.PREFLOP;
            playStage();
        }
        // printPoolStats();

        System.out.println("TPokerServer: We have died!");

        pool.shutdown();
    }

    /**
     * Waits for connections at start of match
     */
    private void waitForConnections() {
        System.out.println("TPokerServer: Waiting for connections");
        while(isJoinStage) {
            Socket client;
            try {
                // printPoolStats();
                // if we've already got 8 players, we don't want more
                if(pool.getActiveCount() >= MAX_PLAYERS) {
                    System.out.println();
                    System.out.println("===========================================");
                    System.out.println("All players have connected, starting match.");
                    System.out.println("===========================================");
                    System.out.println();
                    isPlayStage = true;
                    isJoinStage = false;
                    continue;
                }

                client = this.serverSocket.accept();

                System.out.println("TPokerServer: Connection accepted, id = " + clientID++);

            } catch (IOException e) {
                if(!isJoinStage)
                    System.out.println("TPokerServer: Server has stopped");
                throw new RuntimeException("Client connection error", e);
            }

            Player player = new Player(client);
            boolean dupe = false;

            if(playerIdentities.containsValue(player.identityFile)) {
                System.out.println("TPokerServer: found players with same identifiers");

                for(Map.Entry<Player, TIdentityFile> entry : playerIdentities.entrySet()) {
                    Player        mP       = entry.getKey();
                    TIdentityFile idenFile = entry.getValue();

                    System.out.println("Comparing identities: " + idenFile.token + " and " + player.identityFile.token);

                    if(idenFile.equals(player.identityFile) && mP.disconnected) {
                        System.out.println("TPokerServer: Found exact player with identity file");
                        dupe = true;
                        break;
                    }
                }

                // if the player has previously connected, then we want to accept them
                if(dupe) {
                    System.out.println("TPokerServer: Previously connected player with ID " + player.identityFile.token + " found, attempting to rejoin");
                    player.sendMessage("ACCEPT");
                } else {
                    System.out.println("TPokerServer: Rejecting player");
                    player.sendMessage("REJECT");
                    continue;
                }
            }

            // if we're not taking an old connection, then we need to update the hashmap and accept
            if(!dupe) {
                System.out.println("TPokerServer: Not a duplicate, adding to hashmap");
                playerIdentities.put(player, player.identityFile);
                player.sendMessage("ACCEPT");
            }


            player.future = pool.submit(player.thread);
            players.add(player);
        }
    }

    private int POT = 500;
    private int MIN_BET = 5;
    private int CUR_BET = 5;

    /**
     * Current round
     */
    Round round = Round.PREFLOP;

    /**
     * If we are on the preflop stage
     */
    boolean isPreStage = true;

    /**
     * Main loop for playing the game
     */
    private void playStage() {
        //System.out.println("TPokerServer: Entered PlayStage");

        for(Player p : players) {
            // disallow if the player cannot make the minimum bet
            if(p.chips < MIN_BET) {
                p.folded = true;
                continue;
            }

            // else, subtract the bet + add to pot
            p.chips -= MIN_BET;
            POT += MIN_BET;
        }

        dealRound();

        // while we're not in the result stage, perform each round
        while(round != Round.RESULT) {
            processRound();
            // System.err.println("TPokerServer: Server Current round = " + round);
        }

        System.out.println("TPokerServer: Players should be told their winnings now. Must restart");

        isJoinStage = true;
        isPlayStage = false;
    }

    /**
     * Gets players actions, decides what to do with them
     */
    public void processRound() {
        boolean canMove = false;

        if(round == Round.FLOP)
            isPreStage = false;

        while(!canMove) {
            getTableActions();

            if (playerActions.containsValue(TAction.RAISE)) {
                int maxRaise = Integer.MIN_VALUE;

                // find the max raise that anyone has given, this will be used for the current bet
                for(TAction x : playerActions.values()) {
                    if(x.value > maxRaise) {
                        maxRaise = x.value;
                    }
                }
                CUR_BET = maxRaise;
                // if we've raised, every player will have to send their new actions
                sendGlobalMessage(MSG_STAY);
                continue;
            }

            canMove = true;

            // let each player know they can move their state
            sendGlobalMessage(MSG_NEXT);

            // move to next round if everyone has checked/folded (not raised)
            round = Round.nextRound(round);

            dealRound();
        }
    }

    /**
     * Deals the appropriate round cards
     */
    public void dealRound() {

        // System.out.println("TPokerServer: Dealing round: " + round);

        if(round == Round.PREFLOP) {
            for(Player p : players) {

                if(p.folded)
                    continue;

                Card c1 = deck.pullCard();
                Card c2 = deck.pullCard();

                p.cards[0] = c1;
                p.cards[1] = c2;

                dealCard(p, c1);
                dealCard(p, c2);
            }

            System.out.println("TPokerServer: Dealt PREFLOP cards");
        }

        if(round == Round.FLOP) {
            table.pullFlop();

            Card[] flop = table.getFlop();

            System.out.println("TPokerServer: Dealing FLOP cards: " + Arrays.toString(flop));

            for(Player p : players) {
                for(int i = 0; i < 3; i++) {
                    Card c = flop[i];
                    p.cards[3 + i] = c;
                    dealCard(p, c);
                }
            }

        }

        if(round == Round.RIVER) {
            table.pullRiver();
            Card c = table.getRiver();

            System.out.println("TPokerServer: Dealing RIVER card: " + c);

            for(Player p : players) {
                p.cards[5] = c;
                dealCard(p, c);
            }

        }

        if(round == Round.TURN) {
            table.pullTurn();
            Card c = table.getTurn();

            System.out.println("TPokerServer: Dealing TURN card: " + c);

            for(Player p : players) {
                p.cards[6] = c;
                dealCard(p, c);
            }
        }

        for(Player p : players) {
            p.round = round;
        }
    }

    public void sendGlobalMessage(String message) {
        try {
            // System.out.println("TPokerServer: Sending global message - " + message);
            for(Player p : players) {

                if(p.disconnected || p.error)
                    continue;

                p.objOut.writeUTF(message);

                if(message.equals(MSG_NEXT)) {
                    p.objOut.writeObject(p.getPlayerInfo());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deals card to all players
     * @param card Card to deal
     */
    public void dealGlobalCard(Card card) {
        for(Player p : players) {
            if(p.folded)
                continue;
            dealCard(p, card);
        }
    }

    /**
     * Deals a card
     * @param p Player to deal card to
     * @param card Card to deal
     */
    public void dealCard(Player p, Card card) {
        // System.out.printf("\t(%s) \n", card);

        if(p.folded)
            return;

        try {
            p.objOut.writeObject(card);
        } catch (IOException e) {
            System.err.println("TPokerServer: Error dealing card");
        }
    }

    /**
     * This gathers all the actions from players currently sat on the table. We'll call this after every stage i.e. flop, river, turn.
     */
    public void getTableActions() {
        // printPoolStats();
//        for(Player p : players) {
        for(int i = 0; i < players.size(); i++) {
            Player p = players.get(i);

            // do nothing if they have folded
            if(p.folded || p.disconnected)
                continue;

            ObjectOutputStream out;
            ObjectInputStream   in;

            // alias the in/out streams for the player
            out = p.objOut; in  = p.objIn;

            try {
                // the PING is just a name to tell the client we need their input.
                // this prevents race conditions where client B would be ignored if A was chosen b4
//                System.out.println("TPokerServer: Writing PING request - player should know to reply");
//                System.out.printf("TPokerServer: Writing PING request - player %d should know to reply\n", p.id);
                out.writeUTF("PING");
                out.flush();
            } catch (SocketException e) {
//                System.err.println("TPokerServer: Socket Broken; player has likely unexpectedly dropped");
                handleUnexpectedDisconnection(p);
                i -= 1;
                continue;
            } catch (IOException e) {
                e.printStackTrace();
            }

            String message = "";
            boolean validAction = false;
            while(!validAction) {
                //System.out.println("TPokerServer: Waiting for player to declare action");

                if(p.disconnected || p.folded)
                    break;

                TAction action = null;

                try {
                    message = in.readUTF();
                    action = TAction.parseTAction(message);
                } catch (EOFException e) {
                        System.err.println("TPokerServer: Unexpected EOF; likely the player has disconnected unexpectedly");
                        handleUnexpectedDisconnection(p);
                        i -= 1;
                        continue;
                } catch (IOException e) {
                        System.err.println("TPokerServer: PlayStage receive message error");
                        e.printStackTrace();
                }

                // parseTAction returns null if not applicable; only want normals.
                if (action == null) {
                    continue;
                }

                // System.out.printf("TPokerServer: Player %d has taken action: %s\n", p.id, action);

                // put the action + player into a hashmap,
                playerActions.put(p, action);
                // this will break us out of the whileloop for the player
                validAction = true;

                switch (action) {
                    case CALL:
                        p.chips -= CUR_BET;
                        break;

                    case FOLD:
                        p.folded = true;
                        break;

                    case QUIT:
                        handleUnexpectedDisconnection(p);
//                        p.close();
//                        players.remove(p);
                        break;
                }
            }
        }
    }

    /**
     * Restores player that has reconnected
     * @param p Player to regenerate network I/O
     */
    public void handleUnexpectedDisconnection(Player p) {
        try {
            System.out.printf("TPokerServer: Player %d has unexpectedly disconnected\n", p.id);
            // set our timeout to wait for player
            serverSocket.setSoTimeout(RECONNECT_TIMEOUT);
            System.out.printf("TPokerServer: Waiting %d seconds for the player to disconnect\n", RECONNECT_TIMEOUT / 1000);
            // see if they rejoin
            Socket socket = serverSocket.accept();

            System.out.println("TPokerServer: Player has rejoined");

            // we'll want to replace the in/out streams
            p.thread.KEEP_ALIVE = false;
            p.future.cancel(true);
            pool.remove(p.thread);

            p.revitalizePlayer(socket);

            p.future = pool.submit(p.thread);

        } catch (SocketTimeoutException e) {
            System.out.println("TPokerServer: Player did not reconnect in time, continuing with game");

            // we want to ignore this player from now on
            p.disconnected = true;
            p.folded = true;

            System.out.println("TPokerServer: Before disconnect");
//            printPoolStats();

            // remove their thread from the pool; this frees up space for reconnection
            p.thread.KEEP_ALIVE = false;
            p.future.cancel(true);
            pool.remove(p.thread);

//            printPoolStats();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }


    /**
     * Prints debug stats about the ThreadPool
     */
    public void printPoolStats() {
        int    active = pool.getActiveCount();
        long complete = pool.getCompletedTaskCount();
        long    count = pool.getTaskCount();
        int      size = pool.getPoolSize();

        System.out.printf(
            "TPokerServer Pool Stats: Active: %d Complete: %d Count: %d Size: %d\n", active, complete, count, size
        );
    }

}
