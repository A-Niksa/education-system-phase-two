package shareables.models.pojos.coursewares;

import shareables.models.idgeneration.IdentifiableWithTime;
import shareables.models.pojos.media.MediaFile;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

public class EducationalItem extends IdentifiableWithTime {
    private ItemType itemType;
    private String text;
    private MediaFile mediaFile;

    public EducationalItem() {
        initializeId();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MediaFile getMediaFile() {
        return mediaFile;
    }

    public void setMediaFile(MediaFile mediaFile) {
        this.mediaFile = mediaFile;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public String toString() {
        return "<html>" +
                    id + " - " + itemType.toString() +
                    "<br/>" +
                    descriptionToString() +
                "</html>";
    }

    public String toExtensiveString() {
        return "<html>" +
                    id + " - " + itemType.toString() +
                    "<br/>" +
                    descriptionToString() + " - " + itemType.toString() +
                "</html>";
    }

    private String descriptionToString() {
        if (itemType == ItemType.TEXT) {
            return fetchShortenedText();
        } else if (itemType == ItemType.MEDIA_FILE) {
            return mediaFile.getMediaFileIdentifier().toString();
        }
        return "";
    }

    private String fetchShortenedText() {
        int maximumNumberOfCharacters = ConfigManager.getInt(ConfigFileIdentifier.CONSTANTS, "maxNumberOfChars");
        String shortenedLastMessageText = text.substring(0, Math.min(maximumNumberOfCharacters, text.length()));
        String threeDots = text.length() <= maximumNumberOfCharacters ? "" : "...";
        return shortenedLastMessageText + threeDots;
    }
}
