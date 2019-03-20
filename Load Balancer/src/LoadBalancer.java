import java.io.PrintStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Aditya on 2/14/2019.
 */
public class LoadBalancer implements Runnable {

    private static String SERVERADDRESS = "ada.cs.hbg.psu.edu";
    private static final int CLIENTPORT = 6149;
    private static final int SERVERPORT = 6150;
    private Socket clientSocket;

    private static volatile List<Socket> serverList;

    public LoadBalancer(Socket clientSoc) {
        this.clientSocket = clientSoc;
        serverList = new ArrayList<>();
        connect();
    }

    public static void connect() {
        try {
            Socket serverSocket = new Socket(SERVERADDRESS, SERVERPORT);
            //serverSocket.connect(new InetSocketAddress(SERVERADDRESS, SERVERPORT));
            serverList.add(serverSocket);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public Socket getCurrentServer() {
        for(Socket s : serverList) {
            if(s.isConnected()) return s;
        }
        return null;
    }

    public void run() {
        Socket server = getCurrentServer();
        try {
            // Receive request from client
            Scanner sc = new Scanner(clientSocket.getInputStream());
            int clientInt = sc.nextInt();
            System.out.println("Client sent: " + clientInt);

            // Send to Server
            new PrintStream(server.getOutputStream()).println(clientInt);

            // Receive from server
            int serverInt = new Scanner(server.getInputStream()).nextInt();
            System.out.println("Server sent: " + serverInt);
            // Send to client
            new PrintStream(clientSocket.getOutputStream()).println(serverInt);
            System.out.println("Reply from server sent to client");

        } catch (Exception e) {
            System.err.println("Exception: " + e.getLocalizedMessage());

        }
        finally {
            try {
                clientSocket.close();
                System.out.println("clientSocket closed");
                server.close();
                System.out.println("serverSocket closed");
            } catch (Exception e) {}
        }
    }

    public static void main(String[] args) {

        try(ServerSocket serverSocket = new ServerSocket(CLIENTPORT)) {
            Socket socket;
            ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

            while(true) {
                try {
                    System.out.println("Waiting for the client to connect...");
                    socket = serverSocket.accept();
                    pool.execute(new LoadBalancer(socket));
                    System.out.println("Threads which completed their tasks: " + pool.getCompletedTaskCount());
                    System.out.println("Client connected");
                    Thread.sleep(100);
                    System.out.println("Active threads: " + pool.getActiveCount());
                }
                catch(Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
