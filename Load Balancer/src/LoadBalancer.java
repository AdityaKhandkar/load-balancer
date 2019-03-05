import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.util.Scanner;

/**
 * Created by Aditya on 2/14/2019.
 */
public class LoadBalancer implements Runnable {

    private static String SERVERADDRESS = "127.0.0.1";
    private static final int CLIENTPORT = 1341;
    private static final int SERVERPORT = 1342;
    private Socket clientSocket, serverSocket;

    public LoadBalancer(Socket clientSoc, Socket serverSoc) {
        this.clientSocket = clientSoc;
        this.serverSocket = serverSoc;
    }

    public void run() {
        synchronized(this) {
            try {
                while(clientSocket.isConnected() || serverSocket.isConnected()) {
                    // Receive request from client
                    Scanner sc = new Scanner(clientSocket.getInputStream());
                    int clientInt = sc.nextInt();
                    System.out.println("Client sent: " + clientInt);

                    // Send to Server
                    new PrintStream(serverSocket.getOutputStream()).println(clientInt);

                    // Receive from server
                    int serverInt = new Scanner(serverSocket.getInputStream()).nextInt();
                    System.out.println("Server sent: " + serverInt);
                    // Send to client
                    new PrintStream(clientSocket.getOutputStream()).println(serverInt);
                    System.out.println("Reply from server sent to client");
                }
            } catch (Exception e) {
//                System.err.println("Exception: " + e.getLocalizedMessage());
            }
            finally {
                try {
                    clientSocket.close();
                    System.out.println("clientSocket closed");
//                    if(!serverSocket.isConnected()) {
                        serverSocket.close();
                        System.out.println("serverSocket closed");
//                    }
                } catch (Exception e) {}
            }
        }
    }

    public static void main(String[] args) throws Exception {

        Socket clientSocket,
               serverSocket = new Socket();

        InetSocketAddress serverAddress = new InetSocketAddress(SERVERADDRESS, SERVERPORT);
        serverSocket.connect(serverAddress);

        ServerSocket serverConnection;
        ServerSocket clientConnection = new ServerSocket(CLIENTPORT);
        clientSocket = clientConnection.accept();

        Thread serverThread = new Thread(new LoadBalancer(clientSocket, serverSocket));
        serverThread.start();

        while(true) {
            if(serverSocket.isClosed() && clientSocket.isClosed()) {
                System.out.println("Waiting for the server to reconnect...");
                serverSocket = new Socket();
                serverSocket.connect(serverAddress);
//                serverConnection = new ServerSocket(SERVERPORT);
//                serverSocket = serverConnection.accept();
                System.out.println("Server connected");

                System.out.println("Waiting for client to reconnect");
//                connection = new ServerSocket(CLIENTPORT);
                clientSocket = clientConnection.accept();
                System.out.println("clientSocket.isClosed(): " + clientSocket.isClosed());

                new Thread(new LoadBalancer(clientSocket, serverSocket)).start();
            }

//            if(clientSocket.isClosed()) {
//                System.out.println("Waiting for client to reconnect");
////                connection = new ServerSocket(CLIENTPORT);
//                clientSocket = clientConnection.accept();
//                System.out.println("clientSocket.isClosed(): " + clientSocket.isClosed());
//                new Thread(new LoadBalancer(clientSocket, serverSocket)).start();
//            }
        }
    }
}
