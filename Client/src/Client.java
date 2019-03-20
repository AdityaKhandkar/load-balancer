import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Aditya on 1/17/2019.
 */

public class Client {

    public static void main(String[] args) {

        String loadBalancerAddress = "dijkstra.cs.hbg.psu.edu";
        final int PORT_NUM = 6149;

        InetSocketAddress hostAddress = new InetSocketAddress(loadBalancerAddress, PORT_NUM);
        Socket socket;

        try {
            // Make a new request with every loop.
            // Since its a new socket, its a new connection channel
            System.out.println("IN TRY");
            while(true) {
//                System.out.println("Y");
                socket = new Socket(loadBalancerAddress, PORT_NUM);
                //socket.connect(hostAddress);
                communicate(socket);
            }
        } catch (IOException e ) {
            System.err.println("Error in connecting to the load balancer");
        }
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
//            System.out.println("Enter a number:");
//            int num = new Scanner(System.in).nextInt();
            int num = 10;
            System.out.printf("Sending %d\n", num);
            PrintStream outStream = new PrintStream(s.getOutputStream());
            outStream.println(num);
            // Receive a reply from the load balancer
//            System.out.printf("The reply is: %d\n", new Scanner(s.getInputStream()).nextInt());
            Thread.sleep(100);
        } catch (Exception e) {}
        finally { // Always close the socket
            try {
                s.close();
            } catch (Exception e) {}
        }
    }
}
