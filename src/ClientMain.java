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

        ServerInfo loadBalancerInfo = new ServerInfo(sunLabLoadBalancerAddress, loadBalancerPort);
        System.out.println("lol");
        ExecutorService pool = Executors.newFixedThreadPool(Config.CL_THREAD_LIMIT);

        try {
            while(true) {
                pool.execute(() ->
                {
                    String[] response = new Client()
                            .communicate(loadBalancerInfo, r.nextInt(MAX_RANDOM) + MIN_NUM)
                            .split(":");

                    System.out.println("At Client side");
                    for(String s : response) {
                        System.out.println(s);
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("In ClientMain: " + e.getMessage());
        }
    }
}
