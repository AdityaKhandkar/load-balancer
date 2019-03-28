import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Aditya on 1/17/2019.
 */

public class Client {

    public String communicate(ServerInfo serverInfo, int message) {
        Socket s = null;
        try {
            s = new Socket(serverInfo.getServerIP(), serverInfo.getPort());


            // Sending message to server
            System.out.printf("Sending %d\n", message);

            new PrintStream(s.getOutputStream()).println(message);

            // Receive a reply from the load balancer
            return new Scanner(s.getInputStream()).nextLine();

//            System.out.printf("The reply is: %d\n", new Scanner(s.getInputStream()).nextInt());
//            Thread.sleep(100);

        } catch (Exception e) {
            System.err.println("In communicate: " + e.getMessage());
        } finally { // Always close the socket
            try {
                s.close();
            } catch (Exception e) {
                System.err.println("In communicate-finally: " + e.getMessage());
            }
        }
        return "";
    }
}
