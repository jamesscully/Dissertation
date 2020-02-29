package com.scully.server;

import com.scully.cards.Card;
import com.scully.enums.PlayerInfo;
import com.scully.enums.Round;
import com.scully.enums.TAction;

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

    // we'll convert this to an array later; this is for 'sketching' purposes.
    static Card first, second, third, fourth, fifth, sixth, seventh;

    static public Round round = Round.PREFLOP;

    static PlayerInfo info = null;

    private static boolean QUIT = false;


    public static void main(String[] args) {

        while(!QUIT) {
            Socket sock = null;

            try {
                String HOST = "127.0.0.1";

                if(args.length > 0) {
                    HOST = args[0];
                }

                TIdentityFile identityFile = new TIdentityFile();

                System.out.println("TPokerClient: Connecting...");
                sock = new Socket(HOST, TPokerServer.PORT);

                // in is our input stream, in this case command-line
                // out is the servers
                out = new ObjectOutputStream(sock.getOutputStream());
                in = new ObjectInputStream (sock.getInputStream());

                out.writeObject(identityFile);

                stdIn = new Scanner(System.in);


                String status = "REJECT";

                status = in.readUTF();

                if(status.equals("REJECT")) {
                    System.err.println("TPokerClient: server rejected this connection");
                    System.exit(1);
                } else {
                    System.out.println("TPokerClient: Server accepted out connection");
                }




                System.out.println("TPokerClient: Waiting for initial hand...");
                first  = (Card) in.readObject();
                second = (Card) in.readObject();
                System.out.printf("TPokerClient: Retrieved \n\t%s\n\t%s\n", first, second);
                System.out.println("TPokerClient: Waiting for server to ask us for response.");

                while(round != Round.RESULT) {
                    queryAction();
                    System.err.println("TPokerClient: Current round = " + round);
                }

            } catch (ConnectException e) { System.err.println("TPokerClient: Unable to connect to the server; is it running?"); }
            catch (SocketTimeoutException e) { System.err.println("TPokerClient: There was an error connecting to the server; it may be full."); }
            catch (IOException | ClassNotFoundException i) {
                System.out.println("TPokerClient: Exception Caught");
                i.printStackTrace();
            }

            try {
                System.out.println("TPokerClient: Closing sockets.");
                in.close(); out.close(); sock.close();
                QUIT = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This waits for our input, then if we receive a STAY message (i.e. RAISE) we repeat
     * @throws IOException
     */
    private static void queryAction() throws IOException {
        // while we don't have a 'stay' command from com.scully.server, keep asking for input
        do
            waitForInput();
        while ((in.readUTF().equals("STAY")));

        try {
            info = (PlayerInfo) in.readObject();

            System.out.println("TPokerClient: Read player info");
            System.out.println("\tID   : " + info.id);
            System.out.println("\tChips: " + info.chips);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // if we've got the message to move, we do so and get our new .cards
        round = Round.nextRound(round);

        // read our com.scully.cards for this new round
        readCards();
    }

    private static void readCards() {
        System.out.printf("Waiting for %s card(s)\n", round);
        try {
            switch (round) {
                case FLOP:
                    third  = (Card) in.readObject();
                    fourth = (Card) in.readObject();
                    fifth  = (Card) in.readObject();
                    System.out.printf("Retrieved \n\t%s\n\t%s\n\t%s\ncard(s)\n", third, fourth, fifth);
                    return;

                case TURN:
                    sixth = (Card) in.readObject();
                    System.out.printf("Retrieved %s card\n", sixth);
                    break;

                case RIVER:
                    seventh = (Card) in.readObject();
                    System.out.printf("Retrieved %s card\n", seventh);
                    break;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("TPokerClient: Error occured in readCards()");
            e.printStackTrace();
        }
    }


    private static void waitForInput() throws IOException {
        String ping;
        ping = in.readUTF();

        if (ping.equals("PING")) {
            System.out.println("\nWhat would you like to do? CALL | RAISE X | FOLD");
            inputResponse();
        } else {
            System.err.println("TPokerClient: Signal for response was not correct. Exiting...");
            System.exit(1);
        }
    }

    public static void inputResponse() {
        boolean valid = false;

        String line = "";

        while(!valid) {
            try {
                line = stdIn.nextLine();

                TAction action = TAction.parseTAction(line);

                // if the line isn't valid, then we'll repeat
                if(action == null)
                    continue;

                System.out.println("TPokerClient: TPokerClient: Writing data: " + line);

                out.writeUTF(line);
                out.flush();

                if(action == TAction.QUIT)
                    QUIT = true;

                System.out.println("TPokerClient: TPokerClient: Wrote data: " + line);

                valid = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
