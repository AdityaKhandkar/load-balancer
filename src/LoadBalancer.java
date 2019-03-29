import java.io.IOException;
import java.io.InputStream;
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
public class LoadBalancer implements Application {

    private static String sunlabServer = "ada.cs.hbg.psu.edu";
    private static String localhost = "localhost";
    private static String SERVERADDRESS = localhost;
    private static final int CLIENTPORT = 6149;
    private static final int SERVERPORT = 6150;
    private int serverPort;
    private Socket clientSocket;

    private static volatile List<Socket> serverList;
    private static volatile List<ServerInfo> servers;
    private Client client;
    private ServerInfo currentServer;

//    public LoadBalancer(Socket clientSoc) {
//        this.clientSocket = clientSoc;
//        serverList = new ArrayList<>();
//        connect();
//    }

    public LoadBalancer(List<ServerInfo> servers, Client client) {
        this.servers = servers;
        this.client = client;
        makeConnection();
    }

//    public static void connect() {
//        try {
//            Socket serverSocket = new Socket(SERVERADDRESS, SERVERPORT);
//            //serverSocket.connect(new InetSocketAddress(SERVERADDRESS, SERVERPORT));
//            serverList.add(serverSocket);
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//    }

    public void makeConnection() {
        List<Socket> connections = new ArrayList<>(servers.size());
        servers.forEach(s -> {
            try {
                connections.add(new Socket(s.getServerIP(), s.getPort()));
            } catch (IOException e) {
                System.err.println("In makeConnection: " + s.toString() + " " + e.getMessage());
            }
        });
    }

    public Socket getCurrentServer() {
        for(Socket s : serverList) {
            if(s.isConnected()) return s;
        }
        return null;
    }

//    public void run() {
//        Socket server = getCurrentServer();
//        try {
//            // Receive request from client
//            Scanner sc = new Scanner(clientSocket.getInputStream());
//            int clientInt = sc.nextInt();
//            System.out.println("Client sent: " + clientInt);
//
//            // Send to ServerImpl
//            new PrintStream(server.getOutputStream()).println(clientInt);
//
//            // Receive from server
//            int serverInt = new Scanner(server.getInputStream()).nextInt();
//            System.out.println("ServerImpl sent: " + serverInt);
//            // Send to client
//            new PrintStream(clientSocket.getOutputStream()).println(serverInt);
//            System.out.println("Reply from server sent to client");
//
//        } catch (Exception e) {
//            System.err.println("Exception: " + e.getLocalizedMessage());
//
//        }
//        finally {
//            try {
//                clientSocket.close();
//                System.out.println("clientSocket closed");
//                server.close();
//                System.out.println("serverSocket closed");
//            } catch (Exception e) {}
//        }
//    }

    @Override
    public String start(InputStream in) {
        // NOt
//        return client.communicate(currentServer, new Scanner(in).nextInt());
        // TODO: Find the appropriate server to contact, send message, wait for response.
        return client.communicate(servers.get(0), new Scanner(in).nextInt());

    }
}
