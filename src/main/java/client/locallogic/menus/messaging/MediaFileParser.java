package client.locallogic.menus.messaging;

import shareables.models.pojos.media.*;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MediaFileParser {
    public MediaFile convertFileToMediaFile(File file) {
        MediaFile mediaFile;
        String fileSuffix = SuffixExtractionUtils.getFileSuffix(file.getPath());
        switch (fileSuffix) { // can be expanded and scaled accordingly
            case ".mp3":
            case ".ogg":
            case ".m4a":
                mediaFile = getSoundFile(file);
                break;
            case ".pdf":
                mediaFile = getPDF(file);
                break;
            case ".jpg":
            case ".jpeg":
            case ".png":
                mediaFile = getPicture(file);
                break;
            case ".mp4":
            case ".mov":
            case ".mkv":
                mediaFile = getVideo(file);
                break;
            default:
                mediaFile = null;
        }
        return mediaFile;
    }

    private MediaFile getVideo(File file) {
        return new Video(file.getPath());
    }

    private MediaFile getPicture(File file) {
        return new Picture(file.getPath());
    }

    private MediaFile getPDF(File file) {
        return new PDF(file.getPath());
    }

    private MediaFile getSoundFile(File file) {
        return new SoundFile(file.getPath());
    }

    public boolean isFileTooLarge(File file) {
        long maximumNumberOfBytes = ConfigManager.getLong(ConfigFileIdentifier.CONSTANTS, "maxBytes");
        Path path = Paths.get(file.getPath());
        boolean fileTooLarge = true; // default value
        try {
            fileTooLarge = Files.size(path) > maximumNumberOfBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileTooLarge;
    }
}
