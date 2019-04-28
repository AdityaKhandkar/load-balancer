public class Config {

    // Variables used
    public static int NUM_SERVERS = 9;
    public static int SERVER_THREAD_LIMIT = 20;

    public static int LB_THREAD_LIMIT = NUM_SERVERS * SERVER_THREAD_LIMIT;
    public static int CL_THREAD_LIMIT = LB_THREAD_LIMIT;

    public static String[] servers = { "grace", "dijkstra", "noyce",
                                       "nygaard", "euclid", "shannon",
                                       "gauss", "riemann", "perlis" };

    public static Integer[] clientPorts = { 6151, 6152, 6153,
                                             6154, 6155, 6156,
                                             6157, 6158, 6159 };

    /**
     *  Possible clients include:
     *  dahl, peano, hilbert, erdos, eckert, zorn, fermat
     */
}
