package server.network.clienthandling.logicutils.coursewares.homeworks;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.coursewares.CoursewareManager;
import shareables.models.pojos.coursewares.Homework;
import shareables.models.pojos.coursewares.SubmissionType;
import shareables.models.pojos.media.MediaFile;

public class HomeworkViewUtils {
    public static String getHomeworkDescription(DatabaseManager databaseManager, String courseId, String homeworkId) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);
        Homework homework = getHomework(course.getCoursewareManager(), homeworkId);
        return homework.getDescription();
    }

    public static MediaFile getHomeworkPDF(DatabaseManager databaseManager, String courseId, String homeworkId) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);
        Homework homework = getHomework(course.getCoursewareManager(), homeworkId);
        return homework.getMediaFile();
    }

    public static SubmissionType getSubmissionType(DatabaseManager databaseManager, String courseId, String homeworkId) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);
        Homework homework = getHomework(course.getCoursewareManager(), homeworkId);
        return homework.getSubmissionType();
    }

    public static Homework getHomework(CoursewareManager coursewareManager, String homeworkId) {
        return coursewareManager.getHomeworks().stream()
                .filter(homework -> homework.getId().equals(homeworkId))
                .findAny().orElse(null);
    }
}
