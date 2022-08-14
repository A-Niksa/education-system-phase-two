package client.network;

import client.gui.MainFrame;
import client.controller.ClientController;
import client.locallogic.localdatabase.management.LocalDatabaseManager;
import shareables.network.requests.Request;
import shareables.network.responses.Response;

public class Client {
    private int id;
    private int port;
    private ServerController serverController;
    private LocalDatabaseManager localDatabaseManager;
    private boolean isOnline;

    public Client(int id, int port) {
        this.id = id;
        this.port = port;
    }

    public void start() {
        startClientNetwork();
        new MainFrame(new ClientController(this));
    }

    public void startClientNetwork() {
        serverController = new ServerController(port, id, this);
        serverController.attemptConnectionToServer();
    }

    public void startLocalDatabaseManager(String currentUserId) {
        if (localDatabaseManager == null) createNewLocalDatabaseManager(currentUserId);
        else localDatabaseManager.setCurrentUserId(currentUserId);

        localDatabaseManager.loadDatabase();
    }

    private void createNewLocalDatabaseManager(String currentUserId) {
        localDatabaseManager = new LocalDatabaseManager(currentUserId);
    }

    public Response sendAndListen(Request request) {
        return serverController.sendAndListen(request);
    }

    public int getId() {
        return id;
    }

    public boolean isOnline() {
        serverController.pingServer();
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCurrentUserIdInDatabaseManager(String currentUserId) {
        localDatabaseManager.setCurrentUserId(currentUserId);
    }

    public LocalDatabaseManager getLocalDatabaseManager() {
        return localDatabaseManager;
    }

    public void setLocalDatabaseManager(LocalDatabaseManager localDatabaseManager) {
        this.localDatabaseManager = localDatabaseManager;
    }
}
