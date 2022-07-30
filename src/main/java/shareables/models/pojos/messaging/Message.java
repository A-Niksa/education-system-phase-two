package shareables.models.pojos.messaging;

import shareables.models.idgeneration.IdGenerator;
import shareables.models.idgeneration.IdentifiableWithTime;
import shareables.models.pojos.media.MediaFile;
import shareables.models.pojos.users.User;

import java.util.Date;

public class Message extends IdentifiableWithTime {
    private String senderId;
    private MediaFile messageMediaFile;
    private String messageText;

    public Message() {
        initializeId();
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
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
}
