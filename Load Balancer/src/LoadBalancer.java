import java.io.PrintStream;
import java.net.*;
import java.util.Scanner;

/**
 * Created by Aditya on 2/14/2019.
 */
public class LoadBalancer {

    public static void main(String[] args) throws Exception {

        final int CLIENTPORT = 1341;
        final int SERVERPORT = 1342;

        Socket clientSocket,
               serverSocket = new Socket("127.0.0.1", SERVERPORT);
        // Receive request from client
        ServerSocket fromClient = new ServerSocket(CLIENTPORT);
        clientSocket = fromClient.accept();
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
}
