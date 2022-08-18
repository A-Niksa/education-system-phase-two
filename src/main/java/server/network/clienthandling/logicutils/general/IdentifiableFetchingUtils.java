package server.network.clienthandling.logicutils.general;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.login.LoginUtils;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.specialusers.Admin;
import shareables.models.pojos.users.specialusers.MrMohseni;
import shareables.models.pojos.users.students.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IdentifiableFetchingUtils {
    public static User getUser(DatabaseManager databaseManager, String userId) {
        return LoginUtils.getUser(databaseManager, userId);
    }

    public static Admin getAdmin(DatabaseManager databaseManager, String adminId) {
        return (Admin) getUser(databaseManager, adminId);
    }

    public static MrMohseni getMrMohseni(DatabaseManager databaseManager, String mrMohseniId) {
        return (MrMohseni) getUser(databaseManager, mrMohseniId);
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

    public static List<Course> getCoursesWithPureId(DatabaseManager databaseManager, String pureCourseId) {
        List<Identifiable> courses = databaseManager.getIdentifiables(DatasetIdentifier.COURSES);
        return courses.stream()
                .map(identifiable -> (Course) identifiable)
                .filter(course -> course.getId().startsWith(pureCourseId))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static String getName(DatabaseManager databaseManager, String userId) {
        User user = getUser(databaseManager, userId);
        return user.fetchName();
    }
}
