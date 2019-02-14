/**
 * Created by Aditya on 1/17/2019.
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server implements Runnable {

    private Socket socket;
    private ServerSocket serverSocket;

    public Server(Socket socket) throws Exception {
//        this.serverSocket = serverSocket;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while(true) {
                // Read in the number sent by the load balancer
                Scanner sc = new Scanner(socket.getInputStream());
                int inNum = sc.nextInt();
                System.out.println(inNum);

                // Double the number and send it back
                new PrintStream(socket.getOutputStream()).println(inNum * 2);
            }
        }

        catch (Exception e) {}

        finally {
            try {
                closeSockets();
            } catch (IOException e) {}
            System.out.println("Connection with load balancer closed.");
        }
    }

    private void closeSockets() throws IOException {
        socket.close();
    }

//    private void closeServerSocket() throws IOException {
//        serverSocket.close();
//    }

    public static void main(String[] args) throws Exception {

        final int PORTNUM = 1342;

        // Accept the connection
        ServerSocket serverSocket = new ServerSocket(PORTNUM);
        Socket socket = serverSocket.accept();
        Thread serverThread = new Thread(new Server(socket));
        serverThread.start();

        while(true) {
            if(socket.isClosed()) {
                System.out.println("Waiting for the load balancer to connect...");
                socket = serverSocket.accept();
                new Thread(new Server(socket)).start();
            }
        }
    }
}
