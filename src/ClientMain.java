import java.util.Random;
import java.util.concurrent.Executors;

/**
 * Created by Aditya on 3/26/2019.
 */

public class ClientMain {
    public static void main(String[] args) {
        String sunlabLoadBalancer = "dijkstra.cs.hbg.psu.edu";
        String localhost = "localhost";
        String loadBalancerAddress = localhost;
        final int PORT_NUM = 6149;

        ServerInfo loadBalancerInfo = new ServerInfo(loadBalancerAddress, PORT_NUM);

        Executors.newFixedThreadPool(1).execute(() ->
                System.out.println(new Client().communicate(loadBalancerInfo, new Random().nextInt())));
    }
}
