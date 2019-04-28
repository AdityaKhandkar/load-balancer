/**
 * Created by Aditya on 4/18/2019.
 */
class Print {

    // Concurrent print methods
    static synchronized void out(String msg) {
        System.out.println(msg);
    }

    static synchronized void out(long message, int port) {
        System.out.printf("Sending %d, at port %d\n", message, port);
    }
}
