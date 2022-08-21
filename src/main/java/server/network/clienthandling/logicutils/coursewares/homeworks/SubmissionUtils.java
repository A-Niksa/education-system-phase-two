package server.network.clienthandling.logicutils.coursewares.homeworks;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.coursewares.Homework;
import shareables.models.pojos.coursewares.HomeworkSubmission;
import shareables.models.pojos.media.MediaFile;

public class SubmissionUtils {
    public static void submitTextHomework(DatabaseManager databaseManager, String studentId, String courseId,
                                          String homeworkId, String text) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);
        Homework homework = HomeworkViewUtils.getHomework(course.getCoursewareManager(), homeworkId);

        HomeworkSubmission homeworkSubmission = new HomeworkSubmission();
        homeworkSubmission.setOwnerId(studentId);
        homeworkSubmission.setText(text);

        homework.addToHomeworkSubmissions(homeworkSubmission);
    }

    public static void submitMediaHomework(DatabaseManager databaseManager, String studentId, String courseId,
                                           String homeworkId, MediaFile mediaFile) {

        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);
        Homework homework = HomeworkViewUtils.getHomework(course.getCoursewareManager(), homeworkId);

        HomeworkSubmission homeworkSubmission = new HomeworkSubmission();
        homeworkSubmission.setOwnerId(studentId);
        homeworkSubmission.setMediaFile(mediaFile);

        homework.addToHomeworkSubmissions(homeworkSubmission);
    }

    public static String getSubmissionText(DatabaseManager databaseManager, String courseId, String homeworkId, String submissionId) {
        HomeworkSubmission homeworkSubmission = getHomeworkSubmission(databaseManager, courseId, homeworkId, submissionId);
        return homeworkSubmission.getText();
    }

    public static MediaFile getSubmissionMediaFile(DatabaseManager databaseManager, String courseId, String homeworkId,
                                                   String submissionId) {
        HomeworkSubmission homeworkSubmission = getHomeworkSubmission(databaseManager, courseId, homeworkId, submissionId);
        return homeworkSubmission.getMediaFile();
    }

    public static void scoreStudentSubmission(DatabaseManager databaseManager, String courseId, String homeworkId,
                                              String submissionId, Double score) {
        HomeworkSubmission homeworkSubmission = getHomeworkSubmission(databaseManager, courseId, homeworkId, submissionId);
        homeworkSubmission.setScore(score);
    }

    private static HomeworkSubmission getHomeworkSubmission(DatabaseManager databaseManager, String courseId, String homeworkId,
                                                            String submissionId) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);
        Homework homework = HomeworkViewUtils.getHomework(course.getCoursewareManager(), homeworkId);

        return homework.getHomeworkSubmissions().stream()
                .filter(submission -> submission.getId().equals(submissionId))
                .findAny().orElse(null);
    }
}
