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

            String result = app.start(data);

            Print.out(app.type() + " says result = " + result);

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
//            Print.out("Connection with client closed.");
        }
    }

    public void start() {
        try(ServerSocket serverSocket = new ServerSocket(listenForClientPort)) {
            System.out.println("On " + machineName);
            System.out.println("Listening on port: " + listenForClientPort);
            System.out.println("Application: " + app.type());

            Socket socket;

            ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_LIMIT);

            while(true) {
                if(pool.getActiveCount() < Config.SERVER_THREAD_LIMIT) {
                    System.out.println("Waiting for the client to connect...");

                    socket = serverSocket.accept();

                    System.out.println("Client connected");

                    pool.execute(new Server(listenForClientPort, THREAD_LIMIT, socket, app));

                    System.out.println("Threads which completed their tasks: " + pool.getCompletedTaskCount());

                    System.out.printf("Active threads in %s: %d \n", machineName, pool.getActiveCount());
                }
            }
        } catch (IOException e) {
            System.err.println("In Server start: " + e.getMessage());
        }
    }
}
