package server;

import cards.Card;
import enums.Face;
import enums.Suit;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class TPokerClient {


    public static void main(String[] args) {
        Socket sock = null;

        DataOutputStream out = null;
        DataInputStream  in  = null;
        ObjectInputStream servOut = null;

        try {
            sock = new Socket("127.0.0.1", 1337);
            System.out.println("Connected");

            // in is our input stream, in this case command-line
            // out is the servers
            in = new DataInputStream(System.in);
            out = new DataOutputStream(sock.getOutputStream());

            servOut = new ObjectInputStream(sock.getInputStream());

            String line = (String) servOut.readUTF();

            if(line == null || line.isEmpty()) {
                System.out.println("Line was null or empty");
            } else {
                System.out.println("Retrieved data (connectionID, Face, Suit): " + line);
                getCardFromData(line);
            }

        } catch (IOException i) {
            System.out.println("Exception Caught");
            i.printStackTrace();
        }

        String line = "";

        while(!line.equals("disc")) {
            try {
                line = in.readLine();
                out.writeUTF(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            in.close(); out.close(); sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void getCardFromData(String data) {

        // our data is in the form of: connectionID, Face and Suit so, 0, 1, 2
        String[] split = data.split("\\s+");

        Face face = Face.valueOf(split[1]);
        Suit suit = Suit.valueOf(split[2]);

        Card card = new Card(suit, face);

        System.out.println(card.toString());

    }


}
