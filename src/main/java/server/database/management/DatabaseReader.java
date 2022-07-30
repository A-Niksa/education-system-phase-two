package server.database.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import server.database.datasets.Dataset;
import server.database.datasets.DatasetIdentifier;
import shareables.models.idgeneration.Identifiable;
import shareables.utils.objectmapping.ObjectMapperUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DatabaseReader {
    private Map<DatasetIdentifier, Dataset<? extends Identifiable>> identifierDatasetMap;
    private ObjectMapper objectMapper;

    public DatabaseReader(Map<DatasetIdentifier, Dataset<? extends Identifiable>> identifierDatasetMap) {
        this.identifierDatasetMap = identifierDatasetMap;
        objectMapper = ObjectMapperUtils.getCustomObjectMapper();
    }

    public void loadDatabase() {
        for (DatasetIdentifier datasetIdentifier : DatasetIdentifier.values()) {
            loadDataset(datasetIdentifier);
        }
    }

    private void loadDataset(DatasetIdentifier datasetIdentifier) {
        Dataset<? extends Identifiable> dataset = new Dataset<>(datasetIdentifier);
        readFilesAndSaveToDataset(datasetIdentifier.getFolderPath(), dataset);
        identifierDatasetMap.put(datasetIdentifier, dataset);
    }

    private void readFilesAndSaveToDataset(String folderPath, Dataset<?> dataset) {
        List<File> filesInFolder = getFilesInFolder(folderPath);

    }

    private List<File> getFilesInFolder(String folderPath) {
        List<File> filesInFolder = new ArrayList<>();
        try {
            filesInFolder = Files.walk(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filesInFolder;
    }
}
