package server.network.clienthandling.logicutils;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.CourseDTO;

import java.util.ArrayList;
import java.util.List;

public class WeeklyScheduleUtils {
    public static List<CourseDTO> getStudentCourseDTOs(DatabaseManager databaseManager, String username) {
        List<Identifiable> courses = databaseManager.getIdentifiables(DatasetIdentifier.COURSES);
        List<CourseDTO> courseDTOs = new ArrayList<>();
        courses.parallelStream()
                .filter(e -> studentIsEnrolledInCourse(username, (Course) e))
                .forEach(e -> {
                    Course course = (Course) e;
                    CourseDTO courseDTO = new CourseDTO(course.getCourseName(),
                            getCompressedNamesOfProfessors(course.getTeachingProfessors()),
                            course.getWeeklyClassTimes(),
                            course.getExamDate());
                    courseDTOs.add(courseDTO);
                });
        return courseDTOs;
    }

    public static List<CourseDTO> getProfessorCourseDTOs(DatabaseManager databaseManager, String username) {
        List<Identifiable> courses = databaseManager.getIdentifiables(DatasetIdentifier.COURSES);
        List<CourseDTO> courseDTOs = new ArrayList<>();
        courses.parallelStream()
                .filter(e -> professorIsTeachingCourse(username, (Course) e))
                .forEach(e -> {
                    Course course = (Course) e;
                    CourseDTO courseDTO = new CourseDTO(course.getCourseName(),
                            getCompressedNamesOfProfessors(course.getTeachingProfessors()),
                            course.getWeeklyClassTimes(),
                            course.getExamDate());
                    courseDTOs.add(courseDTO);
                });
        return courseDTOs;
    }

    private static boolean professorIsTeachingCourse(String username, Course course) {
        return course.getTeachingProfessors().stream().anyMatch(e -> e.getId().equals(username));
    }

    private static String getCompressedNamesOfProfessors(List<Professor> professors) {
        StringBuilder stringBuilder = new StringBuilder();
        professors.stream().forEach(e -> getName(stringBuilder, e).append(", "));
        return stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length() - 1).toString();
    }

    private static StringBuilder getName(StringBuilder stringBuilder, Professor professor) {
        return stringBuilder.append(professor.getFirstName()).append(" ").append(professor.getLastName());
    }

    private static boolean studentIsEnrolledInCourse(String username, Course course) {
        return course.getStudents().parallelStream().anyMatch(e -> e.getId().equals(username));
    }
}
