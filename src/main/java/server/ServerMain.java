package server;

import server.network.Server;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

public class ServerMain {
    public static void main(String[] args) {
        MasterLogger.serverInfo("Start of program", "psvm", ServerMain.class);
        new Server(ConfigManager.getPort()).start();
    }
}
