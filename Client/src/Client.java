import org.apache.xerces.impl.xs.SchemaNamespaceSupport;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Aditya on 1/17/2019.
 */
public class Client {
    public static void main(String[] args) throws IOException {

        // Sending number to server
        Socket socket = new Socket("127.0.0.1", 1342);
        System.out.println("Enter a number:");
        int num = new Scanner(System.in).nextInt();
        new PrintStream(socket.getOutputStream()).println(num);

        // Receive a reply from the server
        System.out.println(new Scanner(socket.getInputStream()).nextInt());
    }
}
