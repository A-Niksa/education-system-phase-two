package shareables.models.pojos.messaging;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Chats")
public class Chat {
    @Id
    private String id;
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "Chats_Users")
    private List<Message> messages;
    @Column
    private Date dateOfCreation;

    public Chat() {
        dateOfCreation = new Date();
        messages = new ArrayList<>();
        initializeId();
    }

    public void addToMessages(Message message) {
        messages.add(message);
    }

    public void removeFromMessages(Message message) {
        messages.removeIf(e -> e.getId().equals(message.getId()));
    }

    public void initializeId() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }
}
