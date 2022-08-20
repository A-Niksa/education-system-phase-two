package server.network.clienthandling.logicutils.coursewares;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.abstractions.Course;
import shareables.network.DTOs.unitselection.CourseThumbnailDTO;
import shareables.utils.timing.timekeeping.WeekTime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static server.network.clienthandling.logicutils.unitselection.thumbnails.CourseThumbnailUtils.getTeachingAssistantNames;
import static server.network.clienthandling.logicutils.unitselection.thumbnails.CourseThumbnailUtils.getTeachingProfessorNames;

public class CoursewaresViewUtils {
    public static List<CourseThumbnailDTO> getStudentCoursewareThumbnailDTOs(DatabaseManager databaseManager,
                                                                             String studentId) {
        List<Course> activeCourses = getStudentActiveCourses(databaseManager, studentId);

        return activeCourses.stream()
                .map(course -> initializeExtensiveCourseThumbnailDTO(databaseManager, course))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<CourseThumbnailDTO> getProfessorCoursewareThumbnailDTOs(DatabaseManager databaseManager,
                                                                             String professorId) {
        List<Course> activeCourses = getProfessorActiveCourses(databaseManager, professorId);

        return activeCourses.stream()
                .map(course -> initializeExtensiveCourseThumbnailDTO(databaseManager, course))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static List<Course> getProfessorActiveCourses(DatabaseManager databaseManager, String professorId) {
        List<Identifiable> courses = databaseManager.getIdentifiables(DatasetIdentifier.COURSES);
        return courses.stream()
                .map(identifiable -> (Course) identifiable)
                .filter(Course::isActive)
                .filter(course -> course.getTeachingProfessorIds().contains(professorId))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static List<Course> getStudentActiveCourses(DatabaseManager databaseManager, String studentId) {
        List<Identifiable> courses = databaseManager.getIdentifiables(DatasetIdentifier.COURSES);
        return courses.stream()
                .map(identifiable -> (Course) identifiable)
                .filter(Course::isActive)
                .filter(course -> course.getStudentIds().contains(studentId))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static CourseThumbnailDTO initializeExtensiveCourseThumbnailDTO(DatabaseManager databaseManager, Course course) {
        CourseThumbnailDTO courseThumbnailDTO = new CourseThumbnailDTO();
        courseThumbnailDTO.setCourseId(course.getId());
        courseThumbnailDTO.setCourseName(course.getCourseName());
        courseThumbnailDTO.setGroupNumber(course.getGroupNumber());
        courseThumbnailDTO.setDegreeLevel(course.getDegreeLevel());
        courseThumbnailDTO.setExamDate(course.getExamDate());

        List<WeekTime> weeklyClassTimes = course.getWeeklyClassTimes();

        courseThumbnailDTO.setFirstClassDateString(weeklyClassTimes.get(0).toString());
        if (weeklyClassTimes.size() > 1) {
            courseThumbnailDTO.setSecondClassDateString(weeklyClassTimes.get(1).toString());
        }

        courseThumbnailDTO.setTeachingProfessorNames(getTeachingProfessorNames(databaseManager, course));
        courseThumbnailDTO.setTeachingAssistantNames(getTeachingAssistantNames(databaseManager, course));

        return courseThumbnailDTO;
    }
}
