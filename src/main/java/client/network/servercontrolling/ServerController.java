package client.network.servercontrolling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import shareables.network.requests.Request;
import shareables.network.responses.Response;
import shareables.utils.objectmapping.ObjectMapperUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ServerController {
    private String authToken;
    private int port;
    private Scanner in;
    private PrintStream out;
    private ObjectMapper objectMapper;

    public ServerController(int port) {
        this.port = port;
        objectMapper = ObjectMapperUtils.getNetworkObjectMapper();
    }

    public void attemptConnectionToServer() {
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), port); // TODO: diff between this ip and the config ip?
            in = new Scanner(socket.getInputStream());
            out = new PrintStream(socket.getOutputStream());
            receiveAuthTokenFromServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveAuthTokenFromServer() {
        authToken = in.nextLine();
    }

    public Response sendAndListen(Request request) {
        send(request);
        return listen();
    }

    private Response listen() {
        try {
            String responseString = in.nextLine();
            return objectMapper.readValue(responseString, Response.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void send(Request request) {
        addAuthTokenToRequest(request);
        try {
            String requestString = objectMapper.writeValueAsString(request);
            printlnAndFlush(requestString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void addAuthTokenToRequest(Request request) {
        request.setAuthToken(authToken);
    }

    private void printlnAndFlush(String string) {
        out.println(string);
        out.flush();
    }
}
