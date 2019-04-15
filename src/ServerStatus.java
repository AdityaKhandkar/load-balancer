import org.jetbrains.annotations.NotNull;

/**
 * Created by Aditya on 4/14/2019.
 */

class ServerStatus implements Comparable {

    private final int MAX_THREADS = 10;
    private int availableThreads;
    private ServerInfo serverInfo;

    public ServerStatus(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
        availableThreads = MAX_THREADS;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public synchronized void setAvailableThreads(int t) {
        if(t > 10) availableThreads = MAX_THREADS;
        else availableThreads = t;
    }

    public synchronized int getAvailableThreads() {
        return availableThreads;
    }

    @Override
    public synchronized int compareTo(@NotNull Object o) {
        if(o instanceof ServerStatus) {
            ServerStatus that = (ServerStatus) o;
            return Integer.compare(this.getAvailableThreads(), that.getAvailableThreads());
        }
        return -2;
    }

    @Override
    public synchronized String toString() {
        return serverInfo.toString() + ", " + availableThreads;
    }
}
