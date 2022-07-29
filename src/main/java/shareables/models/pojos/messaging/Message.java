package shareables.models.pojos.messaging;

import shareables.models.idgeneration.IdGenerator;
import server.database.models.pojos.media.MediaFile;
import server.database.models.pojos.users.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Messages")
public class Message {
    @Id
    private String id;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    private User sender;
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "Senders_MediaFiles")
    private MediaFile messageMediaFile;
    @Column
    private Date messageDate;
    @Column
    private String messageText;

    public Message() {
        messageDate = new Date(); // setting current time
        initializeId();
    }

    public void initializeId() {
        id = IdGenerator.generateId(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public MediaFile getMessageMediaFile() {
        return messageMediaFile;
    }

    public void setMessageMediaFile(MediaFile messageMediaFile) {
        this.messageMediaFile = messageMediaFile;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
