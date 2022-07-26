package server.network.clienthandling;

import server.database.management.DatabaseManager;
import server.network.clienthandling.ClientHandler;
import server.network.clienthandling.RequestHandler;
import shareables.network.requests.Request;
import shareables.network.requests.RequestIdentifier;

public class RequestMapper {
    private DatabaseManager databaseManager;
    private RequestHandler requestHandler;

    public RequestMapper(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        requestHandler = new RequestHandler(databaseManager);
    }

    public void mapRequestToHandlerMethod(ClientHandler clientHandler, Request request) {
        RequestIdentifier requestIdentifier = request.getRequestIdentifier();
        switch (requestIdentifier) {
            case LOGIN:
                requestHandler.login(clientHandler, request);
                break;
            case NEW_PASSWORD:
                requestHandler.changePassword(clientHandler, request);
                break;
        }
    }
}
