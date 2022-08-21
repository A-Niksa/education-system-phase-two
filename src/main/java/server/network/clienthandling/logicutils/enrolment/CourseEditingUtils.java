package server.network.clienthandling.logicutils.enrolment;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import server.network.clienthandling.logicutils.unitselection.acquisition.errorutils.SelectionErrorUtils;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.users.professors.Professor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        List<String> allProfessorIds = getIdentifiableIds(professors);
        List<String> teachingProfessorIdsList = getDepartmentProfessorIdsByNames(databaseManager, newTeachingProfessorNames,
                departmentId);

        return teachingProfessorIdsList.parallelStream().allMatch(allProfessorIds::contains) &&
                teachingProfessorIdsList.size() == newTeachingProfessorNames.length;
    }

    public static boolean coursesExist(DatabaseManager databaseManager, String[] courseIds) {
        if (courseIds.length == 1 &&
                courseIds[0].equals("")) {
            return true; // escaping the condition if the array is empty
        }

        List<Identifiable> courses = databaseManager.getIdentifiables(DatasetIdentifier.COURSES);

        List<String> allCourseIds = getIdentifiableIds(courses);
        List<String> allShortenedCourseIds = allCourseIds.stream()
                .map(SelectionErrorUtils::getCourseIdBarTermAndGroupIdentifiers)
                .collect(Collectors.toCollection(ArrayList::new));

        List<String> courseIdsList = Arrays.asList(courseIds);
        return courseIdsList.parallelStream()
                .allMatch(allShortenedCourseIds::contains);
    }

    public static boolean studentsExist(DatabaseManager databaseManager, String[] studentIds) {
        if (studentIds.length == 1 &&
                studentIds[0].equals("")) {
            return true; // escaping the condition if the array is empty
        }

        List<Identifiable> students = databaseManager.getIdentifiables(DatasetIdentifier.STUDENTS);
        List<String> allStudentIds = getIdentifiableIds(students);
        List<String> studentIdsList = Arrays.asList(studentIds);
        return studentIdsList.parallelStream()
                .allMatch(allStudentIds::contains);
    }

    private static List<String> getIdentifiableIds(List<Identifiable> identifiables) {
        return identifiables.stream()
                .map(e -> e.getId())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static boolean professorIsInProfessorsArray(String professorName, String[] professorNames) {
        return Arrays.asList(professorNames).contains(professorName);
    }

    public static void changeTeachingProfessors(DatabaseManager databaseManager, Course course,
                                                String[] newTeachingProfessorNames, String departmentId) {
        List<String> newTeachingProfessorIds = getDepartmentProfessorIdsByNames(databaseManager, newTeachingProfessorNames,
                departmentId);
        course.setTeachingProfessorIds(newTeachingProfessorIds);
    }

    public static List<String> getDepartmentProfessorIdsByNames(DatabaseManager databaseManager, String[] professorNames,
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
