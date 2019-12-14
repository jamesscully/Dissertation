package server;

import cards.Card;
import cards.Deck;
import enums.Face;
import enums.Suit;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TPokerServer implements Runnable {

    protected ServerSocket serverSocket = null;
    protected boolean      isRunning    = false;
    protected Thread       ourThread    = null;
    protected ExecutorService pool      = Executors.newFixedThreadPool(8);

    protected static int  clientID      = 0;

    protected static Deck deck = Deck.getInstance();

    protected String faceToSend = null;
    protected String suitToSend = null;


    @Override
    public void run() {

        synchronized (this) {
            this.ourThread = Thread.currentThread();
        }

        // attempt to open our server socket
        try {
            serverSocket = new ServerSocket(ServerLauncher.PORT);
            isRunning = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(isRunning) {
            Socket client = null;

            try {
                client = this.serverSocket.accept();
                System.out.println("Connection accepted, id = " + clientID++);
            } catch (IOException e) {

                if(!isRunning) {
                    System.out.println("Server has stopped");
                    break;
                }
                throw new RuntimeException("Client connection error", e);
            }

            Random randFace = new Random();
            Random randSuit = new Random();

            // get a random set of cards
            faceToSend = Face.values()[randFace.nextInt(Face.values().length)].name();
            suitToSend = Suit.values()[randSuit.nextInt(Suit.values().length)].name();

            // adds our new thread to the pool
            this.pool.execute(
                    new TPokerThread(serverSocket, client, Integer.toString(clientID), faceToSend, suitToSend)
            );

        }

        pool.shutdown();

    }
}
