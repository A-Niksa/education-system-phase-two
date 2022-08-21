package server.network.clienthandling.logicutils.unitselection.validation;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.coursewares.general.EnrolmentNotificationManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import server.network.clienthandling.logicutils.unitselection.acquisition.errorutils.SelectionErrorUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.unitselection.StudentSelectionLog;
import shareables.models.pojos.unitselection.UnitSelectionSession;
import shareables.models.pojos.users.students.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationTool {
    public void validate(DatabaseManager databaseManager, UnitSelectionSession earliestSession) {
        earliestSession.getStudentSelectionLogs().parallelStream()
                .forEach(selectionLog -> validateSelectionLog(databaseManager, selectionLog));
    }

    private void validateSelectionLog(DatabaseManager databaseManager, StudentSelectionLog studentSelectionLog) {
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, studentSelectionLog.getStudentId());
        List<Course> acquiredCourses = getAcquiredCourses(databaseManager, studentSelectionLog);

        for (int i = 0; i < acquiredCourses.size(); i++) {
            if (!isAcquisitionValid(acquiredCourses, i, student)) {
                acquiredCourses.remove(i);
                i = -1;
            }
        }

        List<String> acquiredCourseIds = acquiredCourses.stream()
                .map(Course::getId)
                .collect(Collectors.toCollection(ArrayList::new));

        studentSelectionLog.setAcquiredCoursesIds(acquiredCourseIds);
    }

    private boolean isAcquisitionValid(List<Course> acquiredCourses, int courseIndex, Student student) {
        Course course = acquiredCourses.get(courseIndex);

        return studentHavePrerequisites(course, student) &&
                isCorequisiteConditionSatisfied(acquiredCourses, course);
    }

    private boolean isCorequisiteConditionSatisfied(List<Course> acquiredCourses, Course course) {
        List<String> courseCorequisiteIds = course.getCorequisiteIds();
        List<String> acquiredCourseIds = acquiredCourses.stream()
                .map(Course::getId)
                .map(SelectionErrorUtils::getCourseIdBarTermAndGroupIdentifiers)
                .collect(Collectors.toCollection(ArrayList::new));

        if (courseCorequisiteIds.isEmpty()) return true;

        return acquiredCourseIds.containsAll(courseCorequisiteIds);
    }

    private boolean studentHavePrerequisites(Course course, Student student) {
        return SelectionErrorUtils.studentHasCoursePrerequisites(course, student);
    }

    private List<Course> getAcquiredCourses(DatabaseManager databaseManager, StudentSelectionLog studentSelectionLog) {
        return studentSelectionLog.getAcquiredCoursesIds().stream()
                .map(id -> IdentifiableFetchingUtils.getCourse(databaseManager, id))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void addStudentToAcquiredCourses(DatabaseManager databaseManager, StudentSelectionLog selectionLog) {
        String studentId = selectionLog.getStudentId();
        selectionLog.getAcquiredCoursesIds().stream()
                .map(id -> IdentifiableFetchingUtils.getCourse(databaseManager, id))
                .filter(course -> !course.getStudentIds().contains(studentId))
                .forEach(course -> {
                    course.addToStudentIds(studentId);
                    course.setActive(true);
                });
    }

    public void sendEnrolmentNotification(DatabaseManager databaseManager, StudentSelectionLog selectionLog) {
        String studentId = selectionLog.getStudentId();
        selectionLog.getAcquiredCoursesIds().stream()
                .forEach(courseId -> {
                    EnrolmentNotificationManager.sendStudentEnrolmentNotification(databaseManager, studentId, courseId);
                });
    }
}
