package server.network.clienthandling.logicutils.unitselection;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.unitselection.StudentSelectionLog;
import shareables.models.pojos.unitselection.UnitSelectionSession;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.unitselection.CourseThumbnailDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseThumbnailUtils {
    public static CourseThumbnailDTO initializeCourseThumbnailDTO(DatabaseManager databaseManager, Course course,
                                                                   Student student, UnitSelectionSession unitSelectionSession) {
        CourseThumbnailDTO courseThumbnailDTO = new CourseThumbnailDTO();
        courseThumbnailDTO.setCourseId(course.getId());
        courseThumbnailDTO.setCourseName(course.getCourseName());
        courseThumbnailDTO.setGroupNumber(course.getGroupNumber());
        courseThumbnailDTO.setDegreeLevel(course.getDegreeLevel());
        courseThumbnailDTO.setExamDate(course.getExamDate());

        courseThumbnailDTO.setCurrentCapacity(getRemainingCapacity(course, unitSelectionSession));

        courseThumbnailDTO.setTeachingProfessorNames(getTeachingProfessorNames(databaseManager, course));

        StudentSelectionLog studentSelectionLog = getStudentSelectionLog(databaseManager, student.getId(), unitSelectionSession);
        courseThumbnailDTO.setAcquired(getAcquisitionStatus(studentSelectionLog, unitSelectionSession, course.getId()));
        courseThumbnailDTO.setPinned(getPinningStatus(studentSelectionLog, unitSelectionSession, course.getId()));

        return courseThumbnailDTO;
    }

    private static int getRemainingCapacity(Course course, UnitSelectionSession unitSelectionSession) {
        int numberOfStudentsEnrolledInCourse = getNumberOfStudentsEnrolledInCourse(unitSelectionSession, course.getId());
        return course.getCourseCapacity() - numberOfStudentsEnrolledInCourse;
    }

    private static int getNumberOfStudentsEnrolledInCourse(UnitSelectionSession unitSelectionSession, String courseId) {
        return (int) unitSelectionSession.getStudentSelectionLogs().stream()
                .filter(selectionLog -> isStudentEnrolledInCourse(selectionLog, courseId))
                .count();
    }

    private static boolean isStudentEnrolledInCourse(StudentSelectionLog selectionLog, String courseId) {
        return selectionLog.getAcquiredCoursesIds().contains(courseId);
    }

    private static boolean getAcquisitionStatus(StudentSelectionLog studentSelectionLog,
                                                UnitSelectionSession unitSelectionSession, String courseId) {
        if (studentSelectionLog == null) return false;

        return studentSelectionLog.getAcquiredCoursesIds().contains(courseId);
    }

    private static boolean getPinningStatus(StudentSelectionLog studentSelectionLog, UnitSelectionSession unitSelectionSession,
                                            String courseId) {
        if (studentSelectionLog == null) return false;

        return studentSelectionLog.getFavoriteCoursesIds().contains(courseId);
    }

    private static List<String> getTeachingProfessorNames(DatabaseManager databaseManager, Course course) {
        return course.getTeachingProfessorIds().stream()
                .map(id -> IdentifiableFetchingUtils.getProfessor(databaseManager, id))
                .map(User::fetchName)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static StudentSelectionLog getStudentSelectionLog(DatabaseManager databaseManager, String studentId,
                                                              UnitSelectionSession unitSelectionSession) {
        return unitSelectionSession.getStudentSelectionLogs().stream()
                .filter(selectionLog -> selectionLog.getStudentId().equals(studentId))
                .findAny().orElse(null);
    }
}
