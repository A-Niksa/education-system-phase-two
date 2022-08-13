package shareables.models.pojos.notifications;

import shareables.models.idgeneration.IdentifiableWithTime;

import java.time.LocalDateTime;

public class Notification extends IdentifiableWithTime {
    private LocalDateTime notificationDate;
    private boolean isSentByUser;
    private String senderId;
    private String receiverId;
    private boolean isRequest;
    private NotificationIdentifier notificationIdentifier;
    private String notificationText;
    private NotificationStatus notificationStatus;

    public Notification() {
        notificationStatus = NotificationStatus.SUBMITTED;
        notificationDate = LocalDateTime.now();
        initializeId();
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

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
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

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }
}
