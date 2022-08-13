package shareables.network.DTOs.messaging;

import shareables.models.pojos.media.MediaFile;
import shareables.models.pojos.media.MediaFileIdentifier;
import shareables.models.pojos.messaging.MessageType;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import java.time.LocalDateTime;

public class ConversationThumbnailDTO {
    private MessageType messageType;
    private String contactId;
    private String contactName;
    private String lastMessageSenderName;
    private String lastMessageText;
    private MediaFileIdentifier lastMediaIdentifier;
    private LocalDateTime dateOfLastMessage;

    public ConversationThumbnailDTO() {
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getLastMessageSenderName() {
        return lastMessageSenderName;
    }

    public void setLastMessageSenderName(String lastMessageSenderName) {
        this.lastMessageSenderName = lastMessageSenderName;
    }

    public String getLastMessageText() {
        return lastMessageText;
    }

    public void setLastMessageText(String lastMessageText) {
        this.lastMessageText = lastMessageText;
    }

    public MediaFileIdentifier getLastMediaIdentifier() {
        return lastMediaIdentifier;
    }

    public void setLastMediaIdentifier(MediaFileIdentifier lastMediaIdentifier) {
        this.lastMediaIdentifier = lastMediaIdentifier;
    }

    public LocalDateTime getDateOfLastMessage() {
        return dateOfLastMessage;
    }

    public void setDateOfLastMessage(LocalDateTime dateOfLastMessage) {
        this.dateOfLastMessage = dateOfLastMessage;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    @Override
    public String toString() {
        switch (messageType) {
            case TEXT:
                return textMessageToString();
            case MEDIA:
                return mediaMessageToString();
            default:
                return null;
        }
    }

    private String textMessageToString() {
        return "<html>" +
                    contactId + " - " + contactName +
                    "<br/>" +
                    lastMessageSenderName + ": " + fetchShortenedLastMessageText() +
                "</html>";
    }

    private String mediaMessageToString() {
        return "<html>" +
                    contactId + " - " + contactName +
                    "<br/>" +
                    lastMessageSenderName + ": " + lastMediaIdentifier +
                "</html>";
    }

    private String fetchShortenedLastMessageText() {
        int maximumNumberOfCharacters = ConfigManager.getInt(ConfigFileIdentifier.CONSTANTS, "maxNumberOfChars");
        String shortenedLastMessageText = lastMessageText.substring(0,
                Math.min(maximumNumberOfCharacters, lastMessageText.length()));
        String threeDots = lastMessageText.length() <= maximumNumberOfCharacters ? "" : "...";
        return shortenedLastMessageText + threeDots;
    }
}
