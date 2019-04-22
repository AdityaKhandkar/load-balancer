import java.io.PrintStream;
import java.math.BigInteger;
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
            PrintStream printStream = new PrintStream(s.getOutputStream());
            Scanner scanner = new Scanner(s.getInputStream());
            // Sending message to server
            Print.out(message, serverInfo.getPort());

            printStream.println(message);

            // Receive a reply from the server
            String reply = scanner.nextLine();

//            Print.out("Reply from LB: " + reply);

            printStream.close();
            scanner.close();

            return reply;
        } catch (Exception e) {
            Print.out("In communicate: " + e.getMessage());
        } finally { // Always close the socket
            try {
                s.close();
            } catch (Exception e) {
                Print.out("In communicate-finally: " + e.getMessage());
            }
        }
        return EXCEPTION;
    }
}
