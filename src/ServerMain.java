/**
 * Created by Aditya on 3/21/2019.
 */

import java.net.*;

public class ServerMain {

    public static final int NUM_SERVERS = 3;

    public static void main(String[] args) {

        String[] servers = { "grace", "dijkstra", "noyce"};
//                             "nygaard", "euclid", "euler".
//                             "gauss", "riemann", "babbage" };

        int[] listenForClientPorts = {6151, 6152, 6153};
//                       6154, 6155, 6156,
//                       6157, 6158, 61598};

        try {
            InetAddress addr = java.net.InetAddress.getLocalHost();
            String hostname = addr.getCanonicalHostName().split("\\.")[0];

            for(int i = 0; i < NUM_SERVERS; i++) {
                if(servers[i].equals(hostname)) {
                    new Server(listenForClientPorts[i], new NumberCruncher()).start();
                    break;
                }
            }
        } catch(Exception e) {
            System.err.println("In Main: " + e.getMessage());
        }
    }
}
