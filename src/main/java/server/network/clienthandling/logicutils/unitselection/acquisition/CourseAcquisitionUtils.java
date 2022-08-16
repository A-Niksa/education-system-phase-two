package server.network.clienthandling.logicutils.unitselection.acquisition;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import server.network.clienthandling.logicutils.unitselection.addition.UnitSelectionTimeUtils;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.unitselection.StudentSelectionLog;
import shareables.models.pojos.unitselection.UnitSelectionSession;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.models.pojos.users.students.Student;

public class CourseAcquisitionUtils {
    public static UnitSelectionSession getOngoingUnitSelectionSession(DatabaseManager databaseManager, String studentId) {
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, studentId);

        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, student.getId());
        int yearOfEntry = student.getYearOfEntry();
        DegreeLevel degreeLevel = student.getDegreeLevel();

        return UnitSelectionTimeUtils.getOngoingUnitSelectionSession(department, yearOfEntry, degreeLevel);
    }

    public static void acquireCourse(UnitSelectionSession unitSelectionSession, String courseId, String studentId) {
        StudentSelectionLog studentSelectionLog = getStudentSelectionLog(studentId, unitSelectionSession);
        studentSelectionLog.addToAcquiredCourseIds(courseId);
    }

    public static StudentSelectionLog getStudentSelectionLog(String studentId, UnitSelectionSession unitSelectionSession) {
        StudentSelectionLog studentSelectionLog = unitSelectionSession.getStudentSelectionLogs().stream()
                .filter(selectionLog -> selectionLog.getStudentId().equals(studentId))
                .findAny().orElse(null);

        if (studentSelectionLog == null) {
            return getNewStudentSelectionLog(studentId, unitSelectionSession);
        } else {
            return studentSelectionLog;
        }
    }

    private static StudentSelectionLog getNewStudentSelectionLog(String studentId, UnitSelectionSession unitSelectionSession) {
        StudentSelectionLog studentSelectionLog = new StudentSelectionLog();
        studentSelectionLog.setStudentId(studentId);
        unitSelectionSession.addToStudentSelectionLogs(studentSelectionLog);

        return studentSelectionLog;
    }
}
