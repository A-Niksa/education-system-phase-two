package server.network.clienthandling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import server.network.Server;
import shareables.network.requests.Request;
import shareables.network.responses.Response;
import shareables.utils.logging.MasterLogger;
import shareables.utils.objectmapping.ObjectMapperUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientHandler {
    private int id;
    private String authToken;
    private Socket socket;
    private Server server;
    private ObjectMapper objectMapper;
    private Scanner in;
    private PrintStream out;

    public ClientHandler(int id, String authToken, Socket socket, Server server) {
        this.id = id;
        this.authToken = authToken;
        this.socket = socket;
        this.server = server;
        objectMapper = ObjectMapperUtils.getNetworkObjectMapper();
        initializeIOStreams();
        sendAuthTokenToClient();
        startRequestListenerThread();
    }

    public void respond(Response response) {
        try {
            String responseString = objectMapper.writeValueAsString(response);
            printlnAndFlush(responseString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void sendAuthTokenToClient() {
        Response response = new Response();
        response.setUnsolicitedMessage(authToken);
        try {
            printlnAndFlush(objectMapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void initializeIOStreams() {
        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startRequestListenerThread() {
        Thread thread = new Thread(() -> {
            while (true) {
                String requestString = null;
                try {
                    requestString = in.nextLine();
                } catch (NoSuchElementException e) {
                    MasterLogger.serverInfo("Client (id: " + id + ") disconnected", "startRequestListenerMethod",
                            getClass());
                    break;
                }
                try {
                    Request request = objectMapper.readValue(requestString, Request.class);
                    if (!request.getAuthToken().equals(authToken)) continue;
                    server.handleRequest(this, request);
                } catch (JsonProcessingException e) {
                    MasterLogger.serverInfo("Client (id: " + id + ") disconnected", "startRequestListenerMethod",
                            getClass());
                    break;
                }
            }
        });
        thread.start();
    }

    public int getId() {
        return id;
    }

    private void printlnAndFlush(String string) {
        out.println(string);
        out.flush();
    }
}