package server.database.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import server.database.datasets.Dataset;
import server.database.datasets.DatasetIdentifier;
import shareables.models.idgeneration.Identifiable;
import shareables.utils.objectmapping.ObjectMapperUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DatabaseWriter { // save in this context refers to saving to file
    private Map<DatasetIdentifier, Dataset> identifierDatasetMap;
    private ObjectMapper objectMapper;

    public DatabaseWriter(Map<DatasetIdentifier, Dataset> identifierDatasetMap) {
        this.identifierDatasetMap = identifierDatasetMap;
        objectMapper = ObjectMapperUtils.getDatabaseObjectMapper();
    }

    public void saveDatabase() {
        identifierDatasetMap.entrySet().stream().parallel().forEach(this::saveDataset);
    }

    private void saveDataset(Map.Entry<DatasetIdentifier, Dataset> identifierDatasetEntry) {
        List<Identifiable> identifiables = identifierDatasetEntry.getValue().getIdentifiables();
        identifiables.stream()
                .forEach(e -> saveIdentifiable(e, identifierDatasetEntry.getKey().getFolderPath()));
    }

    private void saveIdentifiable(Identifiable identifiable, String folderPath) {
        String id = identifiable.getId();
        File file = new File(folderPath + id + ".json");
        try {
            objectMapper.writeValue(file, identifiable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
