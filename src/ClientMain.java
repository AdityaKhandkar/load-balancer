import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Aditya on 3/26/2019.
 */

public class ClientMain {
    public static void main(String[] args) {
        String localhost = "localhost";
        String loadBalancerAddress = localhost;
        final int loadBalancerPort = 6150;

        Random r = new Random();

        ServerInfo loadBalancerInfo = new ServerInfo(loadBalancerAddress, loadBalancerPort);

        ExecutorService pool = Executors.newFixedThreadPool(Server.THREAD_LIMIT + 5);

        while(true) {
            try {
                pool.execute(() ->
                        System.out.println(new Client().communicate(loadBalancerInfo, r.nextInt(20) + 10)));
            } catch (Exception e) {
                System.err.println("In ClientMain: " + e.getMessage());
            }
        }
    }
}
