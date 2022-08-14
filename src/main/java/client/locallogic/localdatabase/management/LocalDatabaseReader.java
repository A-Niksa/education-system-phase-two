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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalDatabaseReader {
    private Map<LocalDatasetIdentifier, LocalDataset> identifierDatasetMap;
    private ObjectMapper objectMapper;
    private String currentUserId;

    public LocalDatabaseReader(Map<LocalDatasetIdentifier, LocalDataset> identifierDatasetMap, String currentUserId) {
        this.identifierDatasetMap = identifierDatasetMap;
        this.currentUserId = currentUserId;
        objectMapper = ObjectMapperUtils.getDatabaseObjectMapper();
    }

    public void loadDatabase() {
        Stream.of(LocalDatasetIdentifier.values()).parallel().forEach(this::loadDataset);
    }

    private void loadDataset(LocalDatasetIdentifier localDatasetIdentifier) {
        LocalDataset localDataset = new LocalDataset();
        readFilesAndSaveToDataset(localDatasetIdentifier, localDataset);
        identifierDatasetMap.put(localDatasetIdentifier, localDataset);
    }

    private void readFilesAndSaveToDataset(LocalDatasetIdentifier localDatasetIdentifier, LocalDataset localDataset) {
        List<File> filesInFolder = getFilesInFolder(getUserDatasetFolderPath());
        readFilesInFolder(localDatasetIdentifier, filesInFolder, localDataset);
    }

    private String getUserDatasetFolderPath() {
        return ConfigManager.getString(ConfigFileIdentifier.ADDRESSES, "localDatasetsFolderPath")
                + currentUserId + "/";
    }

    private void readFilesInFolder(LocalDatasetIdentifier localDatasetIdentifier, List<File> filesInFolder, LocalDataset localDataset) {
        filesInFolder.stream()
                .map(e -> readValueByObjectMapper(e, localDatasetIdentifier))
                .forEach(localDataset::save);
    }

    private Identifiable readValueByObjectMapper(File file, LocalDatasetIdentifier localDatasetIdentifier) {
        Identifiable identifiable = null;
        try {
            identifiable = (Identifiable) objectMapper.readValue(file, localDatasetIdentifier.getClassLiteral());
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

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }
}
