package server.network.clienthandling.logicutils.coursewares.homeworks;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.comparators.coursewarecomparators.HomeworkThumbnailDTOComparator;
import server.network.clienthandling.logicutils.comparators.coursewarecomparators.SubmissionThumbnailDTOComparator;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.coursewares.Homework;
import shareables.models.pojos.coursewares.HomeworkSubmission;
import shareables.network.DTOs.coursewares.HomeworkThumbnailDTO;
import shareables.network.DTOs.coursewares.SubmissionThumbnailDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeworkThumbnailUtils {
    private static HomeworkThumbnailDTOComparator homeworkThumbnailDTOComparator;
    private static SubmissionThumbnailDTOComparator submissionThumbnailDTOComparator;
    static {
        homeworkThumbnailDTOComparator = new HomeworkThumbnailDTOComparator();
        submissionThumbnailDTOComparator = new SubmissionThumbnailDTOComparator();
    }

    public static List<HomeworkThumbnailDTO> getCourseHomeworkThumbnailDTOs(DatabaseManager databaseManager, String courseId) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);

        return course.getCoursewareManager().getHomeworks().stream()
                .map(HomeworkThumbnailUtils::initializeHomeworkThumbnailDTO)
                .sorted(homeworkThumbnailDTOComparator)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static HomeworkThumbnailDTO initializeHomeworkThumbnailDTO(Homework homework) {
        HomeworkThumbnailDTO homeworkThumbnailDTO = new HomeworkThumbnailDTO();
        homeworkThumbnailDTO.setId(homework.getId());
        homeworkThumbnailDTO.setTitle(homework.getTitle());
        homeworkThumbnailDTO.setStartingDate(homework.getStartingTime());
        homeworkThumbnailDTO.setPreliminaryDeadlineDate(homework.getEndingTime());
        homeworkThumbnailDTO.setSharpDeadlineDate(homework.getPermissibleSubmittingTime());
        return homeworkThumbnailDTO;
    }

    public static List<SubmissionThumbnailDTO> getSubmissionThumbnailDTOs(DatabaseManager databaseManager, String courseId,
                                                                          String homeworkId) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);
        Homework homework = HomeworkViewUtils.getHomework(course.getCoursewareManager(), homeworkId);

        return homework.getHomeworkSubmissions().stream()
                .map(HomeworkThumbnailUtils::initializeSubmissionThumbnailDTO)
                .sorted(submissionThumbnailDTOComparator)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static SubmissionThumbnailDTO initializeSubmissionThumbnailDTO(HomeworkSubmission submission) {
        SubmissionThumbnailDTO submissionThumbnailDTO = new SubmissionThumbnailDTO();

        submissionThumbnailDTO.setId(submission.getId());
        submissionThumbnailDTO.setStudentId(submission.getOwnerId());
        submissionThumbnailDTO.setUploadedAt(submission.getDate());
        submissionThumbnailDTO.setScore(submission.getScore());

        return submissionThumbnailDTO;
    }
}