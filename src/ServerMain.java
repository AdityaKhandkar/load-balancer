    /**
 * Created by Aditya on 3/21/2019.
 */

import java.net.*;

public class ServerMain {

    public static void main(String[] args) {

        try {
            InetAddress addr = java.net.InetAddress.getLocalHost();
            String hostname = addr.getCanonicalHostName().split("\\.")[0];

            for(int i = 0; i < Config.NUM_SERVERS; i++) {
                if(Config.servers[i].equals(hostname)) {
                    new Server(Config.listenForClientPorts[i], Config.SERVER_THREAD_LIMIT, new NumberCruncher()).start();
                    break;
                }
            }
        } catch(Exception e) {
            System.err.println("In Main: " + e.getMessage());
        }
    }
}
