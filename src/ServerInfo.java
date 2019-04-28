/**
 * Created by Aditya on 3/21/2019.
 *
 * Class to store a server's information.
 */

class ServerInfo {
    private String serverIP;
    private String serverName;
    private int port;

    public ServerInfo(String serverIP, int port) {
        this.serverIP = serverIP;
        this.serverName = serverIP.split("\\.")[0];
        this.port = port;
    }

    public String getServerIP() {
        return serverIP;
    }

    public String getServerName() {
        return serverIP.split("\\.")[0];
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return this.serverName + " " + this.port;
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
