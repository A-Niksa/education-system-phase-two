package server.network;

import server.database.management.DatabaseManager;
import server.network.authentication.AuthTokenGenerator;
import server.network.clienthandling.ClientHandler;
import server.network.clienthandling.RequestMapper;
import shareables.network.requests.Request;
import shareables.utils.config.ConfigIdSupplier;
import shareables.utils.logging.MasterLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private List<ClientHandler> clientHandlers;
    private int currentClientHandlerId;
    private int port;
    private boolean isActive;
    private final DatabaseManager databaseManager;
    private AuthTokenGenerator authTokenGenerator;
    private RequestMapper requestMapper;

    public Server(int port) {
        this.port = port;
        clientHandlers = new ArrayList<>();
        currentClientHandlerId = ConfigIdSupplier.getCurrentClientId();
        authTokenGenerator = new AuthTokenGenerator();
        databaseManager = new DatabaseManager();
        requestMapper = new RequestMapper(databaseManager);
        setShutdownHook(databaseManager);
        loadDatabase();
    }

    private void loadDatabase() {
        databaseManager.loadDatabase();
    }

    private void setShutdownHook(DatabaseManager databaseManager) {
        Runtime.getRuntime().addShutdownHook(new Thread(new ServerShutdownHook(databaseManager)));
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
                MasterLogger.serverInfo("Connection established with new client (id: " + currentClientHandlerId
                        + ")", "awaitConnection", Server.class);
                ClientHandler clientHandler = new ClientHandler(currentClientHandlerId,
                        authTokenGenerator.generateAuthToken(), socket, this);
                currentClientHandlerId++;
                clientHandlers.add(clientHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleRequest(ClientHandler clientHandler, Request request) {
        requestMapper.mapRequestToHandlerMethod(clientHandler, request);
    }

    private ClientHandler getClientHandler(int clientHandlerId) {
        return clientHandlers.stream().filter(e -> e.getId() == clientHandlerId).findAny().orElse(null);
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

    public List<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public void setClientHandlers(List<ClientHandler> clientHandlers) {
        this.clientHandlers = clientHandlers;
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
