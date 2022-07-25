package server.network.clienthandling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import server.network.Server;
import shareables.network.requests.Request;
import shareables.utils.objectmapping.ObjectMapperUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
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

    private void sendAuthTokenToClient() {
        printlnAndFlush(authToken);
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
                String requestString = in.nextLine();
                try {
                    Request request = objectMapper.readValue(requestString, Request.class);
                    if (!request.getAuthToken().equals(authToken)) continue;
                    server.handleRequest(id, request);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
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
