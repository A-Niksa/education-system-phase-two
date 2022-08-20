package server.network.clienthandling.logicutils.coursewares;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.users.students.Student;

public class CoursewareEnrolmentUtils {
    public static boolean studentExists(DatabaseManager databaseManager, String studentId) {
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, studentId);
        return student != null;
    }

    public static void addStudentToCourse(DatabaseManager databaseManager, String studentId, String courseId) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);
        course.addToStudentIds(studentId);

        EnrolmentNotificationManager.sendStudentEnrolmentNotification(databaseManager, studentId, courseId);
    }

    public static void addTeachingAssistantToCourse(DatabaseManager databaseManager, String teachingAssistantId,
                                                    String courseId) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);
        course.addToTeachingAssistantIds(teachingAssistantId);

        EnrolmentNotificationManager.sendTAEnrolmentNotification(databaseManager, teachingAssistantId,
                courseId);
    }
}
