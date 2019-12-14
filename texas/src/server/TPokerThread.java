package server;

import enums.Face;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TPokerThread implements Runnable {

    Socket client;
    ServerSocket server;

    String data, face, suit;

    public TPokerThread(ServerSocket server, Socket client, String id, String face, String suit) {
        this.client = client; this.server = server;
        this.data = id;       this.face = face; this.suit = suit;
    }

    @Override
    public void run() {
        try {

            DataInputStream in  = new DataInputStream(new BufferedInputStream(client.getInputStream()));
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());

            String sendData = data + " " + face + " " + suit;
            System.out.println("Sending data: " + sendData);

            out.writeUTF(sendData);
            out.flush();
            String line = "";

            // read input from the user. If we receive DISCONNECT, then we close the connection.
            // else we simply print their instruction. These requests can be forwarded to other components in the future.
            while(!line.equals("DISCONNECT")) {
                try {
                    line = in.readUTF();

                    if(line.equals("CALL")) {
                        System.out.printf("ClientID: %s has called for next round\n", data);
                    } else if (line.equals("FOLD")) {
                        System.out.printf("ClientID: %s has folded\n", data);
                    } else if (line.split("\\s+")[0].equals("RAISE")) {
                        System.out.printf("ClientID: %s has raised by: %s\n", data, line.split("\\s+")[1]);
                    } else {
                        System.out.printf("ClientID: %s, Message: %s\n", data, line);
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }

            // close all IO connections
            client.close(); out.close(); in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
