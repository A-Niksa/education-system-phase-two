package shareables.models.pojos.notifications;

import shareables.models.idgeneration.SequentialIdGenerator;

import java.util.ArrayList;
import java.util.List;

public class NotificationsManager {
    private String ownerId;
    private List<Notification> notifications;

    public NotificationsManager() {
        notifications = new ArrayList<>();
    }

    public NotificationsManager(String ownerId) {
        this.ownerId = ownerId;
        notifications = new ArrayList<>();
    }

    public void addToNotifications(Notification notification) {
        notifications.add(notification);
    }

    public void removeFromNotifications(String notificationId) {
        notifications.removeIf(e -> e.getId().equals(notificationId));
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
