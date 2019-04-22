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

        String sunLabLoadBalancerAddress = "ada.hbg.psu.edu";
        final int loadBalancerPort = 6150;
        final int MAX_RANDOM = 15;
        final int MIN_NUM = 10;

        Random r = new Random();

        // Initialize the ThreadPoolExecutor
        ServerInfo loadBalancerInfo = new ServerInfo(sunLabLoadBalancerAddress, loadBalancerPort);
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(Config.CL_THREAD_LIMIT);


        // Send requests to the load balancer until the thread limit and then store
        // a few requests in the queue.
        try {
            while(true) {
                if(pool.getActiveCount() < Config.CL_THREAD_LIMIT + Config.CL_THREAD_LIMIT / 2) {
                    pool.execute(() ->
                    {
                        String[] response = new Client()
                                .communicate(loadBalancerInfo, r.nextInt(MAX_RANDOM) + MIN_NUM)
                                .split(":");

                        Print.out(response[1] + " sent: " + response[2]);
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("In ClientMain: " + e.getMessage());
        }
    }
}
