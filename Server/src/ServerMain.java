import java.net.Socket;

/**
 * Created by Aditya on 3/21/2019.
 */
public class ServerMain {
    public static void main(String[] args) {
        int port = 6150;
        try {
            new Server(port, new NumberCruncher()).start();
        } catch (Exception e) {
            System.err.println("In Main: " + e.getMessage());
        }
    }
}
