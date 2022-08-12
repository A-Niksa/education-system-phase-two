package client.gui.utils;

import shareables.models.pojos.media.MediaFile;
import shareables.models.pojos.messaging.Message;
import shareables.models.pojos.messaging.MessageType;
import shareables.utils.timing.formatting.FormattingUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConversationDisplayUtils {
    public static String getDisplayableConversationText(List<Message> messages, String ownerId, String contactName) {
        StringBuilder converationStringBuilder = new StringBuilder();
        StringBuilder newLineStringBuilder = new StringBuilder("\n");
        messages.forEach(message -> {
            if (message.getSenderId().equals(ownerId)) {
                converationStringBuilder.append(formatMessageStringBuilder("You", message.getMessageDate(),
                        message.getMessageType(), message.getMessageMediaFile(), message.getMessageText()));
            } else {
                converationStringBuilder.append(formatMessageStringBuilder(contactName, message.getMessageDate(),
                        message.getMessageType(), message.getMessageMediaFile(), message.getMessageText()));
            }
            converationStringBuilder.append(newLineStringBuilder).append(newLineStringBuilder);
        });
        converationStringBuilder.delete(converationStringBuilder.length() - 2, converationStringBuilder.length() - 1);
        return converationStringBuilder.toString();
    }

    private static StringBuilder formatMessageStringBuilder(String senderName, LocalDateTime messageDate, MessageType messageType,
                                                            MediaFile messageMediaFile, String messageText) {
        StringBuilder formattedMessage = null;
        DateTimeFormatter dateTimeFormatter = FormattingUtils.getExtensiveDateTimeFormatter();
        if (messageType == MessageType.TEXT) {
            formattedMessage = new StringBuilder(
                    String.format("[%s - %s]: %s", senderName, dateTimeFormatter.format(messageDate), messageText)
            );
        } else if (messageType == MessageType.MEDIA) { // put else if instead of else because of explicitness and scalability
            formattedMessage = new StringBuilder(
                    String.format("[%s - %s]: %s (ID: %s)", senderName, dateTimeFormatter.format(messageDate),
                            messageMediaFile.getMediaFileIdentifier(), messageMediaFile.getId())
            );
        }
        return formattedMessage;
    }
}
