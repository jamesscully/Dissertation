package server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerLauncher {


    public static void main(String[] args) {
        TPokerServer server = new TPokerServer();

        new Thread(server).start();

    }





}
