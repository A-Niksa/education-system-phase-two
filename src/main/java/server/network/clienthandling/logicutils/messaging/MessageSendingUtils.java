package server.network.clienthandling.logicutils.messaging;

import server.database.management.DatabaseManager;
import shareables.models.pojos.media.MediaFile;
import shareables.models.pojos.messaging.Conversation;
import shareables.models.pojos.messaging.Message;
import shareables.models.pojos.messaging.MessageType;

import java.util.List;

public class MessageSendingUtils {
    public static void sendTextMessage(DatabaseManager databaseManager, String senderId, List<String> receiverIds,
                                       String messageText) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setMessageType(MessageType.TEXT);
        message.setMessageText(messageText);

        addMessageToSenderAndReceiversConversations(databaseManager, senderId, receiverIds, message);
    }

    public static void sendMediaMessage(DatabaseManager databaseManager, String senderId, List<String> receiverIds,
                                        MediaFile messageMedia) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setMessageType(MessageType.MEDIA);
        message.setMessageMediaFile(messageMedia);

        addMessageToSenderAndReceiversConversations(databaseManager, senderId, receiverIds, message);
    }

    private static void addMessageToSenderAndReceiversConversations(DatabaseManager databaseManager, String senderId,
                                                                    List<String> receiverIds, Message message) {
        for (String receiverId : receiverIds) {
            Conversation senderConversation = MessengerViewUtils.getContactConversation(databaseManager, senderId, receiverId);
            senderConversation.addToMessages(message);


            Conversation receiverConversation = MessengerViewUtils.getContactConversation(databaseManager, receiverId, senderId);
            receiverConversation.addToMessages(message);
        }
    }
}
