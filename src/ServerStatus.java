/**
 * Created by Aditya on 4/14/2019.
 *
 * Class to store the status of a server.
 */

class ServerStatus implements Comparable {

    private int availableThreads;
    private ServerInfo serverInfo;

    public ServerStatus(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
        availableThreads = Config.SERVER_THREAD_LIMIT;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public synchronized void setAvailableThreads(int threads) {
        if(threads > Config.SERVER_THREAD_LIMIT) availableThreads = Config.SERVER_THREAD_LIMIT;
        else availableThreads = threads;
    }

    public synchronized int getAvailableThreads() {
        return availableThreads;
    }

    @Override
    public synchronized int compareTo(Object o) {
        if(o instanceof ServerStatus) {
            ServerStatus that = (ServerStatus) o;
            return Integer.compare(this.getAvailableThreads(), that.getAvailableThreads());
        }
        return -2;
    }

    @Override
    public synchronized boolean equals(Object o) {
        if(o instanceof ServerStatus) {
            ServerStatus that = (ServerStatus) o;
            return this.serverInfo.equals(that.serverInfo);
        }
        return false;
    }

    @Override
    public synchronized String toString() {
        return serverInfo.toString() + ", " + availableThreads;
    }
}
