package client;

import client.network.Client;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

public class ClientMain {
    public static void main(String[] args) {
        MasterLogger.info("Start of program (Client)", "psvm", ClientMain.class);
        new Client(ConfigManager.getPort()).start();
    }
}
