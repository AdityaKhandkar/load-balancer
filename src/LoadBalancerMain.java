import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Collections;

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

        List<String> serversAddresses = new ArrayList<String>(Config.servers.length);
        Collections.addAll(serversAddresses, Config.servers);

        List<Integer> serverPorts = new ArrayList<>(Config.listenForClientPorts.length);
        Collections.addAll(serverPorts, Config.listenForClientPorts);

        List<ServerInfo> servers = new ArrayList<>();

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
