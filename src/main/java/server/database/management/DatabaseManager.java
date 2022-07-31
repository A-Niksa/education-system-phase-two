package server.database.management;

import server.database.datasets.Dataset;
import server.database.datasets.DatasetIdentifier;
import shareables.models.idgeneration.Identifiable;
import shareables.utils.logging.MasterLogger;

import java.util.HashMap;
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
        MasterLogger.serverInfo("loaded database", "loadDatabase", DatabaseManager.class);
    }

    public void saveDatabase() {
        databaseWriter.saveDatabase();
        MasterLogger.serverInfo("saved database to resources", "saveDatabase", DatabaseManager.class);
    }

    public void save(DatasetIdentifier datasetIdentifier, Identifiable identifiable) {
        getDataset(datasetIdentifier).save(identifiable);
    }

    public void remove(DatasetIdentifier datasetIdentifier, String identifiableId) {
        getDataset(datasetIdentifier).remove(identifiableId);
    }

    public Identifiable get(DatasetIdentifier datasetIdentifier, String identifiableId) {
        return getDataset(datasetIdentifier).get(identifiableId);
    }

    /**
     * works under the condition that the id of t does not change
     */
    public void update(DatasetIdentifier datasetIdentifier, Identifiable identifiable) {
        remove(datasetIdentifier, identifiable.getId());
        save(datasetIdentifier, identifiable);
    }

    private Dataset getDataset(DatasetIdentifier datasetIdentifier) {
        return identifierDatasetMap.get(datasetIdentifier);
    }
}