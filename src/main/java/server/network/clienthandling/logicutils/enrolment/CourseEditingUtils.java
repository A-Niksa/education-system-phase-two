package server.network.clienthandling.logicutils.enrolment;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.users.professors.Professor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourseEditingUtils {
    public static Course getCourse(DatabaseManager databaseManager, String courseId) {
        return (Course) databaseManager.get(DatasetIdentifier.COURSES, courseId);
    }

    public static void removeCourse(DatabaseManager databaseManager, String courseId) {
        removeCourseFromDepartment(databaseManager, courseId);
        removeCourseFromCoursesDataset(databaseManager, courseId);
    }

    private static void removeCourseFromDepartment(DatabaseManager databaseManager, String courseId) {
        List<Identifiable> departments = databaseManager.getIdentifiables(DatasetIdentifier.DEPARTMENTS);
        departments.parallelStream()
                .filter(e -> departmentHasCourse((Department) e, courseId))
                .forEach(e -> ((Department) e).removeFromCourseIds(courseId));
    }

    private static boolean departmentHasCourse(Department department, String courseId) {
        return department.getCourseIds()
                .stream()
                .anyMatch(e -> e.equals(courseId));
    }

    private static void removeCourseFromCoursesDataset(DatabaseManager databaseManager, String courseId) {
        databaseManager.remove(DatasetIdentifier.COURSES, courseId);
    }

    public static boolean professorsExistInDepartment(DatabaseManager databaseManager, String[] newTeachingProfessorNames,
                                                      String departmentId) {
        List<Identifiable> professors = databaseManager.getIdentifiables(DatasetIdentifier.PROFESSORS);
        return professors.parallelStream().anyMatch(e -> { // TODO: two profs with the same name?
            Professor professor = (Professor) e;
            return professorIsInProfessorsArray(professor.fetchName(), newTeachingProfessorNames) &&
                    professor.getDepartmentId().equals(departmentId);
        });
    }

    private static boolean professorIsInProfessorsArray(String professorName, String[] professorNames) {
        return Arrays.asList(professorNames).contains(professorName);
    }

    public static void changeTeachingProfessors(DatabaseManager databaseManager, Course course,
                                                String[] newTeachingProfessorNames, String departmentId) {
        List<String> newTeachingProfessorIds = getProfessorIdsByNames(databaseManager, newTeachingProfessorNames,
                departmentId);
        course.setTeachingProfessorIds(newTeachingProfessorIds);
    }

    public static List<String> getProfessorIdsByNames(DatabaseManager databaseManager, String[] professorNames,
                                                         String departmentId) {
        List<Identifiable> professors = databaseManager.getIdentifiables(DatasetIdentifier.PROFESSORS);
        List<String> selectedProfessorIds = new ArrayList<>();
        professors.parallelStream()
                .filter(e -> {
                    Professor professor = (Professor) e;
                    return professorIsInProfessorsArray(professor.fetchName(), professorNames) &&
                            professor.getDepartmentId().equals(departmentId);
                }).forEach(e -> selectedProfessorIds.add(((Professor) e).getId()));
        return selectedProfessorIds;
    }
}
