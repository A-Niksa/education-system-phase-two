package client.locallogic.localdatabase.management;

import client.locallogic.localdatabase.datamodels.QueuedMessage;
import client.locallogic.localdatabase.datasets.LocalDataset;
import client.locallogic.localdatabase.datasets.LocalDatasetIdentifier;
import shareables.models.idgeneration.Identifiable;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalDatabaseManager {
    private String currentUserId;
    private Map<LocalDatasetIdentifier, LocalDataset> identifierDatasetMap;
    private LocalDatabaseReader localDatabaseReader;
    private LocalDatabaseWriter localDatabaseWriter;

    public LocalDatabaseManager(String currentUserId) {
        this.currentUserId = currentUserId;
        initializeDatabaseManagerTools();
    }

    private void initializeDatabaseManagerTools() {
        createDirectoryIfDoesNotExist();
        initializeIdentifierDatasetMap();
        localDatabaseReader = new LocalDatabaseReader(identifierDatasetMap, currentUserId);
        localDatabaseWriter = new LocalDatabaseWriter(identifierDatasetMap, currentUserId);
    }

    private void createDirectoryIfDoesNotExist() {
        String localDatasetsPath = ConfigManager.getString(ConfigFileIdentifier.ADDRESSES, "localDatasetsFolderPath");
        File localDatasetsDirectory = new File(localDatasetsPath);
        String targetDirectoryPath = localDatasetsPath + currentUserId;
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
    }

    public void saveDatabase() {
        localDatabaseWriter.saveDatabase();
    }

    public synchronized void save(LocalDatasetIdentifier localDatasetIdentifier, Identifiable identifiable) {
        getDataset(localDatasetIdentifier).save(identifiable);
    }

    public synchronized void remove(LocalDatasetIdentifier localDatasetIdentifier, String identifiableId) {
        getDataset(localDatasetIdentifier).remove(identifiableId);
    }

    public Identifiable get(LocalDatasetIdentifier localDatasetIdentifier, String identifiableId) {
        return getDataset(localDatasetIdentifier).get(identifiableId);
    }

    /**
     * gets the identifiables list of a particular dataset
     */
    public List<Identifiable> getIdentifiables(LocalDatasetIdentifier localDatasetIdentifier) {
        return getDataset(localDatasetIdentifier).getIdentifiables();
    }

    /**
     * works under the condition that the id of the identifiable does not change
     */
    public synchronized void update(LocalDatasetIdentifier localDatasetIdentifier, Identifiable identifiable) {
        remove(localDatasetIdentifier, identifiable.getId());
        save(localDatasetIdentifier, identifiable);
    }

    public synchronized List<QueuedMessage> getAllQueuedMessages() {
        return identifierDatasetMap.get(LocalDatasetIdentifier.QUEUED_MESSAGES)
                .getIdentifiables()
                .stream()
                .map(identifiable -> (QueuedMessage) identifiable)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private LocalDataset getDataset(LocalDatasetIdentifier localDatasetIdentifier) {
        return identifierDatasetMap.get(localDatasetIdentifier);
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
        initializeDatabaseManagerTools();
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public LocalDatabaseWriter getDatabaseWriter() {
        return localDatabaseWriter;
    }
}