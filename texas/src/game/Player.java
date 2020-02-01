package game;

import cards.Card;
import cards.Deck;
import cards.Hand;
import cards.TexasHand;
import server.TPokerThread;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class Player {

    public String username;

    static int id = 0;

    public Socket socket;


    public ObjectInputStream  objIn;
    public ObjectOutputStream objOut;


    public TPokerThread thread;

    public boolean folded = false;

    public Hand hand = new TexasHand();

    public int chips = 1000;

    public Player(Socket s) {
        id = id++;
        this.socket = s;

        try {

            System.err.println("Player: setObjOut");
            objOut = new ObjectOutputStream(socket.getOutputStream());

            System.err.println("Player: setObjIn");
            objIn  = new ObjectInputStream (socket.getInputStream());


            Card c1, c2;

            c1 = Deck.getInstance().pullCard();
            c2 = Deck.getInstance().pullCard();

            thread = new TPokerThread(socket, Integer.toString(id), c1, c2);

            thread.in = objIn;
            thread.out  = objOut;

        } catch (IOException e) {
            System.err.println("Player: Error occured");
            e.printStackTrace();
        }

    }
}
