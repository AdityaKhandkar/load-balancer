/**
 * Created by Aditya on 3/21/2019.
 */
class ServerInfo {
    private String serverIP;
    private int port;

    public ServerInfo(String serverIP, int port) {
        this.serverIP = serverIP;
        this.port = port;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServerIP() {
        return serverIP;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return this.serverIP + " " + this.port;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ServerInfo) {
            ServerInfo that = (ServerInfo) obj;
            return this.serverIP.equals(that.serverIP) &&
                    this.port == that.port;
        }
        return false;
    }
}
