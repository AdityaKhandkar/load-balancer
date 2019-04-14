import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Aditya on 1/17/2019.
 */

class Client {

    public static String EXCEPTION = "EXCEPTION: ";

    public String communicate(ServerInfo serverInfo, long message) {
        Socket s = null;
        try {
            s = new Socket(serverInfo.getServerIP(), serverInfo.getPort());

            // Sending message to server
            System.out.printf("Sending %d, at port %d\n", message, serverInfo.getPort());

            new PrintStream(s.getOutputStream()).println(message);

            // Receive a reply from the server
            String reply = new Scanner(s.getInputStream()).nextLine();

            System.out.println("reply: " + reply);

            return reply;

        } catch (Exception e) {
            System.err.println("In communicate: " + e.getMessage());
        } finally { // Always close the socket
            try {
                s.close();
            } catch (Exception e) {
                System.err.println("In communicate-finally: " + e.getMessage());
            }
        }
        return EXCEPTION;
    }
}
