import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;

/**
 * Created by Aditya on 3/25/2019.
 */
public class LoadBalancerMain {
    public static void main(String[] args) {
        int serverPort;
        int clientPort = 6149;

        // List of servers
        String serverAddressPrefix = ".cs.hbg.psu.edu";
        List<String> serversAddresses = new ArrayList<>(Arrays.asList("ada", "dijkstra", "noyce",
                "nygaard", "euclid", "euler",
                "gauss", "riemann", "babbage"));
        List<Integer> serverPorts = new ArrayList<>(Arrays.asList(6150, 6151, 6152,
                6153, 6154, 6155,
                6156, 6157, 6158));

        List<ServerInfo> servers = new ArrayList<>();

        ListIterator<String> addrIter;
        ListIterator<Integer> portIter;

        for (addrIter = serversAddresses.listIterator(), portIter = serverPorts.listIterator();
             addrIter.hasNext() && portIter.hasNext();) {
            servers.add(new ServerInfo(addrIter.next() + serverAddressPrefix, portIter.next()));
        }

        servers.forEach(System.out::println);

        try {
            new Server(clientPort, new LoadBalancer(servers)).start();
        }

        try(ServerSocket serverSocket = new ServerSocket(clientPort)) {
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

//        new LoadBalancer().start();
    }
}
