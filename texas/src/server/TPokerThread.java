package server;

import cards.Card;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TPokerThread implements Runnable {

    Socket client;

    String clientID;

    Card first, second;
    Card third, fourth, fifth;

    public ObjectInputStream  in;
    public ObjectOutputStream out;

    public TPokerThread(Socket client, String id, Card first, Card second) {
        this.client = client;
        this.clientID = id;

        this.first = first;
        this.second = second;
        
        System.out.println("TPokerThread: Found cards: " + first + ", " + second);
    }

    boolean PRE_FLOP_DONE = false;
    boolean FLOP_DONE     = false;
    boolean TURN_DONE     =  false;
    boolean RIVER_DONE    = false;

    boolean CONNECTED = true;

    @Override
    public void run() {
        try {
            out.writeObject(first);
            out.writeObject(second);
            out.flush();

            while(!PRE_FLOP_DONE) {

            }

//            out.writeObject(third);
//            out.writeObject(fourth);
//            out.writeObject(fifth);
//            out.flush();

            while(!FLOP_DONE) {

            }



            // close all IO connections
            // client.close(); out.close(); in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
