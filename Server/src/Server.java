/**
 * Created by Aditya on 1/17/2019.
 */

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server implements Runnable {

    private volatile Socket connectLoadBalancer;

    public Server(Socket socket) throws Exception {
//        this.serverSocket = serverSocket;
        this.connectLoadBalancer = socket;
    }

    @Override
    public void run() {
        try {
            // Read in the number sent by the load balancer
            Scanner sc = new Scanner(connectLoadBalancer.getInputStream());
            int inNum = sc.nextInt();
            System.out.println("Number from load balancer: " + inNum);
            randomNumberCruncher();
            // Double the number and send it back
            new PrintStream(connectLoadBalancer.getOutputStream()).println(inNum * 2);
        }

        catch (Exception e) {}

        finally {
            try {
                closeSockets();
            } catch (IOException e) {}
            System.out.println("Connection with load balancer closed.");
        }

    }

    private void closeSockets() throws IOException {
        connectLoadBalancer.close();
    }

    private void randomNumberCruncher() {
        Random r = new Random();
        int n = r.nextInt(6) + 5;
        try {
            Thread.sleep((r.nextInt(n) + 10) * 1000);
            findNthFib(n*10);
        } catch (InterruptedException e) {
            System.err.println("Sleep not working");
        }
    }

    private void findNthFib(int n) {
        int prev = 0, curr = 1;
        for(int i = 0; i <= n; i++) {
            int temp = prev + curr;
            prev = curr;
            curr = temp;
        }
    }

    public static void main(String[] args) {

        final int PORTNUM = 6150;

        // Accept the connection
        try(ServerSocket serverSocket = new ServerSocket(PORTNUM)) {
            Socket socket;
            ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

            while(true) {
                try {
                    System.out.println("Waiting for the load balancer to connect...");
                    socket = serverSocket.accept();
                    pool.execute(new Server(socket));
                    System.out.println("Threads which completed their tasks: " + pool.getCompletedTaskCount());
                    System.out.println("Load balancer connected");
                    Thread.sleep(100);
                    System.out.println("Active threads: " + pool.getActiveCount());
                }
                catch(Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
