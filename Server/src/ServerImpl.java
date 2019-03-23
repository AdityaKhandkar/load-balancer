/**
 * Created by Aditya on 1/17/2019.
 */

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class ServerImpl implements Server, Runnable {

    private volatile Socket connectLoadBalancer;
    private Application app;

    public ServerImpl(Socket socket, Application app) throws Exception {
//        this.serverSocket = serverSocket;
        connectLoadBalancer = socket;
        this.app = app;
    }

    @Override
    public void run() {
        try {
            // Read in the number sent by the load balancer
            Scanner sc = new Scanner(connectLoadBalancer.getInputStream());
            int inNum = sc.nextInt();
            System.out.println("Number from load balancer: " + inNum);
            app.run(connectLoadBalancer.getInputStream());
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

    @Override
    public void run(int port, Application application) {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running");
            Socket socket;
            ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

            while(true) {
                try {
                    System.out.println("Waiting for the load balancer to connect...");
                    socket = serverSocket.accept();
                    pool.execute(new ServerImpl(socket, application));
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
