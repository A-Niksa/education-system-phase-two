package shareables.models.pojos.messaging;

import shareables.models.idgeneration.SequentialIdGenerator;
import shareables.models.pojos.users.User;
import shareables.models.idgeneration.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class Messenger { // note that this class is a pojo and has no direct messaging capability
    private static SequentialIdGenerator sequentialIdGenerator;

    private String ownerId;
    private List<Conversation> conversations;

    public Messenger() {
        conversations = new ArrayList<>();
    }

    public Messenger(String ownerId) {
        this.ownerId = ownerId;
        conversations = new ArrayList<>();
    }

    public void addToChats(Conversation conversation) {
        conversations.add(conversation);
    }

    public void removeFromChats(String conversationId) {
        conversations.removeIf(e -> e.getId().equals(conversationId));
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }
}
