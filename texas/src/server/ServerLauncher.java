package server;

public class ServerLauncher {


    public static void main(String[] args) {
        TPokerServer server = new TPokerServer();

        new Thread(server).start();

    }





}
