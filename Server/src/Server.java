/**
 * Created by Aditya on 1/17/2019.
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Server {
    public static void main(String[] args) throws IOException {

        // Accept the number sent by the client
        ServerSocket srvS = new ServerSocket(1342);
        Socket s = srvS.accept();
        Scanner sc = new Scanner(s.getInputStream());
        int inNum = sc.nextInt();

        // Double the number and send it back
        new PrintStream(s.getOutputStream()).println(inNum * 2);
    }
}
