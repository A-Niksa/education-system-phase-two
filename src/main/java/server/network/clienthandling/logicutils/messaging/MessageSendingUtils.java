package server.network.clienthandling.logicutils.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.media.MediaFile;
import shareables.models.pojos.messaging.Conversation;
import shareables.models.pojos.messaging.Message;
import shareables.models.pojos.messaging.MessageType;
import shareables.models.pojos.users.User;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.objectmapping.ObjectMapperUtils;

import java.util.ArrayList;
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
        User sender = IdentifiableFetchingUtils.getUser(databaseManager, senderId);
        for (String receiverId : receiverIds) {
            Conversation receiverConversation = MessengerViewUtils.getContactConversation(databaseManager, receiverId, senderId);
            if (receiverConversation == null) {
                receiverConversation = createNewConversation(senderId, receiverId);
                receiverConversation.addToMessages(message);

                User receiver = IdentifiableFetchingUtils.getUser(databaseManager, receiverId);
                receiver.getMessenger().addToConversations(receiverConversation);

                Conversation senderConversation = copyConversation(receiverConversation);
                sender.getMessenger().addToConversations(senderConversation);
            } else {
                receiverConversation.addToMessages(message);

                Conversation senderConversation = MessengerViewUtils.getContactConversation(databaseManager, senderId,
                        receiverId);
                senderConversation.addToMessages(message);
            }
        }
    }

    private static Conversation copyConversation(Conversation conversation) {
        ObjectMapper objectMapper = ObjectMapperUtils.getNetworkObjectMapper();
        String conversationJson = null;
        try {
            conversationJson = objectMapper.writeValueAsString(conversation);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (conversationJson == null) return null;

        Conversation copiedConversation = null;
        try {
            copiedConversation = objectMapper.readValue(conversationJson, Conversation.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (copiedConversation == null) return null;

        copiedConversation.initializeId(Conversation.getSequentialIdGenerator());
        return copiedConversation;
    }

    private static Conversation createNewConversation(String senderId, String receiverId) {
        Conversation conversation = new Conversation();
        conversation.addToConversingUserIds(senderId);
        conversation.addToConversingUserIds(receiverId);

        return conversation;
    }
}
