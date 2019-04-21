import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Aditya on 2/14/2019.
 */
class LoadBalancer implements Application {

    private final int MAX_ITERATIONS = 1000000;
    private volatile CopyOnWriteArrayList<ServerInfo> servers;
    private volatile PriorityBlockingQueue<ServerStatus> status;
    private Client client;

    public LoadBalancer(CopyOnWriteArrayList<ServerInfo> servers, Client client) {
        this.servers = servers;
        this.client = client;
        loadServerStatus();
    }

    public void loadServerStatus() {
        status = new PriorityBlockingQueue<>(servers.size(),
                (t1, t2) -> t2.getAvailableThreads() - t1.getAvailableThreads());
        servers.forEach(s -> status.add(new ServerStatus(s)));
        System.out.println("In load balancer loadServerStatus: " + servers);
    }

    // Load balancing algorithm
    @Override
    public String start(int msg) {
        int iteration = 0;

        while (iteration++ < MAX_ITERATIONS) {

            ServerStatus serverStatus = getNextAvailableServer();

            if(serverStatus == null) {

                System.out.println("All servers busy, but max iterations not reached");

            } else {

                System.out.println("Status: " + serverStatus);

                int availableThreads = serverStatus.getAvailableThreads();

                String name = serverStatus.getServerInfo().getServerName();

                serverStatus.setAvailableThreads(availableThreads--);

                // Add the serverStatus back into the heap
                status.add(serverStatus);

                Print.out("Sending load to " + name);

                String response = client.communicate(serverStatus.getServerInfo(), msg);

                // Reheapify
                status.remove(serverStatus);
                serverStatus.setAvailableThreads(availableThreads++);
                status.add(serverStatus);

                return response;
            }
        }

        Print.out("All servers are busy.");

        // If all servers are currently busy, restart the process.
        return Client.EXCEPTION + " All servers busy. Please try again later.";
    }


    private synchronized ServerStatus getNextAvailableServer() {
        if (status.peek().getAvailableThreads() > 0) return status.poll();
        return null;
    }

//            for(int i = 0; i < status.size(); i++) {
//                ServerStatus serverStatus = status.get(i);
//
//
//                Print.out("In " + type() + " start: ");
//                Print.out("Looking at: " + name);
//                Print.out("ServerThreads: " + availableThreads);
//                Print.out("Message from client: " + msg);
//
//                if(availableThreads > 0) {
//                    serverStatus.setAvailableThreads(availableThreads--);
//
//                    Print.out("Sending load to " + name);
//
//                    String response = client.communicate(serverStatus.getServerInfo(), msg);
//
//                    serverStatus.setAvailableThreads(availableThreads++);
//
//
//                    return response;
//                }
//            }


//    private synchronized void prioritizeAvailable(int index) {
//        if(index == 0) return;
//
//        ServerStatus serverStatus = status.get(index);
//
//        for(int i = index - 1; i >= 0; i--) {
//            if(serverStatus.compareTo(status.get(i)) <= 0) {
//                serverStatus = status.remove(index);
//                status.add(i, serverStatus);
//                break;
//            }
//        }
//    }
//
//    private synchronized void prioritizeUnavailable(int index) {
//        if(index == status.size() - 1) return;
//
//        ServerStatus serverStatus = status.get(index);
//
//        for(int i = index + 1; i < status.size(); i++) {
//            if(serverStatus.compareTo(status.get(i)) >= 0) {
//                serverStatus = status.remove(index);
//                status.add(i, serverStatus);
//                break;
//            }
//        }
//    }

    public String type() {
        return "Load Balancer";
    }

//    public static void main(String[] args) {
//
//        // List of servers
//        String serverAddressPrefix = ".cs.hbg.psu.edu";
//        List<String> serversAddresses = new ArrayList<>(Arrays.asList("ada", "dijkstra", "noyce",
//                                                                      "nygaard", "euclid", "euler",
//                                                                      "gauss", "riemann", "babbage"));
//        List<Integer> serverPorts = new ArrayList<>(Arrays.asList(6150, 6151, 6152,
//                                                                  6153, 6154, 6155,
//                                                                  6156, 6157, 6158));
//
//        List<ServerInfo> servers = new ArrayList<>();
//
//        ListIterator<String> addrIter;
//        ListIterator<Integer> portIter;
//
//
//        for (addrIter = serversAddresses.listIterator(), portIter = serverPorts.listIterator();
//             addrIter.hasNext() && portIter.hasNext();) {
//            servers.add(new ServerInfo(addrIter.next() + serverAddressPrefix, portIter.next()));
//        }
//
//        LoadBalancer lb = new LoadBalancer(servers, new Client());
//
//        lb.status.get(5).setAvailableThreads(6);
//        System.out.println("before prioritizing " + lb.status.get(5).getServerInfo().toString());
//        System.out.println(lb.status.toString());
//        lb.prioritizeAvailable(5);
//        System.out.println("after prioritizing " + lb.status.get(5).getServerInfo().toString());
//        System.out.println(lb.status.toString());
//
//        System.out.println("before prioritizing " + lb.status.get(3).getServerInfo().toString());
//        System.out.println(lb.status.toString());
//        lb.prioritizeUnavailable(3);
//        System.out.println("after prioritizing " + lb.status.get(3).getServerInfo().toString());
//        System.out.println(lb.status.toString());
//
//    }
}
