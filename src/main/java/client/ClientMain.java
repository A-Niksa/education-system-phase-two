package client;

import client.network.Client;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

public class ClientMain {
    private static int clientId = 0;

    public static void main(String[] args) {
        MasterLogger.clientInfo(clientId++, "Start of program","psvm", ClientMain.class);
        new Client(clientId, ConfigManager.getPort()).start();
    }
}
