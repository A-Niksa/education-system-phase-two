package server.network.clienthandling.logicutils.messaging;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.messaging.Message;
import shareables.models.pojos.messaging.MessageType;
import shareables.models.pojos.messaging.Messenger;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.professors.Professor;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import java.util.ArrayList;
import java.util.List;

import static server.network.clienthandling.logicutils.messaging.MessageSendingUtils.addMessageToSenderAndReceiversConversations;

public class AdminMessagingUtils {
    public static void addAdminHelpingMessageToUserMessenger(DatabaseManager databaseManager, User user) {
        String adminId = ConfigManager.getString(ConfigFileIdentifier.CONSTANTS, "adminId");
        String messageText = ConfigManager.getString(ConfigFileIdentifier.TEXTS, "adminHelpingPrompt");

        Message message = new Message();

        message.setSenderId(adminId);
        message.setMessageType(MessageType.TEXT);
        message.setMessageText(messageText);
        addMessageToSenderAndReceiversConversations(databaseManager, adminId, new ArrayList<>(List.of(user.getId())),
                message);
    }

    public static void removeProfessorFromConversations(DatabaseManager databaseManager, String professorId) {
        Professor professor = IdentifiableFetchingUtils.getProfessor(databaseManager, professorId);
        professor.getMessenger().getConversations().stream()
                .flatMap(conversation -> conversation.getConversingUserIds().stream())
                .filter(id -> !id.equals(professorId))
                .distinct()
                .map(id -> IdentifiableFetchingUtils.getUser(databaseManager, id))
                .forEach(user -> {
                    removeProfessorFromUserConversations(user, professorId);
                });
    }

    private static void removeProfessorFromUserConversations(User user, String professorId) {
        List<String> conversationIdsToRemove = new ArrayList<>();
        user.getMessenger().getConversations().stream()
                .filter(conversation -> conversation.getConversingUserIds().contains(professorId))
                .forEach(conversation -> {
                    conversationIdsToRemove.add(conversation.getId());
                });

        removeFromMessengerByConversationIds(user.getMessenger(), conversationIdsToRemove);
    }

    private static void removeFromMessengerByConversationIds(Messenger messenger, List<String> conversationIdsToRemove) {
        conversationIdsToRemove.forEach(messenger::removeFromConversations);
    }
}
