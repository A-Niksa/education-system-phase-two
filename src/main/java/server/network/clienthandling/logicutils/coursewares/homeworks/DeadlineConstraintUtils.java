package server.network.clienthandling.logicutils.coursewares.homeworks;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.coursewares.Homework;

import java.time.LocalDateTime;

public class DeadlineConstraintUtils {
    public static boolean homeworkHasNotBeenStarted(DatabaseManager databaseManager, String courseId, String homeworkId) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);
        Homework homework = HomeworkViewUtils.getHomework(course.getCoursewareManager(), homeworkId);

        LocalDateTime currentDate = LocalDateTime.now();
        return currentDate.compareTo(homework.getStartingTime()) < 0;
    }

    public static boolean sharpDeadlineHasBeenPassed(DatabaseManager databaseManager, String courseId, String homeworkId) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);
        Homework homework = HomeworkViewUtils.getHomework(course.getCoursewareManager(), homeworkId);

        LocalDateTime currentDate = LocalDateTime.now();
        return currentDate.compareTo(homework.getPermissibleSubmittingTime()) > 0;
    }
}
