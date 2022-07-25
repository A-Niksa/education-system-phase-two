package client;

import client.network.Client;
import shareables.utils.config.ConfigManager;

public class ClientMain {
    public static void main(String[] args) {
        new Client(ConfigManager.getPort()).start();
    }
}
