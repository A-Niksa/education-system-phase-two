package client.locallogic.menus.unitselection;

import shareables.network.DTOs.unitselection.CourseThumbnailDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import java.util.ArrayList;

public class GroupThumbnailParser {
    public static String parseThumbnailDTOsAsString(ArrayList<CourseThumbnailDTO> courseThumbnailDTOs,
                                                    ConfigFileIdentifier configIdentifier) {
        StringBuilder parsedBuilder = new StringBuilder("<html>");
        for (CourseThumbnailDTO thumbnailDTO : courseThumbnailDTOs) {
            parsedBuilder.append(thumbnailDTO.getGroupNumber() + " - ");
            parsedBuilder.append(
                    thumbnailDTO.namesListToString(thumbnailDTO.getTeachingProfessorNames()) + " - "
            );
            parsedBuilder.append("Capacity: " + thumbnailDTO.getCurrentCapacity());
            parsedBuilder.append("<br/>");
        }
        parsedBuilder.append(ConfigManager.getString(configIdentifier, "groupInputPrompt"));
        parsedBuilder.append("</html>");
        return parsedBuilder.toString();
    }
}
