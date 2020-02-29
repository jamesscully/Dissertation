package com.scully.server;

import java.io.*;
import java.net.Socket;

public class TPokerThread implements Runnable {

    Socket client;

    String clientID;

    public ObjectInputStream  in;
    public ObjectOutputStream out;

    public TPokerThread(Socket client, String id) {
        this.client = client;
        this.clientID = id;
    }

    boolean PRE_FLOP_DONE = false;
    boolean FLOP_DONE     = false;
    boolean TURN_DONE     =  false;
    boolean RIVER_DONE    = false;

    public boolean KEEP_ALIVE = true;

    boolean CONNECTED = true;

    @Override
    public void run() {

        while(KEEP_ALIVE) {

        }
    }

}
