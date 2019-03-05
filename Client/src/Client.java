import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Aditya on 1/17/2019.
 */

public class Client {

    public static void main(String[] args) {

        String loadBalancerAddress = "127.0.0.1";
        final int PORT_NUM = 1341;

        InetSocketAddress hostAddress = new InetSocketAddress(loadBalancerAddress, PORT_NUM);
        Socket socket;

        try {
            // Make a new request with every loop.
            // Since its a new socket, its a new connection channel
            while(true) {
                socket = new Socket();
                socket.connect(hostAddress);
                communicate(socket);
            }
        } catch (IOException e ) {
            System.err.println("Error in connecting to the load balancer");
        }
    }

    public static Socket connect(Socket s, InetSocketAddress hostAddr) throws IOException {
        s.connect(hostAddr);
        return s;
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.err.println("Thread was interrupted");
        }
    }

    public static void communicate(Socket s) {
        try {
            // Sending number to server
            System.out.println("Enter a number:");
            int num = new Scanner(System.in).nextInt();
            new PrintStream(s.getOutputStream()).println(num);
            System.out.println("Waiting for a reply...");
            // Receive a reply from the load balancer
            System.out.println(new Scanner(s.getInputStream()).nextInt());
        } catch (Exception e) {}
        finally { // Always close the socket
            try {
                s.close();
            } catch (Exception e) {}
        }
    }
}
