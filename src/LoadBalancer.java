import java.util.*;
import java.util.concurrent.Future;

/**
 * Created by Aditya on 2/14/2019.
 */
class LoadBalancer implements Application {

    private final int MAX_ITERATIONS = 100000;
    private volatile List<ServerInfo> servers;
    public volatile List<ServerStatus> status;
    private Client client;


    public LoadBalancer(List<ServerInfo> servers, Client client) {
        this.servers = servers;
        this.client = client;
        loadServerStatus();
    }

    public void loadServerStatus() {
        status = new ArrayList<>();
        servers.forEach(s -> status.add(new ServerStatus(s)));
    }

    // Load balancing algorithm
    @Override
    public String start(long msg) {
        int iteration = 0;

        do {
            // TODO: Figure out which server to send request to, and wait for response
            for(int i = 0; i < status.size(); i++) {
                ServerStatus serverStatus = status.get(i);

                int availableThreads = serverStatus.getAvailableThreads();

                System.out.println("serverThreads: " + availableThreads);
                System.out.println("Message from client: " + msg);

                if(availableThreads > 0) {
                    serverStatus.setAvailableThreads(availableThreads--);

                    prioritizeUnavailable(i);

                    String response = client.communicate(serverStatus.getServerInfo(), msg);
                    serverStatus.setAvailableThreads(availableThreads++);

                    prioritizeAvailable(i);

                    return response;
                }
            }
        } while (iteration++ > MAX_ITERATIONS);

        System.out.println("All servers are busy.");

        // If all servers are currently busy, restart the process.
        return Client.EXCEPTION + " Servers busy. Please try again later.";
    }

    public synchronized void prioritizeAvailable(int index) {
        if(index == 0) return;

        ServerStatus serverStatus = status.get(index);

        for(int i = index - 1; i >= 0; i--) {
            if(serverStatus.compareTo(status.get(i)) <= 0) {
                serverStatus = status.remove(index);
                status.add(i, serverStatus);
                break;
            }
        }
    }

    public synchronized void prioritizeUnavailable(int index) {
        if(index == status.size() - 1) return;

        ServerStatus serverStatus = status.get(index);

        for(int i = index + 1; i < status.size(); i++) {
            if(serverStatus.compareTo(status.get(i)) >= 0) {
                serverStatus = status.remove(index);
                status.add(i, serverStatus);
                break;
            }
        }
    }

    @Override
<<<<<<< HEAD
    public String start(InputStream in) {
        // NOt
//        return client.communicate(currentServer, new Scanner(in).nextInt());
        // TODO: Find the appropriate server to contact, send message, wait for response.
        return client.communicate(servers.get(0), new Scanner(in).nextInt());
=======
    public String type() {
        return "Load Balancer";
    }

    public static void main(String[] args) {

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

        LoadBalancer lb = new LoadBalancer(servers, new Client());

        lb.status.get(5).setAvailableThreads(6);
        System.out.println("before prioritizing " + lb.status.get(5).getServerInfo().toString());
        System.out.println(lb.status.toString());
        lb.prioritizeAvailable(5);
        System.out.println("after prioritizing " + lb.status.get(5).getServerInfo().toString());
        System.out.println(lb.status.toString());

        System.out.println("before prioritizing " + lb.status.get(3).getServerInfo().toString());
        System.out.println(lb.status.toString());
        lb.prioritizeUnavailable(3);
        System.out.println("after prioritizing " + lb.status.get(3).getServerInfo().toString());
        System.out.println(lb.status.toString());

>>>>>>> bbb2a73472ca996092150e54450ed8cff7be3bdb

    }
}
