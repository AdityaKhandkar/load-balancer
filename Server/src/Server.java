/**
 * Created by Aditya on 1/17/2019.
 */

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

public class Server implements Runnable {

    private Socket connectLoadBalancer;

    public Server(Socket socket) throws Exception {
//        this.serverSocket = serverSocket;
        this.connectLoadBalancer = socket;
    }

    @Override
    public void run() {
        synchronized(this) {
            try {
                while(connectLoadBalancer.isConnected()) {
                    // Read in the number sent by the load balancer
                    Scanner sc = new Scanner(connectLoadBalancer.getInputStream());
                    int inNum = sc.nextInt();
                    System.out.println("Number from load balancer: " + inNum);

                    // Double the number and send it back
                    new PrintStream(connectLoadBalancer.getOutputStream()).println(inNum * 2);
                }
            }

            catch (Exception e) {}

            finally {
                try {
                    closeSockets();
                } catch (IOException e) {}
                System.out.println("Connection with load balancer closed.");
            }
        }
    }

    private void closeSockets() throws IOException {
        connectLoadBalancer.close();
    }

    private void randomNumberCruncher() {
        int n = new Random().nextInt(6) + 5;
        try {
            Thread.sleep((new Random().nextInt(n) + 10) * 1000);
            findNthFib(n*15);
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

//    private void closeServerSocket() throws IOException {
//        serverSocket.close();
//    }

    public static void main(String[] args) throws Exception {

        final int PORTNUM = 1342;

        // Accept the connection
        ServerSocket balancerSocket = new ServerSocket(PORTNUM);
        Socket socket = balancerSocket.accept();
        Thread serverThread = new Thread(new Server(socket));
        serverThread.start();

        while(true) {
            if(socket.isClosed()) {
                System.out.println("Waiting for the load balancer to connect...");
                socket = balancerSocket.accept();
                new Thread(new Server(socket)).start();
                System.out.println("Load balancer connected");
            }
        }
    }
}
