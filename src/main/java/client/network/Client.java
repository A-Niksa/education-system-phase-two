package client.network;

import client.network.servercontrolling.ServerController;
import shareables.network.requests.Request;
import shareables.network.responses.Response;

public class Client {
    private int port;
    private ServerController serverController;

    public Client(int port) {
        this.port = port;
    }

    public void start() {
        serverController = new ServerController(port);
        serverController.attemptConnectionToServer();
    }

    public Response sendAndListen(Request request) {
        return serverController.sendAndListen(request);
    }
}
