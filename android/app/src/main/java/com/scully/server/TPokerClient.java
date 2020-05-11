package com.scully.server;

import android.util.Log;

import com.scully.cards.Card;
import com.scully.enums.PlayerInfo;
import com.scully.enums.Round;
import com.scully.enums.TAction;
import com.scullyapps.RekopApplication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class TPokerClient extends Thread {

    // 5 second timeout; in case max is full
    public static final int SOCKET_TIMEOUT = 5 * 1000;

    public static ObjectInputStream  in  = null;
    public static ObjectOutputStream out = null;

    public static Scanner stdIn = null;

    // we'll convert this to an array later; this is for 'sketching' purposes.
    public static Card first, second, third, fourth, fifth, sixth, seventh;

    public Round round = Round.PREFLOP;

    static PlayerInfo info = null;

    private static boolean folded = false;

    private static boolean QUIT = false;

    public TPokerClient(String host) {

        Socket sock = null;

        String HOST = "127.0.0.1";

        if(host != null)
            HOST = host;

        RekopApplication app = RekopApplication.getInstance();

        TIdentityFile identityFile = new TIdentityFile(app.getFilesDir());

        Log.d("TPokerClient", "Connecting...");

        try {
            sock = new Socket(HOST, TPokerServer.PORT);

            // in is our input stream, in this case command-line
            // out is the servers

            out   = new ObjectOutputStream(sock.getOutputStream());
            in    = new ObjectInputStream (sock.getInputStream());

            stdIn = new Scanner(System.in);

            out.writeObject(identityFile);

        } catch (IOException i) {
            Log.d("TPokerClient", "Exception Caught");
            i.printStackTrace();
        }

        String status = "REJECT";

        status = getMessage();

        if(status.equals("REJECT")) {
            System.err.println("Server rejected this connection");
            System.exit(1);
        } else {
            Log.d("TPokerClient", "Server accepted our connection");
        }

        while(!QUIT) {
            reset();
            mainLoop();
        }
    }


    // we'll want to scrub our info
    private void reset() {
        first = null; second = null; third = null; fourth = null; fifth = null; sixth = null; seventh = null;
        folded = false; QUIT = false; info = null;
        round = Round.PREFLOP;
    }

    private void mainLoop() {
        readCards();

        Log.d("TPokerClient", "Waiting for server to ask us for response.");

        while(round != Round.RESULT) {
            queryAction();
            System.err.println("Current round = " + round);
        }
    }

    private void queryAction() {
        // while we don't have a 'stay' command from com.scullyapps.rekop.texas.server, keep asking for input
        do
            waitForInput();
        while ((getMessage().equals("STAY")));

        info = (PlayerInfo) getObject();

        Log.d("TPokerClient", "Read player info");
        Log.d("TPokerClient", "\tID   : " + info.id);
        Log.d("TPokerClient", "\tChips: " + info.chips);

        // if we've got the message to move, we do so and get our new .cards
        round = Round.nextRound(round);

        // read our com.scullyapps.rekop.texas.cards for this new round
        readCards();
    }

    private void readCards() {
        System.out.printf("Waiting for %s card(s)\n", round);

        // if we've folded, we shouldn't be trying to read cards
        if(folded)
            return;

        switch (round) {
            case PREFLOP:
                first = (Card) getObject();
                second = (Card) getObject();
                System.out.printf("Retrieved \n\t%s\n\t%s\n", first, second);
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

    private String getMessage() {
        String msg = "";
        try {
            msg = in.readUTF();
        } catch (IOException e) {
            System.err.println("Error reading message from server");
            e.printStackTrace();
            System.exit(1);
        }
        return msg;
    }

    private Object getObject() {
        Object o = null;
        try {
            o = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading object from server");;
            e.printStackTrace();
            System.exit(1);
        }
        return o;
    }

    private void waitForInput() {
        String ping;
        ping = getMessage();

        if (ping.equals("PING")) {
            Log.d("TPokerClient", "\nWhat would you like to do? CALL | RAISE X | FOLD");
            inputResponse();
        } else {
            System.err.println("Signal for response was not correct. Exiting...");
            System.exit(1);
        }
    }



    public final CountDownLatch latch = new CountDownLatch(1);
    public String act = "";

    public String gin() {
        try {
            latch.await();
            return act;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return act;
    }


    SendActionTask sat;

    public void inputResponse() {
        boolean valid = false;
        String line = "";

        sat = new SendActionTask(out);

        while(!valid) {
            Log.d("TPokerClient", "Waiting for input from gin");
            line = gin();
            Log.d("TPokerClient", "Got input from gin = " + line);

            TAction action = TAction.parseTAction(line);

            // if the line isn't valid, then we'll repeat
            if(action == null)
                continue;

//            try {
                sat.execute(line);
//                out.writeUTF(line);
//                out.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            if(action == TAction.QUIT)
                System.exit(1);
            if(action == TAction.FOLD)
                folded = true;

            Log.d("TPokerClient", "Wrote data: " + line);
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

    // these should always be present
    public Card[] getCurrentHand() {
        return new Card[] {first, second};
    }

    public Card[] getTableCards() {
        Card[] arr = { third, fourth, fifth, sixth, seventh };

        ArrayList<Card> ret = new ArrayList<>();

        for(Card c : arr) {
            // no point looping - since we go from 3rd - 7th
            if(c == null)
                break;

            ret.add(c);
        }

        return (Card[]) ret.toArray();
    }
}

