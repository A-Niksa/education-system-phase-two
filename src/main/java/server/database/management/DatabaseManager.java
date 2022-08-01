package server.database.management;

import server.database.datasets.Dataset;
import server.database.datasets.DatasetIdentifier;
import shareables.models.idgeneration.Identifiable;
import shareables.utils.logging.MasterLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DatabaseManager {
    private Map<DatasetIdentifier, Dataset> identifierDatasetMap;
    private DatabaseReader databaseReader;
    private DatabaseWriter databaseWriter;

    public DatabaseManager() {
        initializeIdentifierDatasetMap();
        databaseReader = new DatabaseReader(identifierDatasetMap);
        databaseWriter = new DatabaseWriter(identifierDatasetMap);
    }

    private void initializeIdentifierDatasetMap() {
        identifierDatasetMap = new HashMap<>();
        Stream.of(DatasetIdentifier.values()).forEach(e -> {
            Dataset dataset = new Dataset();
            identifierDatasetMap.put(e, dataset);
        });
    }

    public void loadDatabase() {
        databaseReader.loadDatabase();
        MasterLogger.serverInfo("Loaded database from resources", "loadDatabase", getClass());
    }

    public void saveDatabase() {
        databaseWriter.saveDatabase();
        MasterLogger.serverInfo("Saved database to resources", "saveDatabase", getClass());
    }

    public void save(DatasetIdentifier datasetIdentifier, Identifiable identifiable) {
        getDataset(datasetIdentifier).save(identifiable);
        MasterLogger.serverInfo("Saved identifiable (id: " + identifiable.getId() + ")", "save",
                getClass());
    }

    public void remove(DatasetIdentifier datasetIdentifier, String identifiableId) {
        getDataset(datasetIdentifier).remove(identifiableId);
        MasterLogger.serverInfo("Removed identifiable (id: " + identifiableId + ")", "remove",
                getClass());
    }

    public Identifiable get(DatasetIdentifier datasetIdentifier, String identifiableId) {
        MasterLogger.serverInfo("Fetched identifiable (id: " + identifiableId + ")", "get",
                getClass());
        return getDataset(datasetIdentifier).get(identifiableId);
    }

    /**
     * gets the identifiables list of a particular dataset
     */
    public List<Identifiable> getIdentifiables(DatasetIdentifier datasetIdentifier) {
        MasterLogger.serverInfo("Fetched identifiables list from " + datasetIdentifier,
                "getIdentifiables", getClass());
        return getDataset(datasetIdentifier).getIdentifiables();
    }

    /**
     * works under the condition that the id of the identifiable does not change
     */
    public void update(DatasetIdentifier datasetIdentifier, Identifiable identifiable) {
        remove(datasetIdentifier, identifiable.getId());
        save(datasetIdentifier, identifiable);
        MasterLogger.serverInfo("Updated identifiable (id: " + identifiable.getId() + ")", "update",
                getClass());
    }

    private Dataset getDataset(DatasetIdentifier datasetIdentifier) {
        return identifierDatasetMap.get(datasetIdentifier);
    }
}