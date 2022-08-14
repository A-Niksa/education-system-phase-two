package shareables.network.DTOs.offlinemode;

import shareables.network.DTOs.messaging.ConversationDTO;

import java.util.ArrayList;
import java.util.List;

public class OfflineMessengerDTO {
    private String ownerId;
    private List<ConversationDTO> shortenedConversationDTOs;

    public OfflineMessengerDTO() {
    }

    public OfflineMessengerDTO(String ownerId) {
        this.ownerId = ownerId;
        shortenedConversationDTOs = new ArrayList<>();
    }

    public void addToShortenedConversationDTOs(ConversationDTO shortenedConversationDTO) {
        shortenedConversationDTOs.add(shortenedConversationDTO);
    }

    public ConversationDTO fetchConversationDTO(String contactId) {
        return shortenedConversationDTOs.stream()
                .filter(conversationDTO -> conversationDTO.getContactId().equals(contactId))
                .findAny().orElse(null);
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<ConversationDTO> getShortenedConversationDTOs() {
        return shortenedConversationDTOs;
    }

    public void setShortenedConversationDTOs(List<ConversationDTO> shortenedConversationDTOs) {
        this.shortenedConversationDTOs = shortenedConversationDTOs;
    }
}
