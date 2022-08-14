package client.locallogic.localdatabase.management;

import client.locallogic.localdatabase.datasets.LocalDataset;
import client.locallogic.localdatabase.datasets.LocalDatasetIdentifier;
import shareables.models.idgeneration.Identifiable;
import shareables.utils.logging.MasterLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class LocalDatabaseManager {
    private Map<LocalDatasetIdentifier, LocalDataset> identifierDatasetMap;
    private LocalDatabaseReader localDatabaseReader;
    private LocalDatabaseWriter localDatabaseWriter;

    public LocalDatabaseManager() {
        initializeIdentifierDatasetMap();
        localDatabaseReader = new LocalDatabaseReader(identifierDatasetMap);
        localDatabaseWriter = new LocalDatabaseWriter(identifierDatasetMap);
    }

    private void initializeIdentifierDatasetMap() {
        identifierDatasetMap = new HashMap<>();
        Stream.of(LocalDatasetIdentifier.values()).forEach(e -> {
            LocalDataset localDataset = new LocalDataset();
            identifierDatasetMap.put(e, localDataset);
        });
    }

    public void loadDatabase() {
        localDatabaseReader.loadDatabase();
        MasterLogger.serverInfo("Loaded database from resources", "loadDatabase", getClass());
    }

    public void saveDatabase() {
        localDatabaseWriter.saveDatabase();
        MasterLogger.serverInfo("Saved database to resources", "saveDatabase", getClass());
    }

    public synchronized void save(LocalDatasetIdentifier localDatasetIdentifier, Identifiable identifiable) {
        getDataset(localDatasetIdentifier).save(identifiable);
        MasterLogger.serverInfo("Saved identifiable (id: " + identifiable.getId() + ")", "save",
                getClass());
    }

    public synchronized void remove(LocalDatasetIdentifier localDatasetIdentifier, String identifiableId) {
        getDataset(localDatasetIdentifier).remove(identifiableId);
        MasterLogger.serverInfo("Removed identifiable (id: " + identifiableId + ")", "remove",
                getClass());
    }

    public Identifiable get(LocalDatasetIdentifier localDatasetIdentifier, String identifiableId) {
        MasterLogger.serverInfo("Fetched identifiable (id: " + identifiableId + ")", "get",
                getClass());
        return getDataset(localDatasetIdentifier).get(identifiableId);
    }

    /**
     * gets the identifiables list of a particular dataset
     */
    public List<Identifiable> getIdentifiables(LocalDatasetIdentifier localDatasetIdentifier) {
        MasterLogger.serverInfo("Fetched identifiables list from " + localDatasetIdentifier,
                "getIdentifiables", getClass());
        return getDataset(localDatasetIdentifier).getIdentifiables();
    }

    /**
     * works under the condition that the id of the identifiable does not change
     */
    public synchronized void update(LocalDatasetIdentifier localDatasetIdentifier, Identifiable identifiable) {
        remove(localDatasetIdentifier, identifiable.getId());
        save(localDatasetIdentifier, identifiable);
        MasterLogger.serverInfo("Updated identifiable (id: " + identifiable.getId() + ")", "update",
                getClass());
    }

    private LocalDataset getDataset(LocalDatasetIdentifier localDatasetIdentifier) {
        return identifierDatasetMap.get(localDatasetIdentifier);
    }

    // TODO: should be removed ->
    public LocalDatabaseWriter getDatabaseWriter() {
        return localDatabaseWriter;
    }
}