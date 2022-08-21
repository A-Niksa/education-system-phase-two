package server.network.clienthandling.logicutils.coursewares.homeworks;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.coursewares.Homework;
import shareables.models.pojos.coursewares.SubmissionType;
import shareables.models.pojos.media.MediaFile;
import shareables.network.requests.Request;

import java.time.LocalDateTime;

public class HomeworkAdditionUtils {
    public static void addHomework(DatabaseManager databaseManager, Request request) {
        String courseId = (String) request.get("courseId");
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);

        Homework homework = initializeHomework(request);

        course.getCoursewareManager().addToHomeworks(homework);
    }

    private static Homework initializeHomework(Request request) {
        Homework homework = new Homework();
        homework.setSubmissionType((SubmissionType) request.get("submissionType"));
        homework.setMediaFile((MediaFile) request.get("mediaFile"));
        homework.setDescription((String) request.get("description"));
        homework.setTitle((String) request.get("homeworkTitle"));
        homework.setStartingTime((LocalDateTime) request.get("startsAt"));
        homework.setEndingTime((LocalDateTime) request.get("endsAt"));
        homework.setPermissibleSubmittingTime((LocalDateTime) request.get("sharplyEndsAt"));

        return homework;
    }
}
