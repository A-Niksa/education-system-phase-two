package server.network.clienthandling.logicutils.coursewares;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.comparators.coursewarecomparators.HomeworkThumbnailDTOComparator;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.coursewares.Homework;
import shareables.network.DTOs.coursewares.HomeworkThumbnailDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeworkThumbnailUtils {
    private static HomeworkThumbnailDTOComparator homeworkThumbnailDTOComparator;
    static {
        homeworkThumbnailDTOComparator = new HomeworkThumbnailDTOComparator();
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
}
