package client.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import shareables.network.requests.Request;
import shareables.network.requests.RequestIdentifier;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;
import shareables.utils.objectmapping.ObjectMapperUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerController {
    // TODO: should be removed ->
    public static int SO_TIMEOUT;
    static {
        SO_TIMEOUT = ConfigManager.getInt(ConfigFileIdentifier.NETWORK, "soTimeout");
    }

    private String authToken;
    private int port;
    private Scanner in;
    private PrintStream out;
    private ObjectMapper objectMapper;
    private int id;
    private Client client;

    public ServerController(int port, int id, Client client) {
        this.port = port;
        this.id = id;
        this.client = client;
        objectMapper = ObjectMapperUtils.getNetworkObjectMapper();
    }

    public void attemptConnectionToServer() {
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), port); // TODO: diff between this ip and the config ip?
            socket.setSoTimeout(SO_TIMEOUT);
            client.setOnline(true);
            in = new Scanner(socket.getInputStream());
            out = new PrintStream(socket.getOutputStream());
            receiveAuthTokenFromServer();
        } catch (ConnectException e) {
            goOffline("attemptConnectionToServer");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void goOffline(String methodName) {
        MasterLogger.clientFatal(id, "Cannot connect to the server", methodName,
                getClass());
        client.setOnline(false);
    }

    private void receiveAuthTokenFromServer() {
        if (in == null) {
            goOffline("receiveAuthTokenFromServer");
            return;
        }
        try {
            String responseString = in.nextLine();
            Response response = objectMapper.readValue(responseString, Response.class);
            authToken = response.getUnsolicitedMessage();
        } catch (NoSuchElementException e) { // TODO: is this the correct exception
            goOffline("receiveAuthTokenFromServer");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void pingServer() {
        Request pingRequest = new Request(RequestIdentifier.CONNECTION_PING);
        sendAndListen(pingRequest);
    }

    public Response sendAndListen(Request request) {
        send(request);
        return listen();
    }

    private Response listen() {
        if (in == null) {
            goOffline("listen");
            return null;
        }
        try {
            String responseString = in.nextLine();
            return objectMapper.readValue(responseString, Response.class);
        } catch (NoSuchElementException e) {
            MasterLogger.clientFatal(id, "Cannot connect to the server", "attemptConnectionToServer",
                    getClass());
            client.setOnline(false);
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
        if (out == null) {
            goOffline("printlnAndFlush");
        } else {
            out.println(string);
            out.flush();
        }
    }
}