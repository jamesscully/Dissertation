package com.scully.server;

import com.scully.cards.Card;
import com.scully.enums.PlayerInfo;
import com.scully.enums.Round;
import com.scully.enums.TAction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Client program for Texas Hold'em
 */
public class TPokerClient {

    // 5 second timeout; in case max is full
    public static final int SOCKET_TIMEOUT = 5 * 1000;

    /**
     * The object input stream of client
     */
    public static ObjectInputStream  in  = null;
    /**
     * Object output stream of client
     */
    public static ObjectOutputStream out = null;

    /**
     * Scanner to read in from the console
     */
    public static Scanner stdIn = null;

    // we'll convert this to an array later; this is for 'sketching' purposes.


    static Card[] cards = new Card[7];

    /**
     * Holds the current round we are in
     */
    static public Round round = Round.PREFLOP;

    /**
     * Holds miscellaneous data about the players statistics
     */
    static PlayerInfo info = null;

    /**
     * Whether this client has folded
     */
    private static boolean folded = false;

    /**
     * Whether this client has opted to quit
     */
    private static boolean hasQuit = false;


    public static void main(String[] args) {

        try {
            Socket sock = null;
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
            stdIn = new Scanner(System.in);

            out.writeObject(identityFile);

            String status = "REJECT";

            status = getMessage();

            if(status.equals("REJECT")) {
                System.err.println("TPokerClient: Server rejected this connection");
                System.exit(1);
            } else if (status.equals("ACCEPT")) {
                System.out.println("TPokerClient: Server accepted our connection");
            } else if (status.equals("RECONNECT")) {
                System.out.println("TPokerClient: Reconnected to server, waiting for the game state");
                reconnecting = true;
            }

        } catch (IOException i) {
            System.out.println("TPokerClient: Exception Caught");
            i.printStackTrace();
        }

        while(!hasQuit) {
            reset();
            mainLoop();
        }
    }

    /**
     * Reset this client to normal
     */
    private static void reset() {
        cards[0] = null; cards[1] = null; cards[2] = null; cards[3] = null; cards[4] = null; cards[5] = null; cards[6] = null;
        folded = false; hasQuit = false; info = null;
        round = Round.PREFLOP;
    }

    private static void getStateInfo() {
        info = (PlayerInfo) getObject();
        System.arraycopy(info.visibleCards, 0, cards, 0, 7);
        round = info.round;
        printCurrentHand();
    }

    static boolean reconnecting = false;
    /**
     * Main loop we use when playing the game
     */
    private static void mainLoop() {
        if(!reconnecting) {
            readCards();
        } else {
            getStateInfo();
        }

        System.out.println("TPokerClient: Waiting for server to ask us for response.");

        while(round != Round.RESULT) {
            queryAction();
//            System.err.println("TPokerClient: Current round = " + round);
        }
    }

    /**
     * Wait for the server to ask us for our action, and send it
     */
    private static void queryAction() {
        // while we don't have a 'stay' command from com.scully.server, keep asking for input
        do
            waitForInput();
        while ((getMessage().equals("STAY")));

        info = (PlayerInfo) getObject();

//        System.out.println("TPokerClient: Read player info");
//        System.out.println("\tID   : " + info.id);
//        System.out.println("\tChips: " + info.chips);
//        System.out.println("\tCards: " + Arrays.toString(info.visibleCards));
//        System.out.println("\tRound: " + info.round);

        // if we've got the message to move, we do so and get our new .cards
        round = Round.nextRound(round);

        // read our com.scully.cards for this new round
        readCards();

    }

    /**
     * Reads cards into our hand depending on the round
     */
    private static void readCards() {
        System.out.printf("TPokerClient: Waiting for %s card(s)\n", round);

        // if we've folded, we shouldn't be trying to read cards
        if(folded)
            return;

        switch (round) {
            case PREFLOP:
                cards[0] = (Card) getObject();
                cards[1] = (Card) getObject();

//                System.out.printf("TPokerClient: Retrieved \n\t%s\n\t%s\n", cards[0], cards[1]);
                break;

            case FLOP:
                cards[2] = (Card) getObject();
                cards[3] = (Card) getObject();
                cards[4] = (Card) getObject();


//                System.out.printf("TPokerClient: Retrieved \n\t%s\n\t%s\n\t%s\n", cards[2], cards[3], cards[4]);
                break;

            case TURN:
                cards[5] = (Card) getObject();
//                System.out.printf("TPokerClient: Retrieved \n\t%s\n", cards[5]);
                break;

            case RIVER:
                cards[6] = (Card) getObject();
//                System.out.printf("TPokerClient: Retrieved \n\t%s\n", cards[6]);
                break;
        }
        printCurrentHand();
    }

    /**
     * Retrieve a String from the ObjectInputStream
     * @return String containing a message from the server
     */
    private static String getMessage() {
        String msg = "";
        try {
            msg = in.readUTF();
        } catch (IOException e) {
            System.err.println("TPokerClient: Error reading message from server");
            e.printStackTrace();
            System.exit(1);
        }
        return msg;
    }

    /**
     * Retrieve an Object from the ObjectInputStream
     * @return Object from the server
     */
    private static Object getObject() {
        Object o = null;
        try {
            o = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("TPokerClient: Error reading object from server");;
            e.printStackTrace();
            System.exit(1);
        }
        return o;
    }

    /**
     * Waits for a PING message from server, then asks for input
     */
    private static void waitForInput() {
        String ping;
        ping = getMessage();

        if (ping.equals("PING")) {
            System.out.printf("\nWhat would you like to do? Options: CALL | RAISE (AMT) | FOLD | CHIPS\nChoice: ");
            inputResponse();
        } else {
            System.err.println("TPokerClient: Signal for response was not correct. Exiting...");
            System.exit(1);
        }
    }

    /**
     * Asks the user to input their action, and checks if their intended action is valid
     */
    public static void inputResponse() {
        boolean valid = false;
        String line = "";

        while(!valid) {
            line = stdIn.nextLine();

            if(line.toUpperCase().equals("CHIPS")) {
                System.out.printf("TPokerClient: You currently have %d chips\n", info.chips);
            }

            TAction action = TAction.parseTAction(line);

            // if the line isn't valid, then we'll repeat
            if(action == null)
                continue;

            try {
                out.writeUTF(line);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(action == TAction.QUIT)
                System.exit(1);
            if(action == TAction.FOLD)
                folded = true;

            // System.out.println("TPokerClient: Sending action " + line + " to server.");
            valid = true;
        }
    }

    /**
     * Prints the current hands we have, and the current hands on the table.
     */
    public static void printCurrentHand() {

        try {
            Runtime.getRuntime().exec("clear");
        } catch (IOException ignored) { }

        System.out.println();
        System.out.println("==================================");
        System.out.println("New round has begun: " + round);
        if(info != null)
            System.out.println("You currently have " + info.chips + " chips");
        System.out.println("==================================");
        System.out.println();

        System.out.printf("Current cards in play:\n\t[%s %s] ", cards[0].toShortString(), cards[1].toShortString());

        for(int i = 2; i < cards.length; i++) {
            if(cards[i] == null)
                continue;

            System.out.print(cards[i].toShortString() + " ");
        }
        System.out.print("\n");
    }
}
