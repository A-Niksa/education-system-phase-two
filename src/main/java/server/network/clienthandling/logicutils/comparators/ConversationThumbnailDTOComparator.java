package server.network.clienthandling.logicutils.comparators;

import shareables.network.DTOs.messaging.ConversationThumbnailDTO;

import java.time.LocalDateTime;
import java.util.Comparator;

public class ConversationThumbnailDTOComparator implements Comparator<ConversationThumbnailDTO> {
    @Override
    public int compare(ConversationThumbnailDTO firstDTO, ConversationThumbnailDTO secondDTO) {
        LocalDateTime firstDTOLastMessageDate = firstDTO.getDateOfLastMessage();
        LocalDateTime secondDTOLastMessageDate = secondDTO.getDateOfLastMessage();
        return firstDTOLastMessageDate.compareTo(secondDTOLastMessageDate);
    }
}
