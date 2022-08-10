package shareables.models.pojos.messaging;

import shareables.models.idgeneration.IdentifiableWithTime;
import shareables.models.idgeneration.SequentialIdGenerator;

import java.util.ArrayList;
import java.util.List;

public class Conversation extends IdentifiableWithTime {
    private static SequentialIdGenerator sequentialIdGenerator;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator();
    }

    private List<Message> messages;
    private List<String> conversingUserIds;

    public Conversation() {
        messages = new ArrayList<>();
        conversingUserIds = new ArrayList<>();
        initializeId(sequentialIdGenerator);
    }

    public void addToMessages(Message message) {
        messages.add(message);
    }

    public void removeFromMessages(String messageId) {
        messages.removeIf(e -> e.getId().equals(messageId));
    }

    public void addToConversingUserIds(String userId) {
        conversingUserIds.add(userId);
    }

    public void removeFromConversingUserIds(String userId) {
        conversingUserIds.removeIf(e -> e.equals(userId));
    }

//    public Message fetchLastMessage() {
//        if (messages.isEmpty()) return null;
//        return messages.get(messages.size() - 1);
//    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<String> getConversingUserIds() {
        return conversingUserIds;
    }

    public void setConversingUserIds(List<String> conversingUserIds) {
        this.conversingUserIds = conversingUserIds;
    }
}
