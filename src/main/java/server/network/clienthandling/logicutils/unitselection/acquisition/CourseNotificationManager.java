package server.network.clienthandling.logicutils.unitselection.acquisition;

import server.database.management.DatabaseManager;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.notifications.Notification;
import shareables.models.pojos.notifications.NotificationIdentifier;
import shareables.models.pojos.unitselection.UnitSelectionSession;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.Student;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

public class CourseNotificationManager {
    public static void sendCourseAcquisitionNotification(Student requestingStudent, Professor receivingDeputy, Course course) {
        Notification notification = new Notification();
        notification.setNotificationIdentifier(NotificationIdentifier.COURSE_ACQUISITION_REQUEST);
        notification.setSentByUser(true);
        notification.setSenderId(requestingStudent.getId());
        notification.setReceiverId(receivingDeputy.getId());
        notification.setRequest(true);
        notification.setNotificationText(
                ConfigManager.getString(ConfigFileIdentifier.TEXTS, "letMeAcquireCourse") +
                        course.getCourseName() + " - " + course.getId()
        );

        receivingDeputy.getNotificationsManager().addToNotifications(notification);
        requestingStudent.getNotificationsManager().addToNotifications(notification);
    }

    public static void acquireCourseByDecreeOfDeputy(DatabaseManager databaseManager, Notification notification) {
        UnitSelectionSession unitSelectionSession = CourseAcquisitionUtils.getOngoingUnitSelectionSession(databaseManager,
                notification.getSenderId());

        String courseId = extractCourseIdFromCourseAcquisitionNotification(notification);

        CourseAcquisitionUtils.acquireCourse(unitSelectionSession, courseId, notification.getSenderId());
    }

    private static String extractCourseIdFromCourseAcquisitionNotification(Notification notification) {
        String notificationText = notification.getNotificationText();
        String[] partitionedNotificationText = notificationText.split(" - ");
        int partitionedNotificationLength = partitionedNotificationText.length;

        return partitionedNotificationText[partitionedNotificationLength - 1];
    }

    public static void sendGroupChangeNotification(Student requestingStudent, Professor receivingDeputy, Course course,
                                                   int newGroupNumber) {
        Notification notification = new Notification();
        notification.setNotificationIdentifier(NotificationIdentifier.GROUP_CHANGE_REQUEST);
        notification.setSentByUser(true);
        notification.setSenderId(requestingStudent.getId());
        notification.setReceiverId(receivingDeputy.getId());
        notification.setRequest(true);
        notification.setNotificationText(
                ConfigManager.getString(ConfigFileIdentifier.TEXTS, "letMeChangeGroupNumber") +
                        course.getCourseName() + " - " + course.getId() + " - " + newGroupNumber
        );

        receivingDeputy.getNotificationsManager().addToNotifications(notification);
        requestingStudent.getNotificationsManager().addToNotifications(notification);
    }

    public static void changeCourseGroupByDecreeOfDeputy(DatabaseManager databaseManager, Notification notification) {
        UnitSelectionSession unitSelectionSession = CourseAcquisitionUtils.getOngoingUnitSelectionSession(databaseManager,
                notification.getSenderId());

        String courseId = extractCourseIdFromGroupChangeNotification(notification);
        int newGroupNumber = extractNewGroupNumberFromGroupChangeNotification(notification);

        CourseGroupUtils.changeGroupNumber(unitSelectionSession, courseId, notification.getSenderId(), newGroupNumber);
    }

    private static int extractNewGroupNumberFromGroupChangeNotification(Notification notification) {
        String notificationText = notification.getNotificationText();
        String[] partitionedNotificationText = notificationText.split(" - ");
        int partitionedNotificationLength = partitionedNotificationText.length;

        return Integer.parseInt(partitionedNotificationText[partitionedNotificationLength - 1]);
    }

    private static String extractCourseIdFromGroupChangeNotification(Notification notification) {
        String notificationText = notification.getNotificationText();
        String[] partitionedNotificationText = notificationText.split(" - ");
        int partitionedNotificationLength = partitionedNotificationText.length;

        return partitionedNotificationText[partitionedNotificationLength - 2];
    }
}
