package server;

import cards.Card;
import enums.Face;
import enums.Suit;
import enums.TAction;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class TPokerClient {

    // 5 second timeout; in case max is full
    public static final int SOCKET_TIMEOUT = 5 * 1000;

    public static ObjectInputStream  in  = null;
    public static ObjectOutputStream out = null;

    public static Scanner stdIn = null;


    public static void main(String[] args) {
        Socket sock = null;

        Card first, second;

        try {

            sock = new Socket("127.0.0.1", TPokerServer.PORT);

            // in the case the server is full, we'll get a timeout
            // sock.setSoTimeout(SOCKET_TIMEOUT);

            System.out.println("TPokerClient: Connecting...");

            // in is our input stream, in this case command-line
            // out is the servers
            out = new ObjectOutputStream(sock.getOutputStream());
             in = new ObjectInputStream (sock.getInputStream());

            stdIn = new Scanner(System.in);

            System.out.println("TPokerClient: Retrieving first card...");
            first  = (Card) in.readObject();
            System.out.println("TPokerClient: Got first, retrieving second...");
            second = (Card) in.readObject();

            System.out.println(
                    String.format("Retrieved cards: \n %s \n %s", first, second)
            );


            System.out.println("Waiting for server to ask us for response.");
            String ping = in.readUTF();

            if(ping.equals("PING")) {
                System.out.println("What would you like to do? CALL | RAISE X | FOLD");
                inputResponse();
            }

        } catch (ConnectException e) {
            System.err.println("TPokerClient: Unable to connect to the server; is it running?");
            return;
        } catch (SocketTimeoutException e) {
            System.err.println("TPokerClient: There was an error connecting to the server; it may be full.");
            return;
        } catch (IOException | ClassNotFoundException i) {
            System.out.println("TPokerClient: Exception Caught");
            i.printStackTrace();
        }

        try {
            System.out.println("TPokerClient: Closing sockets.");
            in.close(); out.close(); sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void inputResponse() throws IOException {

        String line = "";

        while(!line.equals("disc")) {
            try {
                line = stdIn.nextLine();

                System.out.println("Got input: " + line );

                if(TAction.parseTAction(line) == null) {
                    continue;
                }

                System.out.println("TPokerClient: TPokerClient: Writing data: " + line);

                out.writeUTF(line);
                out.flush();

                System.out.println("TPokerClient: TPokerClient: Wrote data: " + line);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
