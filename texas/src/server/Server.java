package server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int PORT = 1337;

    public static void main(String[] args) {
        try(
                ServerSocket serverSocket = new ServerSocket(PORT);
                Socket clientSocket = serverSocket.accept()
            )
        {

        } catch (Exception e) {

        }
    }





}
