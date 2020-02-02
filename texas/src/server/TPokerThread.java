package server;

import cards.Card;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TPokerThread implements Runnable {

    Socket client;

    String clientID;

    Card first, second;

    public ObjectInputStream  in;
    public ObjectOutputStream out;

    public TPokerThread(Socket client, String id, Card first, Card second) {
        this.client = client;
        this.clientID = id;

        this.first = first;
        this.second = second;
        
        System.out.println("TPokerThread: Found cards: " + first + ", " + second);
    }

    @Override
    public void run() {
        try {
            System.out.println("TPokerThread: Sending first card...");
            out.writeObject(first);
            System.out.println("TPokerThread: Sending second card...");
            out.writeObject(second);
            out.flush();

            String line = "";

            // read input from the user. If we receive DISCONNECT, then we close the connection.
            // else we simply print their instruction. These requests can be forwarded to other components in the future.
            while(!line.equals("DISCONNECT")) {
//                try {
//                    // line = in.readUTF();
//
//                } catch (IOException e) {
//                    System.out.printf("TPokerThread: Client %s has stopped responding, killing...\n", clientID);
//                    e.printStackTrace();
//                    return;
//                }
            }

            // close all IO connections
            // client.close(); out.close(); in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
