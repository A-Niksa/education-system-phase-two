package server.network.clienthandling.logicutils.general;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.login.LoginUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.Student;

public class IdentifiableFetchingUtils {
    public static User getUser(DatabaseManager databaseManager, String studentId) {
        return LoginUtils.getUser(databaseManager, studentId);
    }

    public static Student getStudent(DatabaseManager databaseManager, String studentId) {
        return (Student) getUser(databaseManager, studentId);
    }

    public static Professor getProfessor(DatabaseManager databaseManager, String professorId) {
        return (Professor) getUser(databaseManager, professorId);
    }

    public static String getDepartmentDeputyId(DatabaseManager databaseManager, String departmentId) {
        return getDepartment(databaseManager, departmentId).getDeputyId();
    }

    public static Department getDepartment(DatabaseManager databaseManager, String departmentId) {
        return (Department) databaseManager.get(DatasetIdentifier.DEPARTMENTS, departmentId);
    }

    public static Course getCourse(DatabaseManager databaseManager, String courseId) {
        return (Course) databaseManager.get(DatasetIdentifier.COURSES, courseId);
    }

    public static String getName(DatabaseManager databaseManager, String userId) {
        User user = getUser(databaseManager, userId);
        return user.fetchName();
    }
}
