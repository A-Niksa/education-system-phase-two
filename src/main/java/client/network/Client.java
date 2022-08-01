package client.network;

import client.gui.MainFrame;
import client.controller.ClientController;
import shareables.network.requests.Request;
import shareables.network.responses.Response;

public class Client {
    private int id;
    private int port;
    private ServerController serverController;

    public Client(int id, int port) {
        this.id = id;
        this.port = port;
    }

    public void start() {
        serverController = new ServerController(port);
        serverController.attemptConnectionToServer();
        new MainFrame(new ClientController(this));
    }

    public Response sendAndListen(Request request) {
        return serverController.sendAndListen(request);
    }

    public int getId() {
        return id;
    }
}
