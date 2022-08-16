package server.network.clienthandling.logicutils.unitselection.acquisition.errorutils;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import server.network.clienthandling.logicutils.standing.StandingViewUtils;
import server.network.clienthandling.logicutils.unitselection.thumbnails.CourseThumbnailUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.unitselection.StudentSelectionLog;
import shareables.models.pojos.unitselection.UnitSelectionSession;
import shareables.models.pojos.users.students.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static server.network.clienthandling.logicutils.unitselection.acquisition.CourseAcquisitionUtils.getStudentSelectionLog;

public class SelectionErrorUtils {
    public static boolean courseHasCapacity(Course course, UnitSelectionSession unitSelectionSession) {
        return CourseThumbnailUtils.getRemainingCapacity(course, unitSelectionSession) > 0;
    }

    public static boolean studentHasCoursePrerequisites(Course course, Student student) {
        List<String> passedCourseIds = StandingViewUtils.getPassedCourseIds(student.getTranscript().getCourseIdScoreMap());
        List<String> coursePrerequisiteIds = course.getPrerequisiteIds();

        return passedCourseIds.containsAll(coursePrerequisiteIds);
    }

    public static boolean isTryingToAcquireTwoTheologyCourses(DatabaseManager databaseManager, Course course,
                                                              String studentId, UnitSelectionSession unitSelectionSession) {
        int numberOfAcquiredTheologyCourses = getNumberOfAcquiredTheologyCourses(unitSelectionSession, studentId,
                databaseManager);

        return numberOfAcquiredTheologyCourses >= 1 &&
                course.isTheologyCourse();
    }

    private static int getNumberOfAcquiredTheologyCourses(UnitSelectionSession unitSelectionSession, String studentId,
                                                          DatabaseManager databaseManager) {
        StudentSelectionLog studentSelectionLog = getStudentSelectionLog(studentId, unitSelectionSession);

        return (int) studentSelectionLog.getAcquiredCoursesIds().stream()
                .map(id -> IdentifiableFetchingUtils.getCourse(databaseManager, id))
                .filter(Course::isTheologyCourse)
                .count();
    }

    public static boolean doClassTimesCollideWithStudentClasses(DatabaseManager databaseManager, Course course,
                                                                String studentId, UnitSelectionSession unitSelectionSession) {
        StudentSelectionLog studentSelectionLog = getStudentSelectionLog(studentId, unitSelectionSession);
        List<Course> studentAcquiredCourses = getStudentAcquiredCourses(databaseManager, studentSelectionLog);

        return studentAcquiredCourses.stream()
                .anyMatch(acquiredCourse -> {
                    return TimeCollisionUtils.weeklyClassTimeListsCollide(acquiredCourse.getWeeklyClassTimes(),
                            course.getWeeklyClassTimes());
                });
    }


    public static boolean doExamDateCollideWithStudentExams(DatabaseManager databaseManager, Course course, String studentId,
                                                            UnitSelectionSession unitSelectionSession) {
        StudentSelectionLog studentSelectionLog = getStudentSelectionLog(studentId, unitSelectionSession);
        List<Course> studentAcquiredCourses = getStudentAcquiredCourses(databaseManager, studentSelectionLog);

        return studentAcquiredCourses.stream()
                .anyMatch(acquiredCourse -> {
                    return TimeCollisionUtils.examDatesCollide(acquiredCourse.getExamDate(), course.getExamDate());
                });
    }

    private static List<Course> getStudentAcquiredCourses(DatabaseManager databaseManager,
                                                          StudentSelectionLog studentSelectionLog) {
        return studentSelectionLog.getAcquiredCoursesIds().stream()
                .map(id -> IdentifiableFetchingUtils.getCourse(databaseManager, id))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
