package server.network.clienthandling.logicutils.general;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.login.LoginUtils;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.Student;

import java.util.List;

public class IdentifiableFetchingUtils {
    public static Student getStudent(DatabaseManager databaseManager, String studentId) {
        return (Student) LoginUtils.getUser(databaseManager, studentId);
    }

    public static Professor getProfessor(DatabaseManager databaseManager, String professorId) {
        return (Professor) LoginUtils.getUser(databaseManager, professorId);
    }

    public static Professor getDepartmentDeputy(DatabaseManager databaseManager, String departmentId) {
        return getDepartment(databaseManager, departmentId).getDeputy();
    }

    public static Department getDepartment(DatabaseManager databaseManager, String departmentId) {
        return (Department) databaseManager.get(DatasetIdentifier.DEPARTMENTS, departmentId);
    }

    public static Course getCourse(DatabaseManager databaseManager, String courseId) {
        return (Course) databaseManager.get(DatasetIdentifier.COURSES, courseId);
    }
}
