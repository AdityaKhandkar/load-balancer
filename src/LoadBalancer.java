

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Aditya on 2/14/2019.
 */
class LoadBalancer implements Application {

    private final int MAX_ITERATIONS = 100000;
    private volatile CopyOnWriteArrayList<ServerInfo> servers;
    private volatile PriorityBlockingQueue<ServerStatus> status;
    private int iterations;
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
    }

    // Load balancing algorithm
    @Override
    public String start(int msg) {
        iterations = 0;

        while (iterations++ < MAX_ITERATIONS) {

            ServerStatus serverStatus = getNextAvailableServer();

            if(serverStatus == null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.err.println("In LB Start, can't sleep: " + e.getMessage());
                }
                Print.out("Please wait. A server should be with you shortly.");
            } else {

                int availableThreads = serverStatus.getAvailableThreads();

                String name = serverStatus.getServerInfo().getServerName();

                serverStatus.setAvailableThreads(availableThreads - 1);

                // Add the serverStatus back into the heap
                status.add(serverStatus);

                Print.out("Sending load to " + name);

                String response = client.communicate(serverStatus.getServerInfo(), msg);

                // Reheapify
                status.remove(serverStatus);
                serverStatus.setAvailableThreads(availableThreads + 1);
                status.add(serverStatus);

                return response;
            }
        }

        Print.out("All servers are busy. Please try again later");

        // If all servers are currently busy, restart the process.
        return Client.EXCEPTION + " All servers busy. Please try again later.";
    }

    // Get the next available server
    private synchronized ServerStatus getNextAvailableServer() {
        if (status.peek().getAvailableThreads() > 0) return status.poll();
        return null;
    }

    public String type() {
        return "Load Balancer";
    }
}
