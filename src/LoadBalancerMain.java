import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Aditya on 3/25/2019.
 */
public class LoadBalancerMain {
    public static void main(String[] args) {

        boolean debug = false;

        int serverPort = 6150;
        int clientPort = 6149;
        String localhost = "localhost";

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

        if(debug) {
            for (addrIter = serversAddresses.listIterator(), portIter = serverPorts.listIterator();
                 addrIter.hasNext() && portIter.hasNext();) {
                servers.add(new ServerInfo(addrIter.next() + serverAddressPrefix, portIter.next()));
            }
        } else {
            servers.add(new ServerInfo(localhost, serverPort));
        }

        try {
            new Server(clientPort, new LoadBalancer(servers, new Client())).start();
        } catch (Exception e) {
            System.err.println("In LoadBalancerMain" + e.getMessage());
        }
    }
}
