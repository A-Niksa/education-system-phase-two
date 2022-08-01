package server.network;

import server.database.management.DatabaseManager;

public class ServerShutdownHook implements Runnable {
    private DatabaseManager databaseManager;

    public ServerShutdownHook(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public void run() {
        databaseManager.saveDatabase();
    }
}
