package server.network.clienthandling.logicutils.general;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.comparators.MessageComparator;
import shareables.models.pojos.messaging.Conversation;
import shareables.models.pojos.messaging.Message;
import shareables.models.pojos.messaging.Messenger;
import shareables.models.pojos.users.User;
import shareables.network.DTOs.messaging.ConversationDTO;
import shareables.network.DTOs.offlinemode.OfflineMessengerDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OfflineMessengerUtils {
    private static MessageComparator messageComparator;
    static {
        messageComparator = new MessageComparator();
    }

    public static OfflineMessengerDTO getOfflineMessengerDTO(DatabaseManager databaseManager, Messenger messenger) {
        OfflineMessengerDTO offlineMessengerDTO = new OfflineMessengerDTO();

        int numberOfOfflineModeMessages = ConfigManager.getInt(ConfigFileIdentifier.CONSTANTS,
                "numberOfOfflineModeMessages");

        offlineMessengerDTO.setOwnerId(messenger.getOwnerId());
        offlineMessengerDTO.setShortenedConversationDTOs(getShortenedConversationDTOs(databaseManager,
                        messenger.getConversations(), messenger.getOwnerId(), numberOfOfflineModeMessages));

        return offlineMessengerDTO;
    }

    private static List<ConversationDTO> getShortenedConversationDTOs(DatabaseManager databaseManager,
                                                                      List<Conversation> conversations, String ownerId,
                                                                      int numberOfOfflineModeMessages) {
        return conversations.stream()
                .map(conversation -> {
                    return getShortenedConversationDTO(databaseManager, conversation, ownerId, numberOfOfflineModeMessages);
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static ConversationDTO getShortenedConversationDTO(DatabaseManager databaseManager, Conversation conversation,
                                                               String ownerId, int numberOfOfflineModeMessages) {
        conversation.getMessages().sort(messageComparator);
        Collections.reverse(conversation.getMessages());

        List<Message> shortenedListOfMessages = conversation.getMessages().stream()
                .limit(numberOfOfflineModeMessages)
                .collect(Collectors.toCollection(ArrayList::new));

        Conversation shortenedConversation = new Conversation();
        shortenedConversation.setMessages(shortenedListOfMessages);
        shortenedConversation.setConversingUserIds(conversation.getConversingUserIds());

        ConversationDTO shortenedConversationDTO = new ConversationDTO();
        shortenedConversationDTO.setConversation(shortenedConversation);

        String contactId = getContactId(conversation.getConversingUserIds(), ownerId);
        User contact = IdentifiableFetchingUtils.getUser(databaseManager, contactId);
        shortenedConversationDTO.setContactId(contactId);
        shortenedConversationDTO.setContactName(contact.fetchName());
        shortenedConversationDTO.setContactProfilePicture(contact.getProfilePicture());

        return shortenedConversationDTO;
    }

    private static String getContactId(List<String> conversingUserIds, String ownerId) {
        return conversingUserIds.stream()
                .filter(id -> !id.equals(ownerId))
                .findAny().orElse(null);
    }
}