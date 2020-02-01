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

    ArrayList<Player> socks = new ArrayList<>();

    protected static int  clientID      = 0;


    public static final int PORT = 1337;
    public static final int MAX_PLAYERS = 2;

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

            socks.add(player);


        }

        playStage();

        System.out.println("TPokerServer: We have died!");

        pool.shutdown();
    }

    private int POT = 500;
    private int BET = 2;

    private void playStage() {

        System.out.println("TPokerServer: Entered PlayStage");

        boolean isPreStage = true, isFlop = false, isTurn = false, isRiver = false;

        while(isPlayStage) {

            while(isPreStage) {

                System.out.println("TPokerServer: waiting for player actions");

                // this will block the thread until all players have made their move.
                getTableActions();

                if(playerActions.containsValue(TAction.RAISE)) {
                    continue;
                }
            }

        }
    }



    /**
     * This gathers all the actions from players currently sat on the table. We'll call this after every stage i.e. flop, river, turn.
     */
    public void getTableActions() {
        for(Player p : socks) {

            try {
                System.out.println("TPokerServer: Sending PING request");



                ObjectOutputStream out = new ObjectOutputStream(p.socket.getOutputStream());
                out.reset();
                out.writeUTF("PING");
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }



            // do nothing if they have folded
            if(p.folded)
                continue;

            String message = "";
            boolean validAction = false;

            try {
                DataInputStream in  = new DataInputStream(new BufferedInputStream(p.socket.getInputStream()));

                while(!validAction) {
                    message = in.readUTF();
                    System.out.println("TPokerServer: received input " + message);

                    TAction action = TAction.parseTAction(message);

                    if(action != null) {
                        System.out.println(action);
                        playerActions.put(p, action);
                    }

                }

                System.out.println("TPokerServer: received valid input: " + message);

            } catch (IOException e) {
                System.out.println("TPokerServer: PlayStage receive message error");
                e.printStackTrace();
            }
        }
    }

}
