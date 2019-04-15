/**
 * Created by Aditya on 1/17/2019.
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

class Server implements Runnable {

    private volatile Socket clientSocket;
    private Application app;
    private int port;

    public static final int THREAD_LIMIT = 10;

    public Server(int port, Application app) {
        this(port, new Socket(), app);
    }

    private Server(int port, Socket socket, Application app) {
        this.port = port;
        clientSocket = socket;
        this.app = app;
    }

    @Override
    public void run() {
        try {
            // Read in the number sent by the client
            long data = new Scanner(clientSocket.getInputStream()).nextInt();
            System.out.println("Number from client: " + data);
            String result = app.start(data);
            System.out.println(app.type() + " says " + result);
            // Send back the result to the client
            new PrintStream(clientSocket.getOutputStream()).println(result);
        } catch (Exception e) {
            System.err.println("In start: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("In finally: " + e.getMessage());
            }
            System.out.println("Connection with client closed.");
        }
    }

    public void start() {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running");
            Socket socket;
            ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_LIMIT);

            while(true) {
                try {
                    System.out.println("Waiting for the client to connect...");
                    socket = serverSocket.accept();
                    pool.execute(new Server(port, socket, app));
                    System.out.println("Threads which completed their tasks: " + pool.getCompletedTaskCount());
                    System.out.println("Client connected");
                    Thread.sleep(100);
                    System.out.println("Active threads: " + pool.getActiveCount());
                } catch(Exception e) {
                    System.err.println("In while: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("In start: " + e.getMessage());
        }
    }
}
