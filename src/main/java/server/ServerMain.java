package server;

import server.network.Server;
import shareables.utils.config.ConfigManager;

public class ServerMain {
    public static void main(String[] args) {
        new Server(ConfigManager.getPort()).start();
    }
}
