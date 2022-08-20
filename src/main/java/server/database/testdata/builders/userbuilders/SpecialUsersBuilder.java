package server.database.testdata.builders.userbuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import server.network.clienthandling.logicutils.messaging.MessengerViewUtils;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.messaging.Conversation;
import shareables.models.pojos.messaging.Message;
import shareables.models.pojos.messaging.MessageType;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.specialusers.Admin;
import shareables.models.pojos.users.specialusers.MrMohseni;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.objectmapping.ObjectMapperUtils;

import java.util.ArrayList;
import java.util.List;

public class SpecialUsersBuilder {
    private Admin admin;
    private MrMohseni mrMohseni;

    public void buildSpecialUsers(DatabaseManager databaseManager) {
        admin = new Admin();
        mrMohseni = new MrMohseni();

        saveSpecialUsers(databaseManager);
        sendAdminHelpMessages(databaseManager);
    }

    private void sendAdminHelpMessages(DatabaseManager databaseManager) {
        List<Identifiable> allUsers = new ArrayList<>();
        allUsers.addAll(databaseManager.getIdentifiables(DatasetIdentifier.PROFESSORS));
        allUsers.addAll(databaseManager.getIdentifiables(DatasetIdentifier.STUDENTS));

        allUsers.add(databaseManager.get(DatasetIdentifier.SPECIAL_USERS, mrMohseni.getId()));

        String messageText = ConfigManager.getString(ConfigFileIdentifier.TEXTS, "adminHelpingPrompt");
        String adminId = admin.getId();
        allUsers.forEach(user -> {
            Message message = new Message();
            message.setSenderId(adminId);
            message.setMessageType(MessageType.TEXT);
            message.setMessageText(messageText);
            addMessageToSenderAndReceiversConversations(databaseManager, adminId, new ArrayList<>(List.of(user.getId())),
                    message);
        });
    }

    private void addMessageToSenderAndReceiversConversations(DatabaseManager databaseManager, String senderId,
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

    private Conversation copyConversation(Conversation conversation) {
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

    private Conversation createNewConversation(String senderId, String receiverId) {
        Conversation conversation = new Conversation();
        conversation.addToConversingUserIds(senderId);
        conversation.addToConversingUserIds(receiverId);

        return conversation;
    }

    private void saveSpecialUsers(DatabaseManager databaseManager) {
        databaseManager.save(DatasetIdentifier.SPECIAL_USERS, admin);
        databaseManager.save(DatasetIdentifier.SPECIAL_USERS, mrMohseni);
    }
}
