package server;

import cards.Card;
import cards.Deck;
import enums.TAction;
import game.Player;
import game.TexasTable;

import java.io.*;
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
    public static final int MAX_PLAYERS = 3;

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

        // attempt to open our server socket
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
                    System.out.println("TPokerServer: All players connected; proceeding with game.");
                    isPlayStage = true;
                    break;
                }

                client = this.serverSocket.accept();

                System.out.println(pool.getActiveCount());
                System.out.println("TPokerServer: Connection accepted, id = " + clientID++);

            } catch (IOException e) {
                if(!isJoinStage) {
                    System.out.println("TPokerServer: Server has stopped");
                    break;
                }
                throw new RuntimeException("Client connection error", e);
            }

            Card drawOne = deck.pullCard();
            Card drawTwo = deck.pullCard();

            System.out.println("TPokerServer: Passing cards: " + drawOne + " and " + drawTwo);

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
    private int BET = 2;

    private void playStage() {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("TPokerServer: Entered PlayStage");

        boolean isPreStage = true, isFlop = false, isTurn = false, isRiver = false;

        while(isPlayStage) {

            while(isPreStage) {
                // this will block the thread until all players have made their move.
                getTableActions();

                // we'll want to keep this stage going, until no raises have been made
                if(playerActions.containsValue(TAction.RAISE)) {
                    continue;
                }

                isPreStage = false;
                isFlop = true;
            }

            dealFlop();

            System.out.println("TPokerServer: Flop has been dealt");
            while(isFlop) {
                getTableActions();

                if(playerActions.containsValue(TAction.RAISE)) {
                    continue;
                }

                isFlop = false;
                isTurn = true;
            }

            dealRound("TURN");

            System.out.println("TPokerServer: Turn has been dealt");
            while(isTurn) {
                getTableActions();

                if(playerActions.containsValue(TAction.RAISE)) {
                    continue;
                }

                isTurn = false;
                isRiver = true;
            }

            dealRound("RIVER");

            System.out.println("TPokerServer: River has been dealt");
            while(isRiver) {
                getTableActions();

                if(playerActions.containsValue(TAction.RAISE)) {
                    continue;
                }

                isRiver = false;
            }

            System.out.println("TPokerServer: Players should be told their winnings now.");


            System.out.println("Exiting pre `stage");
        }
    }

    public void dealRound(String round) {
        for(Player p : players) {
            if(p.folded)
                continue;

            if(round.equals("TURN"))
                p.thread.FLOP_DONE = true;

            if(round.equals("RIVER"))
                p.thread.TURN_DONE = true;

            dealCard(p);
        }
    }

    public void dealFlop() {
        System.out.println("TPokerServer: Dealing flop");

        for(Player p : players) {
            if(p.folded)
                continue;

            p.thread.PRE_FLOP_DONE = true;

            dealCard(p);
            dealCard(p);
            dealCard(p);
        }
    }


    public void dealCard(Player p) {

        Card card = deck.pullCard();

        System.out.println("TPokerServer: Dealing Flop Card: " + card);

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

            out = p.objOut;
            in  = p.objIn;

            System.out.println("Using out/in streams with IDs\n\t" + out + "\n\t" + in);

            try {
                System.out.println("TPokerServer: Sending PING request");

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
                    System.out.println("TPokerServer: waiting for input (action) ...");

                    message = in.readUTF();

                    System.out.println("TPokerServer: received input " + message);

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

                        continue;
                    }

                    System.out.println("TPokerServer: invalid input: " + message);
                }

                System.out.println("TPokerServer: received valid input: " + message);

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
