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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TPokerServer implements Runnable {

    protected ServerSocket serverSocket = null;

    protected boolean isJoinStage = false;
    protected boolean isPlayStage = false;

    protected Thread       ourThread    = null;
    protected ThreadPoolExecutor pool      = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);

    ArrayList<Player> players = new ArrayList<>();

    protected static int  clientID      = 0;


    public static final int PORT = 1337;
    public static final int MAX_PLAYERS = 1;

    public static final String MSG_NEXT = "NEXT";
    public static final String MSG_STAY = "STAY";

    Deck deck = Deck.getInstance();

    TexasTable table = new TexasTable();

    HashMap<Player, TAction> playerActions = new HashMap<>();


    // add a texas Game object here; we can sync potentially.

    @Override
    public void run() {

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

        while(isJoinStage) {
            Socket client = null;

            try {
                // if we've already got 8 players, we don't want more
                if(pool.getActiveCount() >= MAX_PLAYERS) {
                    System.out.println("TPokerServer: All players connected; proceeding with com.scully.game.");
                    isPlayStage = true;
                    break;
                }

                client = this.serverSocket.accept();

                System.out.println("TPokerServer: Connection accepted, id = " + clientID++);

            } catch (IOException e) {
                if(!isJoinStage) {
                    System.out.println("TPokerServer: Server has stopped");
                    break;
                }
                throw new RuntimeException("Client connection error", e);
            }

            Player player = new Player(client);

            this.pool.execute(
                    player.thread
            );

            players.add(player);
        }

        playStage();

        System.out.println("TPokerServer: We have died!");

        pool.shutdown();
    }

    private int POT = 500;
    private int MIN_BET = 5;
    private int CUR_BET = 5;

    Round round = Round.PREFLOP;

    boolean isPreStage = true;

    private void playStage() {

        // pause thread for 2 seconds; allows for clients to sync
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("TPokerServer: Entered PlayStage");

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

        // while we're not in the result stage, perform each round
        while(round != Round.RESULT) {
            processRound();

            System.err.println("TPokerServer: Server Current round = " + round);
        }

        System.out.println("TPokerServer: Players should be told their winnings now.");
        System.out.println("Exiting pre stage");
    }

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



    public void dealRound() {
        if(round == Round.FLOP) {
            table.pullFlop();
            dealFlop();
        }

        if(round == Round.RIVER || round == Round.TURN) {
            for(Player p : players) {
                if(p.folded)
                    continue;

                p.thread.FLOP_DONE = round == Round.TURN;
                p.thread.TURN_DONE = round == Round.RIVER;

                System.out.println("TPokerServer: Dealing round: " + round);

                switch (round) {
                    case TURN:
                        table.pullTurn();
                        dealCard(p, table.getTurn());
                        break;
                    case RIVER:
                        table.pullRiver();
                        dealCard(p, table.getRiver());
                        break;
                }
            }
        }
    }

    public void sendGlobalMessage(String message) {
        try {
            for(Player p : players) {
                p.objOut.writeUTF(message);

                if(message.equals(MSG_NEXT))
                    p.objOut.writeObject(p.getPlayerInfo());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendGlobalObject(Object object) {
        try {
            for(Player p : players) {
                p.objOut.writeObject(object);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dealFlop() {
        for(Player p : players) {
            System.out.print("TPokerServer: Dealing flop ");

            // if they've folded, we don't need to deal them com.scully.cards
            if(p.folded)
                continue;

            p.thread.PRE_FLOP_DONE = true;

            for(Card c : table.getFlop()) {
                System.out.println("TPokerServer: Dealing flop card: " + c);
                dealCard(p, c);
            }

            System.out.println();
        }
    }


    public void dealCard(Player p, Card card) {

        System.out.printf("(%s) ", card);

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
        for(Player p : players) {

            // do nothing if they have folded
            if(p.folded)
                continue;

            ObjectOutputStream out;
            ObjectInputStream   in;

            // alias the in/out streams for the player
            out = p.objOut;
            in  = p.objIn;

            try {
                // the PING is just a name to tell the client we need their input.
                // this prevents race conditions where client B would be ignored if A was chosen b4
                out.writeUTF("PING");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String message = "";
            boolean validAction = false;

            try {
                while(!validAction) {
                    System.out.print("TPokerServer: waiting for input (action) ... ");

                    message = in.readUTF();

                    System.out.print(message + "... ");

                    TAction action = TAction.parseTAction(message);

                    // parseTAction returns null if not applicable; only want normals.
                    if(action != null) {

                        // put the action + player into a hashmap,
                        playerActions.put(p, action);
                        // this will break us out of the whileloop for the player
                        validAction = true;


                        // if they have folded, mark them so we don't include them later
                        if(action == TAction.FOLD) {
                            p.folded = true;
                        }

                        if(action == TAction.CALL) {
                            if(isPreStage) {
                                p.chips -= CUR_BET;
                            }
                        }

                        continue;
                    }

                    System.out.println("invalid");
                }

                System.out.println("valid");

            } catch (EOFException e) {
                System.err.println("TPokerServer: EOFException Caught");
                e.printStackTrace();
                System.exit(1);
            } catch (IOException e) {
                System.out.println("TPokerServer: PlayStage receive message error");
                e.printStackTrace();
            }
        }
    }

}
