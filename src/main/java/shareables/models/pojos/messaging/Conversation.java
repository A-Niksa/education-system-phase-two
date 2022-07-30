package shareables.models.pojos.messaging;

import shareables.models.idgeneration.IdGenerator;
import shareables.models.idgeneration.IdentifiableWithTime;
import shareables.models.idgeneration.SequentialIdGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Conversation extends IdentifiableWithTime {
    private static SequentialIdGenerator sequentialIdGenerator;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator();
    }

    private List<Message> messages;

    public Conversation() {
        messages = new ArrayList<>();
        initializeId(sequentialIdGenerator);
    }

    public void addToMessages(Message message) {
        messages.add(message);
    }

    public void removeFromMessages(String messageId) {
        messages.removeIf(e -> e.getId().equals(messageId));
    }

    public Message getLastMessage() {
        return messages.get(messages.size() - 1);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
