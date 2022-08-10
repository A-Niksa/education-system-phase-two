package shareables.models.pojos.messaging;

import shareables.models.idgeneration.IdGenerator;
import shareables.models.idgeneration.IdentifiableWithTime;
import shareables.models.pojos.media.MediaFile;
import shareables.models.pojos.users.User;

import java.time.LocalDateTime;
import java.util.Date;

public class Message extends IdentifiableWithTime {
    private String senderId;
    private MessageType messageType;
    private MediaFile messageMediaFile;
    private String messageText;
    private LocalDateTime messageDate;

    public Message() {
        messageDate = LocalDateTime.now();
        initializeId();
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public MediaFile getMessageMediaFile() {
        return messageMediaFile;
    }

    public void setMessageMediaFile(MediaFile messageMediaFile) {
        this.messageMediaFile = messageMediaFile;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public LocalDateTime getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(LocalDateTime messageDate) {
        this.messageDate = messageDate;
    }
}
