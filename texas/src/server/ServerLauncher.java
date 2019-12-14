package server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerLauncher {

    public static final int PORT = 1337;

    public static void main(String[] args) {
        TPokerServer server = new TPokerServer();

        new Thread(server).start();

        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }





}
