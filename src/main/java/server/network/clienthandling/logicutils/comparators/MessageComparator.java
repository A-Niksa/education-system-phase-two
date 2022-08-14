package server.network.clienthandling.logicutils.comparators;

import shareables.models.pojos.messaging.Message;

import java.util.Comparator;

public class MessageComparator implements Comparator<Message> {
    @Override
    public int compare(Message firstMessage, Message secondMessage) {
        int comparisonResult = firstMessage.getMessageDate().compareTo(secondMessage.getMessageDate());
        if (comparisonResult == 0) {
            return 1;
        } else {
            return comparisonResult;
        }
    }
}
