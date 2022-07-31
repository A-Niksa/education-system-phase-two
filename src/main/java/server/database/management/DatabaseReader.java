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
import java.util.stream.Stream;

public class DatabaseReader {
    private Map<DatasetIdentifier, Dataset> identifierDatasetMap;
    private ObjectMapper objectMapper;

    public DatabaseReader(Map<DatasetIdentifier, Dataset> identifierDatasetMap) {
        this.identifierDatasetMap = identifierDatasetMap;
        objectMapper = ObjectMapperUtils.getCustomObjectMapper();
    }

    public void loadDatabase() {
        Stream.of(DatasetIdentifier.values()).parallel().forEach(this::loadDataset);
    }

    private void loadDataset(DatasetIdentifier datasetIdentifier) {
        Dataset dataset = new Dataset();
        readFilesAndSaveToDataset(datasetIdentifier, dataset);
        identifierDatasetMap.put(datasetIdentifier, dataset);
    }

    private void readFilesAndSaveToDataset(DatasetIdentifier datasetIdentifier, Dataset dataset) {
        List<File> filesInFolder = getFilesInFolder(datasetIdentifier.getFolderPath());
        readFilesInFolder(datasetIdentifier, filesInFolder, dataset);
    }

    private void readFilesInFolder(DatasetIdentifier datasetIdentifier, List<File> filesInFolder, Dataset dataset) {
        filesInFolder.stream()
                .map(e -> readValueByObjectMapper(e, datasetIdentifier))
                .forEach(dataset::save);
    }

    private Identifiable readValueByObjectMapper(File file, DatasetIdentifier datasetIdentifier) {
        Identifiable identifiable = null;
        try {
            identifiable = (Identifiable) objectMapper.readValue(file, datasetIdentifier.getClassLiteral());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return identifiable;
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
