package server.network.clienthandling.logicutils.messaging;

import server.database.management.DatabaseManager;
import shareables.models.pojos.media.MediaFile;
import shareables.models.pojos.messaging.Conversation;
import shareables.models.pojos.messaging.Message;
import shareables.models.pojos.messaging.MessageType;

public class DownloadingUtils {
    public static boolean mediaFileExistsInConversation(DatabaseManager databaseManager, String userId, String contactId,
                                                        String mediaId) {
        Conversation conversation = MessengerViewUtils.getContactConversation(databaseManager, userId, contactId);
        if (conversation == null) return false;

        return conversation.getMessages().stream()
                .filter(message -> message.getMessageType() == MessageType.MEDIA)
                .anyMatch(message -> message.getMessageMediaFile().getId().equals(mediaId));
    }

    public static MediaFile getMediaFileFromConversation(DatabaseManager databaseManager, String userId, String contactId,
                                                         String mediaId) {
        Conversation conversation = MessengerViewUtils.getContactConversation(databaseManager, userId, contactId);
        if (conversation == null) return null;

        return conversation.getMessages().stream()
                .filter(message -> message.getMessageType() == MessageType.MEDIA)
                .map(Message::getMessageMediaFile)
                .filter(mediaFile -> mediaFile.getId().equals(mediaId))
                .findAny().orElse(null);
    }
}
