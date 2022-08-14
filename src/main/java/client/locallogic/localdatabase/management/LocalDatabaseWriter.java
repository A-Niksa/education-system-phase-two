package client.locallogic.localdatabase.management;

import client.locallogic.localdatabase.datasets.LocalDataset;
import client.locallogic.localdatabase.datasets.LocalDatasetIdentifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import shareables.models.idgeneration.Identifiable;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.objectmapping.ObjectMapperUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LocalDatabaseWriter { // save in this context refers to saving to file
    private Map<LocalDatasetIdentifier, LocalDataset> identifierDatasetMap;
    private ObjectMapper objectMapper;
    private File datasetsDirectory;

    public LocalDatabaseWriter(Map<LocalDatasetIdentifier, LocalDataset> identifierDatasetMap) {
        this.identifierDatasetMap = identifierDatasetMap;
        objectMapper = ObjectMapperUtils.getDatabaseObjectMapper();
        datasetsDirectory = new File(ConfigManager.getString(ConfigFileIdentifier.ADDRESSES, "localDatasetsFolderPath"));
    }

    public void saveDatabase() {
        purgeDirectory(datasetsDirectory);
        identifierDatasetMap.entrySet().stream().parallel().forEach(this::saveDataset);
    }

    // TODO: should be private
    public void purgeDirectory(File directory) {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) purgeDirectory(file);
            else file.delete();
        }
    }

    private void saveDataset(Map.Entry<LocalDatasetIdentifier, LocalDataset> identifierDatasetEntry) {
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
