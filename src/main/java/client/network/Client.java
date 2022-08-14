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
        localDatabaseManager = new LocalDatabaseManager(id);
    }

    public void start() {
        startClientNetwork();
        new MainFrame(new ClientController(this));
    }

    public void startClientNetwork() {
        serverController = new ServerController(port, id, this);
        serverController.attemptConnectionToServer();
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
}
