package client.locallogic.messaging;

import shareables.models.pojos.media.MediaFile;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static client.locallogic.messaging.SuffixExtractionUtils.getFileSuffix;

public class DownloadManager {
    private final String downloadsFolderPath;

    public DownloadManager() {
        downloadsFolderPath = ConfigManager.getString(ConfigFileIdentifier.ADDRESSES, "downloadsFolderPath");
    }

    /**
     * returns a prompt telling the client where the media file was saved
     */
    public String saveMediaToLocalDownloads(MediaFile mediaFile) {
        byte[] encodedBytes = mediaFile.getEncodedBytes();
        String fileName = generateFileName(mediaFile);
        Path path = Paths.get(downloadsFolderPath + fileName);

        try {
            Files.write(path, encodedBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ConfigManager.getString(ConfigFileIdentifier.TEXTS, "downloadedFile") + fileName;
    }

    private String generateFileName(MediaFile mediaFile) {
        String fileSuffix = getFileSuffix(mediaFile.getPath());
        return mediaFile.getId() + fileSuffix;
    }
}
