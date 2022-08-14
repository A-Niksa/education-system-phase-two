package client.locallogic.localdatabase.management;

import client.locallogic.localdatabase.datasets.LocalDataset;
import client.locallogic.localdatabase.datasets.LocalDatasetIdentifier;
import shareables.models.idgeneration.Identifiable;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class LocalDatabaseManager {
    private int id;
    private Map<LocalDatasetIdentifier, LocalDataset> identifierDatasetMap;
    private LocalDatabaseReader localDatabaseReader;
    private LocalDatabaseWriter localDatabaseWriter;

    public LocalDatabaseManager(int id) {
        this.id = id;
        createDirectoryIfDoesNotExist();
        initializeIdentifierDatasetMap();
        localDatabaseReader = new LocalDatabaseReader(identifierDatasetMap, id);
        localDatabaseWriter = new LocalDatabaseWriter(identifierDatasetMap, id);
    }

    private void createDirectoryIfDoesNotExist() {
        String localDatasetsPath = ConfigManager.getString(ConfigFileIdentifier.ADDRESSES, "localDatasetsFolderPath");
        File localDatasetsDirectory = new File(localDatasetsPath);
        String targetDirectoryPath = localDatasetsPath + id;
        if (!directoryExists(localDatasetsDirectory, targetDirectoryPath)) {
            new File(targetDirectoryPath).mkdir();
        }
    }

    private boolean directoryExists(File localDatasetsDirectory, String targetDirectoryPath) {
        for (File file : localDatasetsDirectory.listFiles()) {
            if (file.getPath().equals(targetDirectoryPath)) {
                return true;
            }
        }
        return false;
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
        MasterLogger.clientInfo(id, "Loaded database from resources", "loadDatabase", getClass());
    }

    public void saveDatabase() {
        localDatabaseWriter.saveDatabase();
        MasterLogger.clientInfo(id, "Saved database to resources", "saveDatabase", getClass());
    }

    public synchronized void save(LocalDatasetIdentifier localDatasetIdentifier, Identifiable identifiable) {
        getDataset(localDatasetIdentifier).save(identifiable);
        MasterLogger.clientInfo(id, "Saved identifiable (id: " + identifiable.getId() + ")", "save",
                getClass());
    }

    public synchronized void remove(LocalDatasetIdentifier localDatasetIdentifier, String identifiableId) {
        getDataset(localDatasetIdentifier).remove(identifiableId);
        MasterLogger.clientInfo(id, "Removed identifiable (id: " + identifiableId + ")", "remove",
                getClass());
    }

    public Identifiable get(LocalDatasetIdentifier localDatasetIdentifier, String identifiableId) {
        MasterLogger.clientInfo(id, "Fetched identifiable (id: " + identifiableId + ")", "get",
                getClass());
        return getDataset(localDatasetIdentifier).get(identifiableId);
    }

    /**
     * gets the identifiables list of a particular dataset
     */
    public List<Identifiable> getIdentifiables(LocalDatasetIdentifier localDatasetIdentifier) {
        MasterLogger.clientInfo(id, "Fetched identifiables list from " + localDatasetIdentifier,
                "getIdentifiables", getClass());
        return getDataset(localDatasetIdentifier).getIdentifiables();
    }

    /**
     * works under the condition that the id of the identifiable does not change
     */
    public synchronized void update(LocalDatasetIdentifier localDatasetIdentifier, Identifiable identifiable) {
        remove(localDatasetIdentifier, identifiable.getId());
        save(localDatasetIdentifier, identifiable);
        MasterLogger.clientInfo(id, "Updated identifiable (id: " + identifiable.getId() + ")", "update",
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