package server.network.clienthandling.logicutils.unitselection.thumbnails;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.unitselection.StudentSelectionLog;
import shareables.models.pojos.unitselection.UnitSelectionSession;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.unitselection.CourseThumbnailDTO;
import shareables.utils.timing.timekeeping.WeekTime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static server.network.clienthandling.logicutils.unitselection.acquisition.errorutils.SelectionErrorUtils.courseHasCapacity;
import static server.network.clienthandling.logicutils.unitselection.acquisition.errorutils.SelectionErrorUtils.studentHasCoursePrerequisites;

public class CourseThumbnailUtils {
    public static CourseThumbnailDTO initializeCourseThumbnailDTO(DatabaseManager databaseManager, Course course,
                                                                   Student student, UnitSelectionSession unitSelectionSession) {
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

        courseThumbnailDTO.setCurrentCapacity(getRemainingCapacity(course, unitSelectionSession));

        courseThumbnailDTO.setTeachingProfessorNames(getTeachingProfessorNames(databaseManager, course));

        StudentSelectionLog studentSelectionLog = getStudentSelectionLog(student.getId(), unitSelectionSession);
        courseThumbnailDTO.setAcquired(getAcquisitionStatus(studentSelectionLog, unitSelectionSession, course.getId()));
        courseThumbnailDTO.setPinnedToFavorites(getFavouritePinningStatus(
                studentSelectionLog, unitSelectionSession, course.getId()
        ));

        courseThumbnailDTO.setRecommended(getRecommendationStatus(
                databaseManager, studentSelectionLog, unitSelectionSession, course, student
        ));

        return courseThumbnailDTO;
    }

    public static boolean getRecommendationStatus(DatabaseManager databaseManager, StudentSelectionLog studentSelectionLog,
                                                   UnitSelectionSession unitSelectionSession, Course course, Student student) {

        return course.getDegreeLevel() == student.getDegreeLevel() &&
                studentHasCoursePrerequisites(course, student) &&
                courseHasCapacity(course, unitSelectionSession);
    }

    public static int getRemainingCapacity(Course course, UnitSelectionSession unitSelectionSession) {
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

    private static boolean getFavouritePinningStatus(StudentSelectionLog studentSelectionLog, UnitSelectionSession unitSelectionSession,
                                                     String courseId) {
        if (studentSelectionLog == null) return false;

        return studentSelectionLog.getFavoriteCoursesIds().contains(courseId);
    }

    public static List<String> getTeachingProfessorNames(DatabaseManager databaseManager, Course course) {
        return course.getTeachingProfessorIds().stream()
                .map(id -> IdentifiableFetchingUtils.getProfessor(databaseManager, id))
                .map(User::fetchName)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<String> getTeachingAssistantNames(DatabaseManager databaseManager, Course course) {
        return course.getTeachingAssistantIds().stream()
                .map(id -> IdentifiableFetchingUtils.getStudent(databaseManager, id))
                .map(User::fetchName)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static StudentSelectionLog getStudentSelectionLog(String studentId, UnitSelectionSession unitSelectionSession) {
        return unitSelectionSession.getStudentSelectionLogs().stream()
                .filter(selectionLog -> selectionLog.getStudentId().equals(studentId))
                .findAny().orElse(null);
    }
}
