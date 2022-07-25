package server.network.clienthandling;

import server.database.management.DatabaseManager;
import server.network.clienthandling.ResponseHandler;

public class RequestHandler {
    private DatabaseManager databaseManager;
    private ResponseHandler responseHandler;

    public RequestHandler(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        responseHandler = new ResponseHandler();
    }
}
