package server.network.clienthandling.logicutils.unitselection.thumbnails;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.comparators.coursethumbnailcomparators.PinnedCourseThumbnailComparator;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import server.network.clienthandling.logicutils.unitselection.acquisition.CourseAcquisitionUtils;
import server.network.clienthandling.logicutils.unitselection.addition.UnitSelectionTimeUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.unitselection.StudentSelectionLog;
import shareables.models.pojos.unitselection.UnitSelectionSession;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.unitselection.CourseThumbnailDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static server.network.clienthandling.logicutils.unitselection.acquisition.errorutils.SelectionErrorUtils.courseHasCapacity;
import static server.network.clienthandling.logicutils.unitselection.acquisition.errorutils.SelectionErrorUtils.studentHasCoursePrerequisites;

public class PinnedCoursesUtils {
    private static PinnedCourseThumbnailComparator pinnedCourseThumbnailComparator;
    static {
        pinnedCourseThumbnailComparator = new PinnedCourseThumbnailComparator();
    }

    public static List<CourseThumbnailDTO> getPinnedCourseThumbnailDTOs(DatabaseManager databaseManager, String studentId) {
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, studentId);

        UnitSelectionSession unitSelectionSession = getUnitSelectionSession(databaseManager, student);
        StudentSelectionLog studentSelectionLog = CourseAcquisitionUtils.getStudentSelectionLog(studentId,
                unitSelectionSession);

        List<CourseThumbnailDTO> pinnedCourseThumbnailDTOs = new ArrayList<>();

        pinnedCourseThumbnailDTOs.addAll(getRecommendedCourseThumbnailDTOs(databaseManager, unitSelectionSession, student));
        pinnedCourseThumbnailDTOs.addAll(getFavoriteCourseThumbnailDTOs(databaseManager, studentSelectionLog,
                unitSelectionSession, student));

        pinnedCourseThumbnailDTOs.sort(pinnedCourseThumbnailComparator);
        return pinnedCourseThumbnailDTOs;
    }

    private static List<CourseThumbnailDTO> getRecommendedCourseThumbnailDTOs(DatabaseManager databaseManager,
                                                              UnitSelectionSession unitSelectionSession, Student student) {
        List<Course> activeCourses = getActiveCourses(databaseManager);
        DegreeLevel studentDegreeLevel = student.getDegreeLevel();
        return activeCourses.stream()
                .filter(course -> course.getDegreeLevel() == studentDegreeLevel)
                .filter(course -> studentHasCoursePrerequisites(course, student))
                .filter(course -> courseHasCapacity(course, unitSelectionSession))
                .map(course -> {
                    CourseThumbnailDTO courseThumbnailDTO = CourseThumbnailUtils.initializeCourseThumbnailDTO(databaseManager,
                            course, student, unitSelectionSession);
                    courseThumbnailDTO.setRecommended(true);
                    return courseThumbnailDTO;
                }).collect(Collectors.toCollection(ArrayList::new));
    }

    private static List<Course> getActiveCourses(DatabaseManager databaseManager) {
        return databaseManager.getIdentifiables(DatasetIdentifier.COURSES).stream()
                .map(identifiable -> (Course) identifiable)
                .filter(Course::isActive)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static List<CourseThumbnailDTO> getFavoriteCourseThumbnailDTOs(DatabaseManager databaseManager,
            StudentSelectionLog studentSelectionLog, UnitSelectionSession unitSelectionSession, Student student) {

        return studentSelectionLog.getFavoriteCoursesIds().stream()
                .map(id -> IdentifiableFetchingUtils.getCourse(databaseManager, id))
                .map(course -> {
                    return CourseThumbnailUtils.initializeCourseThumbnailDTO(databaseManager, course, student,
                            unitSelectionSession);
                }).collect(Collectors.toCollection(ArrayList::new));
    }

    private static UnitSelectionSession getUnitSelectionSession(DatabaseManager databaseManager, Student student) {
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, student.getDepartmentId());

        UnitSelectionSession unitSelectionSession = UnitSelectionTimeUtils.getOngoingUnitSelectionSession(department,
                student.getYearOfEntry(), student.getDegreeLevel());

        return unitSelectionSession;
    }
}
