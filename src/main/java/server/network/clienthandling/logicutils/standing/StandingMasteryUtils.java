package server.network.clienthandling.logicutils.standing;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.comparators.CourseScoreDTOComparator;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.abstractions.DepartmentName;
import shareables.models.pojos.abstractions.Score;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.CourseScoreDTO;
import shareables.network.DTOs.CourseStatsDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StandingMasteryUtils {
    private static CourseScoreDTOComparator courseScoreDTOComparator = new CourseScoreDTOComparator();

    public static List<CourseScoreDTO> getCourseScoreDTOsForProfessor(DatabaseManager databaseManager, String departmentId,
                                                                      String professorName) {
        String professorId = getProfessorId(databaseManager, departmentId, professorName);
        List<Course> professorCourses = getProfessorCourses(databaseManager, professorId);
        List<CourseScoreDTO> courseScoreDTOs = new ArrayList<>();
        professorCourses.forEach(course -> {
            courseScoreDTOs.addAll(StandingManagementUtils.getCourseScoreDTOsForCourse(databaseManager, departmentId,
                    course.getCourseName()));
        });
        courseScoreDTOs.sort(courseScoreDTOComparator);
        return courseScoreDTOs;
    }

    private static String getProfessorId(DatabaseManager databaseManager, String departmentId, String professorName) {
        List<String> departmentProfessorIds = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId)
                .getProfessorIds();
        return departmentProfessorIds.stream()
                .map(e -> IdentifiableFetchingUtils.getProfessor(databaseManager, e))
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
                    courseScoreDTO.setStudentName(student.fetchName());
                    courseScoreDTOs.add(courseScoreDTO);
                });
        courseScoreDTOs.sort(courseScoreDTOComparator);
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
        Collections.sort(courseNames);
        return courseNames.toArray(new String[0]);
    }

    public static String[] getDepartmentProfessorNames(DatabaseManager databaseManager, String departmentId) {
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        List<String> professorNames = new ArrayList<>();
        department.getProfessorIds().stream()
                .map(e -> IdentifiableFetchingUtils.getProfessor(databaseManager, e))
                .forEach(prof -> professorNames.add(prof.fetchName()));
        Collections.sort(professorNames);
        return professorNames.toArray(new String[0]);
    }

    public static String[] getDepartmentStudentIds(DatabaseManager databaseManager, String departmentId) {
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        List<String> studentIds = department.getStudentIds();
        Collections.sort(studentIds);
        return studentIds.toArray(new String[0]);
    }

    public static String[] getDepartmentStudentNames(DatabaseManager databaseManager, String departmentId) {
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        return department.getStudentIds().stream()
                .map(e -> IdentifiableFetchingUtils.getStudent(databaseManager, e))
                .map(User::fetchName)
                .sorted()
                .toArray(String[]::new);
    }

    public static CourseStatsDTO getCourseStatsDTO(DatabaseManager databaseManager, Course course) {
        int numberOfPassingStudents = getNumberOfPassingStudents(databaseManager, course);
        int numberOfFailingStudents = getNumberOfFailingStudents(numberOfPassingStudents, course);
        double courseAverageScore = getCourseAverageScore(databaseManager, course);
        double courseAverageWithoutFailingStudents = getCourseAverageScoreWithoutFailingStudents(databaseManager, course,
                numberOfPassingStudents);
        CourseStatsDTO courseStatsDTO = new CourseStatsDTO();
        courseStatsDTO.setNumberOfPassingStudents(numberOfPassingStudents);
        courseStatsDTO.setNumberOfFailingStudents(numberOfFailingStudents);
        courseStatsDTO.setCourseAverageScore(courseAverageScore);
        courseStatsDTO.setCourseAverageScoreWithoutFailingStudents(courseAverageWithoutFailingStudents);
        return courseStatsDTO;
    }

    public static Course getCourse(DatabaseManager databaseManager, String courseName, DepartmentName departmentName) {
        String departmentId = getDepartmentId(departmentName);
        return StandingManagementUtils.getCourse(databaseManager, departmentId, courseName);
    }

    private static double getCourseAverageScoreWithoutFailingStudents(DatabaseManager databaseManager, Course course,
                                                                      int numberOfPassingStudents) {
        double averageWithoutFails = -1.0;
        double minimumPassingScore = ConfigManager.getDouble(ConfigFileIdentifier.CONSTANTS, "minimumPassingScore");
        double totalSumOfScoresWithoutFails = getCourseScoresStream(databaseManager, course)
                .mapToDouble(Score::getScore)
                .filter(score -> score >= minimumPassingScore)
                .sum();
        if (numberOfPassingStudents != 0) {
            averageWithoutFails = totalSumOfScoresWithoutFails / numberOfPassingStudents;
        }
        return averageWithoutFails;
    }

    private static double getCourseAverageScore(DatabaseManager databaseManager, Course course) {
        double average = -1.0;
        int numberOfStudents = course.getStudentIds().size();
        double totalSumOfScores = getCourseScoresStream(databaseManager, course)
                .mapToDouble(Score::getScore)
                .sum();
        if (numberOfStudents != 0) {
            average = totalSumOfScores / numberOfStudents;
        }
        return average;
    }

    private static int getNumberOfFailingStudents(int numberOfPassingStudents, Course course) {
        return course.getStudentIds().size() - numberOfPassingStudents;
    }

    private static int getNumberOfPassingStudents(DatabaseManager databaseManager, Course course) {
        double minimumPassingScore = ConfigManager.getDouble(ConfigFileIdentifier.CONSTANTS, "minimumPassingScore");
        return (int) getCourseScoresStream(databaseManager, course)
                .filter(e -> e.getScore() >= minimumPassingScore)
                .count();
    }

    private static Stream<Score> getCourseScoresStream(DatabaseManager databaseManager, Course course) {
        return course.getStudentIds().stream()
                .map(e -> IdentifiableFetchingUtils.getStudent(databaseManager, e))
                .map(Student::getTranscript)
                .flatMap(transcript -> transcript.getCourseIdScoreMap().entrySet().stream())
                .filter(entry -> entry.getKey().equals(course.getId()))
                .map(Map.Entry::getValue);
    }

    private static String getDepartmentId(DepartmentName departmentName) {
        String departmentId;
        switch (departmentName) {
            case MATHEMATICS:
                departmentId = "1";
                break;
            case PHYSICS:
                departmentId = "2";
                break;
            case ECONOMICS:
                departmentId = "3";
                break;
            case CHEMISTRY:
                departmentId = "4";
                break;
            case AEROSPACE_ENGINEERING:
                departmentId = "5";
                break;
            default:
                departmentId = null; // added for explicitness
        }
        return departmentId;
    }

    public static boolean allStudentScoresHaveBeenFinalized(DatabaseManager databaseManager, Course course) {
        int numberOfCourseStudents = course.getStudentIds().size();
        int numberOfCourseStudentsWithFinalizedScores = (int) getCourseScoresStream(databaseManager, course)
                .filter(Score::isFinalized)
                .count();
        return numberOfCourseStudents == numberOfCourseStudentsWithFinalizedScores;
    }
}