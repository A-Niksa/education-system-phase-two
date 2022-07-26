package server.network;

import server.database.management.DatabaseManager;
import server.network.authentication.AuthTokenGenerator;
import server.network.clienthandling.ClientHandler;
import server.network.clienthandling.RequestMapper;
import shareables.network.requests.Request;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private List<ClientHandler> activeClientHandlers; // TODO: only includes active clientHandlers?
    private int currentClientHandlerId;
    private int port;
    private boolean isActive;
    private final DatabaseManager databaseManager; // TODO: synchronizing db if necessary?
    private AuthTokenGenerator authTokenGenerator;
    private RequestMapper requestMapper;

    public Server(int port) {
        this.port = port;
        activeClientHandlers = new ArrayList<>();
        currentClientHandlerId = 0;
        databaseManager = new DatabaseManager();
        authTokenGenerator = new AuthTokenGenerator();
        requestMapper = new RequestMapper(databaseManager);
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            isActive = true;
            awaitConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void awaitConnection() {
        while (isActive) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(currentClientHandlerId,
                        authTokenGenerator.generateAuthToken(), socket, this);
                currentClientHandlerId++;
                activeClientHandlers.add(clientHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleRequest(int clientHandlerId, Request request) {
        ClientHandler clientHandler = getClientHandler(clientHandlerId);
        requestMapper.mapRequestToHandlerMethod(clientHandler, request);
    }

    private ClientHandler getClientHandler(int clientHandlerId) {
        return activeClientHandlers.stream().filter(e -> e.getId() == clientHandlerId).findAny().orElse(null);
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public AuthTokenGenerator getAuthTokenGenerator() {
        return authTokenGenerator;
    }

    public void setAuthTokenGenerator(AuthTokenGenerator authTokenGenerator) {
        this.authTokenGenerator = authTokenGenerator;
    }

    public List<ClientHandler> getActiveClientHandlers() {
        return activeClientHandlers;
    }

    public void setActiveClientHandlers(List<ClientHandler> activeClientHandlers) {
        this.activeClientHandlers = activeClientHandlers;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
