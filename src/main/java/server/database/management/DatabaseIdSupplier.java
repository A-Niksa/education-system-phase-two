package server.database.management;

import server.database.datasets.DatasetIdentifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseIdSupplier {
    private static DatabaseIdSupplier databaseIdSupplier;

    private DatabaseIdSupplier() {

    }

    private static DatabaseIdSupplier getInstance() {
        if (databaseIdSupplier == null) databaseIdSupplier = new DatabaseIdSupplier();
        return databaseIdSupplier;
    }

    public static int getDatasetIdCounter(DatasetIdentifier datasetIdentifier) {
        return getInstance()
                .getNumberOfFilesInFolder(datasetIdentifier.getFolderPath());
    }

    private int getNumberOfFilesInFolder(String folderPath) {
        int numberOfFilesInFolder = -1;
        try {
            numberOfFilesInFolder = (int) Files.walk(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .count();
            // casting is viable, since it's far-fetched that the number of files exceeds Integer.MAX_VALUE
        } catch (IOException e) {
            e.printStackTrace();
        }
        return numberOfFilesInFolder;
    }
}
