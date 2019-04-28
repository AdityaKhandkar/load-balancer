import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Aditya on 3/26/2019.
 */

public class ClientMain {
    public static void main(String[] args) {

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
                if(pool.getActiveCount() < Config.CL_THREAD_LIMIT) {
                    pool.execute(() ->
                    {
                        int rand = r.nextInt(MAX_RANDOM) + MIN_NUM;

                        String response = new Client()
                                .communicate(loadBalancerInfo, rand);

                        // If you get a correct response, display it.
                        if(response.contains(":")) {
                            String[] responseArray = response.split(":");

                            String output = "The message is from: " + responseArray[1] + "\n"
                                    + "Fibonacci #" + rand + " = " + responseArray[2];

                            Print.out(output);
                        }

                        // If all servers are full, give them a chance to finish their work.
                        if(response.contains(Client.EXCEPTION)) {
                            Print.out(response);
                            Print.out("Going to sleep...");
                            try {
                                Thread.sleep(10*1000);
                            } catch (InterruptedException e) {
                                System.out.println("In ClientMain if-catch, can't sleep: " + e.getMessage());
                            }
                        }

                    });
                }
            }
        } catch (Exception e) {
            System.err.println("In ClientMain: " + e.getMessage());
        }
    }
}
