package server.network.clienthandling.logicutils.messaging.contactfetching;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;

import java.util.List;

public class ContactFetchingUtils {
    public static boolean contactIdsExist(DatabaseManager databaseManager, List<String> contactIds) {
        return contactIds.stream()
                .allMatch(id -> IdentifiableFetchingUtils.getUser(databaseManager, id) != null);
    }
}
