import java.util.List;
import java.util.ListIterator;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Aditya on 3/25/2019.
 */

public class LoadBalancerMain {
    public static void main(String[] args) {

        int clientPort = 6150;

        // List of servers
        String serverAddressPrefix = ".cs.hbg.psu.edu";

        // Making a list of ServerInfo objects using the servers and their
        // respective clientPorts
        List<String> serversAddresses = new CopyOnWriteArrayList<>();
        Collections.addAll(serversAddresses, Config.servers);

        List<Integer> serverPorts = new CopyOnWriteArrayList<>();
        Collections.addAll(serverPorts, Config.clientPorts);

        CopyOnWriteArrayList<ServerInfo> servers = new CopyOnWriteArrayList<>();

        ListIterator<String> addrIter;
        ListIterator<Integer> portIter;

        for (addrIter = serversAddresses.listIterator(), portIter = serverPorts.listIterator();
             addrIter.hasNext() && portIter.hasNext();) {
            servers.add(new ServerInfo(addrIter.next() + serverAddressPrefix, portIter.next()));
        }

        // Start the server and pass it the load balancer application.
        try {
            new Server(clientPort, Config.LB_THREAD_LIMIT, new LoadBalancer(servers, new Client())).start();
        } catch (Exception e) {
            System.err.println("In LoadBalancerMain: " + e.getMessage());
        }
    }
}
