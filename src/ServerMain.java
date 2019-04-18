/**
 * Created by Aditya on 3/21/2019.
 */

import java.net.*;

public class ServerMain {
    public static void main(String[] args) {

        final int NUM_SERVERS = 9;

        String[] servers = { "grace", "dijkstra", "noyce",
                             "nygaard", "euclid", "euler",
                             "gauss", "riemann", "babbage" };

        int[] ports = {6151, 6152, 6153,
                       6154, 6155, 6156,
                       6157, 6158, 61598};

        try {
            InetAddress addr = java.net.InetAddress.getLocalHost();
//            System.out.println(addr);
            String hostname = addr.getCanonicalHostName().split("\\.")[0];
            System.out.println("Hostname of system = " + hostname);

            for(int i = 0; i < NUM_SERVERS; i++) {
                if(servers[i].equals(hostname)) {
                    new Server(ports[i], new NumberCruncher()).start();
                    break;
                }
            }
        } catch(Exception e) {
            System.err.println("In Main: " + e.getMessage());
        }
    }
}
