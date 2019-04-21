import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Aditya on 3/25/2019.
 */

public class LoadBalancerMain {
    public static void main(String[] args) {

        boolean debug = false;

        // Only for local testing
        int serverPort = 6149;
        String localhost = "localhost";

        // For sunlab use
        int clientPort = 6150;

        // List of servers
        String serverAddressPrefix = ".cs.hbg.psu.edu";

        List<String> serversAddresses = new CopyOnWriteArrayList<>();
        Collections.addAll(serversAddresses, Config.servers);

        List<Integer> serverPorts = new CopyOnWriteArrayList<>();
        Collections.addAll(serverPorts, Config.listenForClientPorts);

        CopyOnWriteArrayList<ServerInfo> servers = new CopyOnWriteArrayList<>();

        ListIterator<String> addrIter;
        ListIterator<Integer> portIter;

        if(debug) {
            servers.add(new ServerInfo(localhost, serverPort));
        } else {  
            for (addrIter = serversAddresses.listIterator(), portIter = serverPorts.listIterator();
                 addrIter.hasNext() && portIter.hasNext();) {
                servers.add(new ServerInfo(addrIter.next() + serverAddressPrefix, portIter.next()));
            }
        }

        try {
            new Server(clientPort, Config.LB_THREAD_LIMIT, new LoadBalancer(servers, new Client())).start();
        } catch (Exception e) {
            System.err.println("In LoadBalancerMain: " + e.getMessage());
        }
    }
}
