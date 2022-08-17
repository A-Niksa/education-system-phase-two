package shareables.models.pojos.notifications;

public enum NotificationIdentifier {
    MESSAGE_REQUEST("Messaging Request"),
    COURSE_ACQUISITION_REQUEST("Course Acquisition Request");

    private String notificationIdentifierString;

    NotificationIdentifier(String notificationIdentifierString) {
        this.notificationIdentifierString = notificationIdentifierString;
    }

    @Override
    public String toString() {
        return notificationIdentifierString;
    }
}
