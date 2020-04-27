package com.scully.game;

import com.scully.cards.Card;
import com.scully.enums.PlayerInfo;
import com.scully.enums.Round;
import com.scully.server.TIdentityFile;
import com.scully.server.TPokerThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Future;

public class Player {

    public String username;

    /**
     * Static counter for the ID field
     */
    static int _id = 0;

    /**
     * Players ID / Index
     */
    public int id = 0;


    /**
     * Network socket for the player
     */
    public Socket socket;


    /**
     * ObjectInputStream for the player
     */
    public ObjectInputStream  objIn;

    /**
     * ObjectOutputStream for the player
     */
    public ObjectOutputStream objOut;

    /**
     * Thread of execution for the player
     */
    public TPokerThread thread;

    /**
     * Players identity
     */
    public TIdentityFile identityFile;

    /**
     * Whether the player has folded
     */
    public boolean folded = false;

    /**
     * Whether the player has disconnected
     */
    public boolean disconnected = false;

    /**
     * Whether the player has unexpectedly disconnected
     */
    public boolean error = false;

    /**
     * The future of the players connection thread
     */
    public Future future;

    /**
     * How many chips the player currently has
     */
    public int chips = 1000;

    public Card[] cards = new Card[7];
    public Round round = Round.PREFLOP;

    public Player(Socket s) {
        id = _id++;
        this.socket = s;

        try {

            // these need to be arranged this way; they wait for the peer (client-program) to create their in/outs
            objOut = new ObjectOutputStream(socket.getOutputStream());
            objIn  = new ObjectInputStream (socket.getInputStream());

            identityFile = (TIdentityFile) objIn.readObject();

            System.out.println("Player: Retrieved identity file with token: " + identityFile.token);

            thread = new TPokerThread(socket, Integer.toString(_id));

            // assign the threads in/out here; we can keep track of it.
            thread.in   = objIn;
            thread.out  = objOut;

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Player: Error occured");
            e.printStackTrace();
        }
    }

    /**
     * @return Sends a String to the player over network
     */
    public void sendMessage(String message) {
        try {
            objOut.writeUTF(message);
            objOut.flush();
        } catch (IOException e) {
            System.err.println("Player: error sending message to player");
        }
    }

    /**
     * @return Receives a String from the player over network
     */
    public String receiveMessage() {
        String ret = "IF YOU SEE THIS, THERE IS AN ERROR!";
        try {
            ret = objIn.readUTF();
        } catch (IOException e) {
            System.err.println("Player: error retrieving message from player");
        }

        return ret;
    }
    /**
     * @return Sends an Object to the player over network
     */
    public void sendObject(Object o) {
        try {
            objOut.writeObject(o);
            objOut.flush();
        } catch (IOException e) {
            System.err.println("Player: error sending object to server");
        }

    }

    /**
     * @return Receives Object from the player over network
     */
    public Object receiveObject() {
        Object ret = null;
        try {
            ret = objIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Player: error retrieving object from player");
        }
        return ret;
    }

    public void sendState() {

    }

    /**
     * @return Closes this players network I/O
     */
    public void close() {
        System.out.println("Player: Closing socket and streams");
        try {
            objIn.close();
            objOut.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Player: Error closing in/out sockets");
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Player: Closed socket and streams");

    }

    public void revitalizePlayer(Socket socket) {
        // close our current sockets
        // close();

        System.out.println("Player: Revitalizing player");
        this.socket = socket;
        try {
            objOut = new ObjectOutputStream(socket.getOutputStream());
            objIn  = new ObjectInputStream (socket.getInputStream());

            thread = new TPokerThread(socket, Integer.toString(id));

            // assign the threads in/out here; we can keep track of it.
            thread.in   = objIn;
            thread.out  = objOut;

            TIdentityFile temp = (TIdentityFile) objIn.readObject();


            // we only want the person that previously disconnected
            System.out.println("Player: \tValidating the same player is reconnecting");

            if(!identityFile.token.equals(temp.token))
                throw new RuntimeException("Player: \tReconnecting player was not the same");

            System.out.println("Player: \tPlayer validated, accepting");

            // let the client program know we're reconnecting them
            sendMessage("RECONNECT");

            // send them the data we have on the player
            sendObject(getPlayerInfo());


        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Player: Error revitalizing objIn/Out streams\n");
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Player: Revitalized player");
    }

    /**
     * @return Returns the players miscellaneous info
     */
    public PlayerInfo getPlayerInfo() {
        return new PlayerInfo(id, chips, cards, round);
    }
}
