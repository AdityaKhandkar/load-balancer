import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Aditya on 1/17/2019.
 */

public class Client {
    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("127.0.0.1", 1341);

        while(true) {
            // Sending number to server
            System.out.println("Enter a number:");
            int num = new Scanner(System.in).nextInt();
            System.out.println("Your num is: " + num);
            new PrintStream(socket.getOutputStream()).println(num);
            System.out.println("Waiting on reply...");
            // Receive a reply from the load balancer
            System.out.println(new Scanner(socket.getInputStream()).nextInt());
        }
    }
}
