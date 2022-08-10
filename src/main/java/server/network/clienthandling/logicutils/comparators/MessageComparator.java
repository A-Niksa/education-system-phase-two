package server.network.clienthandling.logicutils.comparators;

import shareables.models.pojos.messaging.Message;

import java.util.Comparator;

public class MessageComparator implements Comparator<Message> {
    @Override
    public int compare(Message firstMessage, Message secondMessage) {
        return firstMessage.getMessageDate().compareTo(secondMessage.getMessageDate());
    }
}
