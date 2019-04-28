/**
 * Created by Aditya on 1/17/2019.
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

class Server implements Runnable {

    private volatile Socket clientSocket;
    private Application app;
    private int listenForClientPort;
    private String machineName;

    public int THREAD_LIMIT;

    public Server(int listenForClientPort, int threads, Application app) {
        this(listenForClientPort, threads, new Socket(), app);
    }


    // Private constructor to keep the public one clean.
    private Server(int listenForClientPort, int threads, Socket socket, Application app) {

        this.listenForClientPort = listenForClientPort;
        clientSocket = socket;
        THREAD_LIMIT = threads;
        this.app = app;

        try {
            machineName = java.net.InetAddress.getLocalHost().getCanonicalHostName().split("\\.")[0];
        } catch (Exception e) {
            System.err.println("In Server constructor: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            // Read in the number sent by the client
            PrintStream printStream = new PrintStream(clientSocket.getOutputStream());
            Scanner scanner = new Scanner(clientSocket.getInputStream());

            int data = scanner.nextInt();

            Print.out("Number from client: " + data);

            // Blocking call to app.start()
            String result = app.start(data);

            Print.out(app.type() + " returned: " + result);

            // Send back the result to the client
            printStream.println(machineName + ":" + result);

            scanner.close();
            printStream.close();

        } catch (IOException e) {
            System.err.println("In Server run: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("In Server finally: " + e.getMessage());
            }
        }
    }

    // Start method starts the server up and
    //  passes the application to a new thread for every request received.
    public void start() {
        try(ServerSocket serverSocket = new ServerSocket(listenForClientPort)) {

            Socket socket;

            ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_LIMIT);

            while(true) {
                if(pool.getActiveCount() < THREAD_LIMIT) {
                    System.out.println("Waiting for the client to connect...");

                    socket = serverSocket.accept();

                    System.out.println("Client connected");

                    pool.execute(new Server(listenForClientPort, THREAD_LIMIT, socket, app));

                    System.out.println("Threads in use in " + machineName + ": " + pool.getActiveCount());
                }
            }
        } catch (IOException e) {
            System.err.println("In Server start: " + e.getMessage());
        }
    }
}
