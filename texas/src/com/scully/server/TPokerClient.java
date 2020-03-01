package com.scully.server;

import com.scully.cards.Card;
import com.scully.enums.PlayerInfo;
import com.scully.enums.Round;
import com.scully.enums.TAction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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

    private static boolean folded = false;

    private static boolean QUIT = false;


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
            } else {
                System.out.println("TPokerClient: Server accepted our connection");
            }

        } catch (IOException i) {
            System.out.println("TPokerClient: Exception Caught");
            i.printStackTrace();
        }

        while(!QUIT) {
            reset();
            mainLoop();
        }
    }

    // we'll want to scrub our info
    private static void reset() {
        first = null; second = null; third = null; fourth = null; fifth = null; sixth = null; seventh = null;
        folded = false; QUIT = false; info = null;
        round = Round.PREFLOP;
    }

    private static void mainLoop() {
        readCards();

        System.out.println("TPokerClient: Waiting for server to ask us for response.");

        while(round != Round.RESULT) {
            queryAction();
            System.err.println("TPokerClient: Current round = " + round);
        }
    }

    private static void queryAction() {
        // while we don't have a 'stay' command from com.scully.server, keep asking for input
        do
            waitForInput();
        while ((getMessage().equals("STAY")));

        info = (PlayerInfo) getObject();

        System.out.println("TPokerClient: Read player info");
        System.out.println("\tID   : " + info.id);
        System.out.println("\tChips: " + info.chips);

        // if we've got the message to move, we do so and get our new .cards
        round = Round.nextRound(round);

        // read our com.scully.cards for this new round
        readCards();
    }

    private static void readCards() {
        System.out.printf("TPokerClient: Waiting for %s card(s)\n", round);

        // if we've folded, we shouldn't be trying to read cards
        if(folded)
            return;

        switch (round) {
            case PREFLOP:
                first = (Card) getObject();
                second = (Card) getObject();
                System.out.printf("TPokerClient: Retrieved \n\t%s\n\t%s\n", first, second);
                break;

            case FLOP:
                third  = (Card) getObject();
                fourth = (Card) getObject();
                fifth  = (Card) getObject();
                break;

            case TURN:
                sixth = (Card) getObject();
                break;

            case RIVER:
                seventh = (Card) getObject();
                break;
        }

        printCurrentHand();
    }

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

    private static void waitForInput() {
        String ping;
        ping = getMessage();

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
            line = stdIn.nextLine();

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

            System.out.println("TPokerClient: TPokerClient: Wrote data: " + line);
            valid = true;
        }
    }

    public static void printCurrentHand() {
        Card[] arr = {first, second, third, fourth, fifth, sixth, seventh};
        System.out.printf("Current cards in play:\n\t[%s %s] ", first.toShortString(), second.toShortString());
        for(int i = 2; i < arr.length; i++) {
            if(arr[i] == null)
                continue;

            System.out.print(arr[i].toShortString() + " ");
        }
        System.out.print("\n");
    }
}
