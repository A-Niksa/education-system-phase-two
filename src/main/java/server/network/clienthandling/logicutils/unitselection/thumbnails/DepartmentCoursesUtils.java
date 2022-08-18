package server.network.clienthandling.logicutils.unitselection.thumbnails;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.comparators.coursethumbnailcomparators.AlphabeticalCourseThumbnailComparator;
import server.network.clienthandling.logicutils.comparators.coursethumbnailcomparators.DegreeLevelCourseThumbnailComparator;
import server.network.clienthandling.logicutils.comparators.coursethumbnailcomparators.ExamDateCourseThumbnailComparator;
import server.network.clienthandling.logicutils.general.EnumStringMappingUtils;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import server.network.clienthandling.logicutils.unitselection.sessionaddition.UnitSelectionTimeUtils;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.unitselection.UnitSelectionSession;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.unitselection.CourseThumbnailDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static server.network.clienthandling.logicutils.unitselection.thumbnails.CourseThumbnailUtils.initializeCourseThumbnailDTO;

public class DepartmentCoursesUtils {
    private static AlphabeticalCourseThumbnailComparator alphabeticalCourseThumbnailComparator;
    private static ExamDateCourseThumbnailComparator examDateCourseThumbnailComparator;
    private static DegreeLevelCourseThumbnailComparator degreeLevelCourseThumbnailComparator;
    static {
        alphabeticalCourseThumbnailComparator = new AlphabeticalCourseThumbnailComparator();
        examDateCourseThumbnailComparator = new ExamDateCourseThumbnailComparator();
        degreeLevelCourseThumbnailComparator = new DegreeLevelCourseThumbnailComparator();
    }

    public static List<CourseThumbnailDTO> getDepartmentCourseThumbnailDTOs(DatabaseManager databaseManager,
                                                                            String departmentNameString, String studentId) {
        String departmentId = EnumStringMappingUtils.getDepartmentId(departmentNameString);
        Department targetDepartment = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, studentId);
        Department studentDepartment = IdentifiableFetchingUtils.getDepartment(databaseManager, student.getDepartmentId());

        UnitSelectionSession unitSelectionSession = UnitSelectionTimeUtils.getOngoingUnitSelectionSession(studentDepartment,
                student.getYearOfEntry(), student.getDegreeLevel());
        if (unitSelectionSession == null) return new ArrayList<>();

        return targetDepartment.getCourseIds().stream()
                .map(id -> IdentifiableFetchingUtils.getCourse(databaseManager, id))
                .map(course -> {
                    return initializeCourseThumbnailDTO(databaseManager, course, student, unitSelectionSession);
                }).collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<CourseThumbnailDTO> getDepartmentCourseThumbnailDTOsAlphabetically(DatabaseManager databaseManager,
                                                                                          String departmentNameString,
                                                                                          String studentId) {
        List<CourseThumbnailDTO> departmentCourseThumbnailDTOs = getDepartmentCourseThumbnailDTOs(databaseManager,
                departmentNameString, studentId);
        departmentCourseThumbnailDTOs.sort(alphabeticalCourseThumbnailComparator);
        return departmentCourseThumbnailDTOs;
    }

    public static List<CourseThumbnailDTO> getDepartmentCourseThumbnailDTOsInExamDateOrder(DatabaseManager databaseManager,
                                                                                           String departmentNameString,
                                                                                           String studentId) {
        List<CourseThumbnailDTO> departmentCourseThumbnailDTOs = getDepartmentCourseThumbnailDTOs(databaseManager,
                departmentNameString, studentId);
        departmentCourseThumbnailDTOs.sort(examDateCourseThumbnailComparator);
        return departmentCourseThumbnailDTOs;
    }

    public static List<CourseThumbnailDTO> getDepartmentCourseThumbnailDTOsInDegreeLevelOrder(DatabaseManager databaseManager,
                                                                                              String departmentNameString,
                                                                                              String studentId) {
        List<CourseThumbnailDTO> departmentCourseThumbnailDTOs = getDepartmentCourseThumbnailDTOs(databaseManager,
                departmentNameString, studentId);
        departmentCourseThumbnailDTOs.sort(degreeLevelCourseThumbnailComparator);
        return departmentCourseThumbnailDTOs;
    }
}
