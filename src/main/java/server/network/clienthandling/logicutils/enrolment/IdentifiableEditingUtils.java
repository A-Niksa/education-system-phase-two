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
import java.util.stream.Stream;

public class IdentifiableEditingUtils {
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
                .forEach(e -> ((Department) e).removeFromCourses(courseId));
    }

    private static boolean departmentHasCourse(Department department, String courseId) {
        return department.getCourses()
                .stream()
                .anyMatch(e -> e.getId().equals(courseId));
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
        List<Professor> newTeachingProfessors = getProfessorsByNames(databaseManager, newTeachingProfessorNames,
                departmentId);
        course.setTeachingProfessors(newTeachingProfessors);
    }

    public static List<Professor> getProfessorsByNames(DatabaseManager databaseManager, String[] professorNames,
                                                        String departmentId) {
        List<Identifiable> professors = databaseManager.getIdentifiables(DatasetIdentifier.PROFESSORS);
        List<Professor> selectedProfessors = new ArrayList<>();
        professors.parallelStream()
                .filter(e -> {
                    Professor professor = (Professor) e;
                    return professorIsInProfessorsArray(professor.fetchName(), professorNames) &&
                            professor.getDepartmentId().equals(departmentId);
                }).forEach(e -> selectedProfessors.add((Professor) e));
        return selectedProfessors;
    }
}
