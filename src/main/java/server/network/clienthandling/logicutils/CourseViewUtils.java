package server.network.clienthandling.logicutils;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.abstractions.Course;
import shareables.network.DTOs.CourseDTO;

import java.util.ArrayList;
import java.util.List;

import static server.network.clienthandling.logicutils.WeeklyScheduleUtils.initializeCourseDTO;

public class CourseViewUtils {
    public static List<CourseDTO> getActiveCourseDTOs(DatabaseManager databaseManager) {
        List<Identifiable> courses = databaseManager.getIdentifiables(DatasetIdentifier.COURSES);
        List<CourseDTO> activeCourseDTOs = new ArrayList<>();
        courses.parallelStream()
                .filter(e -> ((Course) e).isActive())
                .forEach(e -> {
                    Course course = (Course) e;
                    CourseDTO courseDTO = initializeCourseDTO(course);
                    activeCourseDTOs.add(courseDTO);
                });
        return activeCourseDTOs;
    }
}
