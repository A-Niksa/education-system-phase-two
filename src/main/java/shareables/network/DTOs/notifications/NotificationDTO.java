package shareables.network.DTOs.notifications;

import shareables.models.pojos.notifications.NotificationIdentifier;
import shareables.models.pojos.notifications.NotificationStatus;

import java.time.LocalDateTime;

public class NotificationDTO {
    private String id;
    private LocalDateTime notificationDate;
    private boolean isSentByUser;
    private String senderId;
    private String senderName;
    private boolean isRequest;
    private NotificationIdentifier notificationIdentifier;
    private String notificationText;
    private NotificationStatus notificationStatus;

    public NotificationDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(LocalDateTime notificationDate) {
        this.notificationDate = notificationDate;
    }

    public boolean isSentByUser() {
        return isSentByUser;
    }

    public void setSentByUser(boolean sentByUser) {
        isSentByUser = sentByUser;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public boolean isRequest() {
        return isRequest;
    }

    public void setRequest(boolean request) {
        isRequest = request;
    }

    public NotificationIdentifier getNotificationIdentifier() {
        return notificationIdentifier;
    }

    public void setNotificationIdentifier(NotificationIdentifier notificationIdentifier) {
        this.notificationIdentifier = notificationIdentifier;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    @Override
    public String toString() {
        return "<html>" +
                    id + " | " + senderId + " - " + senderName +
                    "<br/>" +
                    notificationIdentifier + ": " + notificationText +
                    "<br/>" +
                    "Status: " + notificationStatus +
                "</html>";
    }
}
