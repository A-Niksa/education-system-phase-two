package shareables.models.pojos.notifications;

public enum NotificationStatus {
    SUBMITTED("Submitted"),
    ACCEPTED("Accepted"),
    DECLINED("Declined");

    private String notificationStatusString;

    NotificationStatus(String notificationStatusString) {
        this.notificationStatusString = notificationStatusString;
    }

    @Override
    public String toString() {
        return notificationStatusString;
    }
}
