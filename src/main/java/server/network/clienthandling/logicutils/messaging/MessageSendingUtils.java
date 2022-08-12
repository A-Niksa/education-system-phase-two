package server.network.clienthandling.logicutils.messaging;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import server.network.clienthandling.logicutils.login.LoginUtils;
import shareables.models.pojos.media.MediaFile;
import shareables.models.pojos.messaging.Conversation;
import shareables.models.pojos.messaging.Message;
import shareables.models.pojos.messaging.MessageType;

public class MessageSendingUtils {
    public static void sendTextMessage(DatabaseManager databaseManager, String senderId, String receiverId,
                                       String messageText) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setMessageType(MessageType.TEXT);
        message.setMessageText(messageText);

        addMessageToSenderAndReceiverConversations(databaseManager, senderId, receiverId, message);
    }

    public static void sendMediaMessage(DatabaseManager databaseManager, String senderId, String receiverId,
                                        MediaFile messageMedia) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setMessageType(MessageType.MEDIA);
        message.setMessageMediaFile(messageMedia);

        addMessageToSenderAndReceiverConversations(databaseManager, senderId, receiverId, message);
    }

    private static void addMessageToSenderAndReceiverConversations(DatabaseManager databaseManager, String senderId,
                                                                   String receiverId, Message message) {
        Conversation senderConversation = MessengerViewUtils.getContactConversation(databaseManager, senderId, receiverId);
        senderConversation.addToMessages(message);

        Conversation receiverConversation = MessengerViewUtils.getContactConversation(databaseManager, receiverId, senderId);
        receiverConversation.addToMessages(message);
    }
}
