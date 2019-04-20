public class Config {
    public static int NUM_SERVERS = 3;
    public static int SERVER_THREAD_LIMIT = 2;
    public static int LB_THREAD_LIMIT = NUM_SERVERS * SERVER_THREAD_LIMIT;
    public static int CL_THREAD_LIMIT = LB_THREAD_LIMIT;

    public static String[] servers = { "grace", "dijkstra", "noyce"};
//                             "nygaard", "euclid", "euler".
//                             "gauss", "riemann", "babbage" };

    public static Integer[] listenForClientPorts = {6151, 6152, 6153};
//                       6154, 6155, 6156,
//                       6157, 6158, 61598};
}