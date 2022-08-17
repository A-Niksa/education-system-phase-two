package server.network.clienthandling.logicutils.unitselection.acquisition;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.addition.CourseAdditionUtils;
import server.network.clienthandling.logicutils.comparators.coursethumbnailcomparators.GroupNumberCourseThumbnailComparator;
import server.network.clienthandling.logicutils.general.EnumStringMappingUtils;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import server.network.clienthandling.logicutils.unitselection.thumbnails.DepartmentCoursesUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.unitselection.StudentSelectionLog;
import shareables.models.pojos.unitselection.UnitSelectionSession;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.unitselection.CourseThumbnailDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseGroupUtils {
    private static GroupNumberCourseThumbnailComparator groupNumberCourseThumbnailComparator;
    static {
        groupNumberCourseThumbnailComparator = new GroupNumberCourseThumbnailComparator();
    }

    public static List<CourseThumbnailDTO> getCourseGroupsThumbnailDTOs(DatabaseManager databaseManager, String courseId,
                                                                        String studentId) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);

        String departmentNameString = EnumStringMappingUtils.getDepartmentName(course.getDepartmentId()).toString();
        List<CourseThumbnailDTO> departmentCourseThumbnailDTOs =
                DepartmentCoursesUtils.getDepartmentCourseThumbnailDTOs(databaseManager, departmentNameString, studentId);

        String courseIdBarGroupIdentifier = CourseAdditionUtils.getCourseIdBarGroupIdentifier(courseId);
        return departmentCourseThumbnailDTOs.stream()
                .filter(courseThumbnailDTO -> {
                    String potentialCourseIdBarGroupIdentifier = CourseAdditionUtils.getCourseIdBarGroupIdentifier(
                            courseThumbnailDTO.getCourseId()
                    );

                    return courseIdBarGroupIdentifier.equals(potentialCourseIdBarGroupIdentifier);
                }).sorted(groupNumberCourseThumbnailComparator)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static boolean newGroupIsTheSameAsCurrentGroup(String courseId, int newGroupNumber) {
        int groupNumber = extractGroupNumberFromCourseId(courseId);
        return groupNumber == newGroupNumber;
    }

    public static boolean courseWithGroupNumberExists(DatabaseManager databaseManager, String courseId, int newGroupNumber) {
        return getNewCourseWithGroupNumber(databaseManager, courseId, newGroupNumber) != null;
    }

    private static int extractGroupNumberFromCourseId(String courseId) {
        String[] partitionedCourseId = courseId.split("0");
        String groupIdString = partitionedCourseId[partitionedCourseId.length - 1];
        return Integer.parseInt(groupIdString);
    }

    public static void changeGroupNumber(UnitSelectionSession unitSelectionSession, String courseId, String studentId,
                                         int newGroupNumber) {
        StudentSelectionLog studentSelectionLog = CourseAcquisitionUtils.getStudentSelectionLog(studentId,
                unitSelectionSession);

        String courseIdBarGroupIdentifier = CourseAdditionUtils.getCourseIdBarGroupIdentifier(courseId);
        String newCourseId = courseIdBarGroupIdentifier + "0" + newGroupNumber; // "0" is the group number separator

        studentSelectionLog.removeFromAcquiredCourseIds(courseId);
        studentSelectionLog.addToAcquiredCourseIds(newCourseId);
    }

    public static void requestGroupChangeFromDeputy(DatabaseManager databaseManager, String courseId, String studentId,
                                                    int newGroupNumber) {
        Student requestingStudent = IdentifiableFetchingUtils.getStudent(databaseManager, studentId);

        String departmentDeputyId = IdentifiableFetchingUtils.getDepartmentDeputyId(databaseManager,
                requestingStudent.getDepartmentId());
        Professor departmentDeputy = IdentifiableFetchingUtils.getProfessor(databaseManager, departmentDeputyId);

        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);


        CourseNotificationManager.sendGroupChangeNotification(requestingStudent, departmentDeputy, course, newGroupNumber);
    }

    public static Course getNewCourseWithGroupNumber(DatabaseManager databaseManager, String courseId, int newGroupNumber) {
        String courseIdBarGroupIdentifier = CourseAdditionUtils.getCourseIdBarGroupIdentifier(courseId);
        String newCourseId = courseIdBarGroupIdentifier + "0" + newGroupNumber;

        return IdentifiableFetchingUtils.getCourse(databaseManager, newCourseId);
    }
}
