import java.io.IOException;
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

            printStream.close();
            scanner.close();

            return reply;
        } catch (IOException e) {

            Print.out("In communicate: " + e.getMessage());

            String out = "Load balancer down.\nGiving it a chance to get back online.\nGoing to sleep for 10 seconds...";

            Print.out(out);

            try {
                Thread.sleep(10*1000);
            } catch (InterruptedException i) {
                System.out.println("In communicate catch, can't sleep: " + i.getMessage());
            }

            return EXCEPTION;
        } finally {
            try {
                s.close();
            } catch (IOException e) {
                Print.out("In communicate-finally: " + e.getMessage());
            }
        }
    }
}
