package game;

import cards.Card;
import cards.Deck;
import cards.Hand;
import cards.TexasHand;
import server.TPokerThread;

import java.io.*;
import java.net.*;

public class Player {

    public String username;

    static int _id = 0;


    public int id = 0;

    public Socket socket;


    public ObjectInputStream  objIn;
    public ObjectOutputStream objOut;


    public TPokerThread thread;

    public boolean folded = false;

    public Hand hand = new TexasHand();

    public int chips = 1000;

    public Player(Socket s) {
        id = _id++;
        this.socket = s;

        try {

            // these need to be arranged this way; they wait for the peer (client-program) to create their in/outs
            objOut = new ObjectOutputStream(socket.getOutputStream());
            objIn  = new ObjectInputStream (socket.getInputStream());

            Card c1, c2;

            // we'll pull the first two cards here
            c1 = Deck.getInstance().pullCard();
            c2 = Deck.getInstance().pullCard();

            thread = new TPokerThread(socket, Integer.toString(_id), c1, c2);

            // assign the threads in/out here; we can keep track of it.
            thread.in   = objIn;
            thread.out  = objOut;

        } catch (IOException e) {
            System.err.println("Player: Error occured");
            e.printStackTrace();
        }

    }
}
