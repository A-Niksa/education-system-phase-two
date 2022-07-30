package server.database.management;

import server.database.datasets.Dataset;
import server.database.datasets.DatasetIdentifier;
import shareables.models.idgeneration.Identifiable;
import shareables.models.idgeneration.IdentifiableWithTime;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    private DatasetMapper datasetMapper;
    private Map<DatasetIdentifier, Dataset<? extends Identifiable>> identifierDatasetMap;

    public DatabaseManager() {
        identifierDatasetMap = new HashMap<>();
        hibernateConfigPath = getHibernateConfigPath();
        buildSessionFactory();
    }

    public <T extends Identifiable> void save(DatasetIdentifier datasetIdentifier, T t) {
        getDataset(datasetIdentifier)
                .save(datasetMapper.mapTToAppropriateIdentifiable(datasetIdentifier, t));
    }

    public void remove(DatasetIdentifier datasetIdentifier, String tId) {
        getDataset(datasetIdentifier).remove(tId);
    }

    public void get(DatasetIdentifier datasetIdentifier, String tId) {
        getDataset(datasetIdentifier).get(tId);
    }

    /**
     * works under the condition that the id of t does not change
     */
    public <T extends Identifiable> void update(DatasetIdentifier datasetIdentifier, T t) {
        remove(datasetIdentifier, t.getId());
        save(datasetIdentifier, t);
    }

    private Dataset<? extends Identifiable> getDataset(DatasetIdentifier datasetIdentifier) {
        return identifierDatasetMap.get(datasetIdentifier);
    }
}