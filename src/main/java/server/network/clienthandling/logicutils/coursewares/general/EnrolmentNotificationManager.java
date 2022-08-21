package server.network.clienthandling.logicutils.coursewares.general;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.notifications.Notification;
import shareables.models.pojos.notifications.NotificationIdentifier;
import shareables.models.pojos.users.User;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

public class EnrolmentNotificationManager {
    public static void sendStudentEnrolmentNotification(DatabaseManager databaseManager, String studentId, String courseId) {
        Notification notification = new Notification();
        notification.setNotificationText(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "studentCourseEnrolmentPrompt") + courseId);
        notification.setRequest(false);
        notification.setNotificationIdentifier(NotificationIdentifier.STUDENT_COURSE_ENROLMENT);
        notification.setReceiverId(studentId);
        notification.setSentByUser(false);

        addNotificationToReceiveNotificationManager(databaseManager, studentId, notification);
    }

    public static void sendTAEnrolmentNotification(DatabaseManager databaseManager, String studentId, String courseId) {
        Notification notification = new Notification();
        notification.setNotificationText(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "teachingAssistantCourseEnrolmentPrompt") + courseId);
        notification.setRequest(false);
        notification.setNotificationIdentifier(NotificationIdentifier.TA_COURSE_ENROLMENT);
        notification.setReceiverId(studentId);
        notification.setSentByUser(false);

        addNotificationToReceiveNotificationManager(databaseManager, studentId, notification);
    }

    private static void addNotificationToReceiveNotificationManager(DatabaseManager databaseManager, String receiverId,
                                                                    Notification notification) {
        User receiver = IdentifiableFetchingUtils.getUser(databaseManager, receiverId);
        if (receiver == null) return;

        receiver.getNotificationsManager().addToNotifications(notification);
    }
}
