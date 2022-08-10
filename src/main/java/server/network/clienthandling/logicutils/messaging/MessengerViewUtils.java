package server.network.clienthandling.logicutils.messaging;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.comparators.ConversationThumbnailDTOComparator;
import server.network.clienthandling.logicutils.comparators.MessageComparator;
import server.network.clienthandling.logicutils.login.LoginUtils;
import shareables.models.pojos.messaging.Conversation;
import shareables.models.pojos.messaging.Message;
import shareables.models.pojos.messaging.MessageType;
import shareables.models.pojos.users.User;
import shareables.network.DTOs.ConversationThumbnailDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessengerViewUtils {
    private static ConversationThumbnailDTOComparator conversationThumbnailDTOComparator;
    private static MessageComparator messageComparator;
    static {
        conversationThumbnailDTOComparator = new ConversationThumbnailDTOComparator();
        messageComparator = new MessageComparator();
    }

    public static List<ConversationThumbnailDTO> getConversationThumbnailDTOs(DatabaseManager databaseManager,
                                                                              String username) {
        List<Conversation> conversations = getUserConversations(databaseManager, username);
        List<ConversationThumbnailDTO> conversationThumbnailDTOs = new ArrayList<>();
        conversations.forEach(conversation -> {
            conversationThumbnailDTOs.add(
                    initializeConversationThumbnailDTO(databaseManager, conversation, username)
            );
        });
        conversationThumbnailDTOs.sort(conversationThumbnailDTOComparator);
        Collections.reverse(conversationThumbnailDTOs);
        return conversationThumbnailDTOs;
    }

    private static ConversationThumbnailDTO initializeConversationThumbnailDTO(DatabaseManager databaseManager,
                                                                               Conversation conversation, String ownerUsername) {
        ConversationThumbnailDTO conversationThumbnailDTO = new ConversationThumbnailDTO();
        Message lastMessage = getConversationLastMessage(conversation);
        conversationThumbnailDTO.setMessageType(lastMessage.getMessageType());
        conversationThumbnailDTO.setLastMessageText(lastMessage.getMessageText());
        conversationThumbnailDTO.setDateOfLastMessage(lastMessage.getMessageDate());

        User sender = LoginUtils.getUser(databaseManager, lastMessage.getSenderId());
        conversationThumbnailDTO.setLastMessageSenderName(sender.fetchName());

        String contactId = conversation.getConversingUserIds().stream()
                .filter(id -> !id.equals(ownerUsername))
                .findAny().orElse(null);
        conversationThumbnailDTO.setContactId(contactId);

        User contact = LoginUtils.getUser(databaseManager, contactId);
        conversationThumbnailDTO.setContactName(contact.fetchName());

        if (lastMessage.getMessageType() == MessageType.MEDIA) {
            conversationThumbnailDTO.setLastMediaIdentifier(lastMessage.getMessageMediaFile().getMediaFileIdentifier());
        }

        return conversationThumbnailDTO;
    }

    private static Message getConversationLastMessage(Conversation conversation) {
        List<Message> messages = conversation.getMessages();
        messages.sort(messageComparator);
        return messages.get(messages.size() - 1);
    }

    private static List<Conversation> getUserConversations(DatabaseManager databaseManager, String username) {
        User user = LoginUtils.getUser(databaseManager, username);
        return user.getMessenger().getConversations();
    }
}
