package client.controllers;

import client.network.Client;
import shareables.network.requests.Request;
import shareables.network.requests.RequestIdentifier;
import shareables.network.responses.Response;

public class ClientController {
    private Client client;
    private RequestGenerator requestGenerator;

    public ClientController(Client client) {
        this.client = client;
        requestGenerator = new RequestGenerator();
    }

    public Response login(String username, String password, String captcha) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.LOGIN,
                new StringObjectMap("username", username), new StringObjectMap("password", password),
                new StringObjectMap("captcha", captcha));
        return client.sendAndListen(request);
    }
}
