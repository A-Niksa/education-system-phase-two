package server.network.clienthandling.logicutils.standing;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.abstractions.Score;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.CourseScoreDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StandingMasteryUtils {
    public static List<CourseScoreDTO> getCourseScoreDTOsForProfessor(DatabaseManager databaseManager, String departmentId,
                                                                      String professorName) {
        String professorId = getProfessorId(databaseManager, departmentId, professorName);
        List<Course> professorCourses = getProfessorCourses(databaseManager, professorId);
        List<CourseScoreDTO> courseScoreDTOs = new ArrayList<>();
        professorCourses.forEach(course -> {
            courseScoreDTOs.addAll(StandingManagementUtils.getCourseScoreDTOsForCourse(databaseManager, departmentId,
                    course.getCourseName()));
        });
        return courseScoreDTOs;
    }

    private static String getProfessorId(DatabaseManager databaseManager, String departmentId, String professorName) {
        List<String> departmentProfessorIds = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId)
                .getProfessorIds();
        return departmentProfessorIds.stream()
                .map(e -> IdentifiableFetchingUtils.getProfessor(databaseManager, professorName))
                .filter(prof -> prof.fetchName().equals(professorName))
                .map(User::getId)
                .findAny().orElse(null);
    }

    private static List<Course> getProfessorCourses(DatabaseManager databaseManager, String professorId) {
        List<Identifiable> courses = databaseManager.getIdentifiables(DatasetIdentifier.COURSES);
        return courses.stream()
                .map(e -> (Course) e)
                .filter(course -> course.getTeachingProfessorIds().contains(professorId))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<CourseScoreDTO> getCourseScoreDTOsForStudent(DatabaseManager databaseManager, String departmentId,
                                                                         String studentId) {
        List<String> departmentStudentIds = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId)
                .getStudentIds();
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, studentId);
        List<CourseScoreDTO> courseScoreDTOs = new ArrayList<>();
        departmentStudentIds.stream()
                .filter(e -> e.equals(studentId))
                .map(e -> IdentifiableFetchingUtils.getStudent(databaseManager, e))
                .map(Student::getTranscript)
                .flatMap(e -> e.getCourseIdScoreMap().entrySet().stream())
                .forEach(e -> {
                    Course course = IdentifiableFetchingUtils.getCourse(databaseManager, e.getKey());
                    CourseScoreDTO courseScoreDTO = initializeCourseScoreDTO(course, e.getValue());
                    courseScoreDTOs.add(courseScoreDTO);
                });
        return courseScoreDTOs;
    }

    private static CourseScoreDTO initializeCourseScoreDTO(Course course, Score score) {
        CourseScoreDTO courseScoreDTO = new CourseScoreDTO();
        courseScoreDTO.setCourseId(course.getId());
        courseScoreDTO.setCourseName(course.getCourseName());
        courseScoreDTO.setStudentProtest(score.getStudentProtest());
        courseScoreDTO.setProfessorResponse(score.getProfessorResponse());
        courseScoreDTO.setScore(score.getScore());
        courseScoreDTO.setFinalized(score.isFinalized());
        return courseScoreDTO;
    }

    public static String[] getDepartmentCourseNames(DatabaseManager databaseManager, String departmentId) {
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        List<String> courseNames = new ArrayList<>();
        department.getCourseIds().stream()
                .map(e -> IdentifiableFetchingUtils.getCourse(databaseManager, e))
                .forEach(course -> courseNames.add(course.getCourseName()));
        return courseNames.toArray(new String[0]);
    }

    public static String[] getDepartmentProfessorNames(DatabaseManager databaseManager, String departmentId) {
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        List<String> professorNames = new ArrayList<>();
        department.getProfessorIds().stream()
                .map(e -> IdentifiableFetchingUtils.getProfessor(databaseManager, e))
                .forEach(prof -> professorNames.add(prof.fetchName()));
        return professorNames.toArray(new String[0]);
    }

    public static String[] getDepartmentStudentIds(DatabaseManager databaseManager, String departmentId) {
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        return department.getStudentIds()
                .toArray(new String[0]);
    }
}
