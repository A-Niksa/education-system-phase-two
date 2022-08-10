package server.network.clienthandling.logicutils.standing;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.comparators.CourseScoreDTOComparator;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.abstractions.Score;
import shareables.models.pojos.abstractions.Transcript;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.CourseScoreDTO;
import shareables.network.DTOs.TranscriptDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StandingViewUtils {
    private static CourseScoreDTOComparator courseScoreDTOComparator = new CourseScoreDTOComparator();

    public static TranscriptDTO getStudentTranscriptDTOWithId(DatabaseManager databaseManager, String studentId) {
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, studentId);
        return initializeTranscriptDTO(databaseManager, student);
    }

    public static TranscriptDTO getStudentTranscriptDTOWithName(DatabaseManager databaseManager, String departmentId,
                                                                String studentName) {
        Student student = getStudent(databaseManager, departmentId, studentName);
        return initializeTranscriptDTO(databaseManager, student);
    }

    private static TranscriptDTO initializeTranscriptDTO(DatabaseManager databaseManager, Student student) {
        Transcript transcript = student.getTranscript();
        TranscriptDTO transcriptDTO = new TranscriptDTO();
        int numberOfPassedCredits = getNumberOfPassedCredits(databaseManager, transcript.getCourseIdScoreMap());
        transcriptDTO.setNumberOfPassedCredits(numberOfPassedCredits);
        transcriptDTO.setGPAString(transcript.fetchGPAString());
        return transcriptDTO;
    }

    private static Student getStudent(DatabaseManager databaseManager, String departmentId,
                                      String studentName) {
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        return department.getStudentIds().stream()
                .map(e -> IdentifiableFetchingUtils.getStudent(databaseManager, e))
                .filter(student -> student.fetchName().equals(studentName))
                .findAny().orElse(null);
    }

    private static int getNumberOfPassedCredits(DatabaseManager databaseManager, Map<String, Score> courseIdScoreMap) {
        double minimumPassingScore = ConfigManager.getDouble(ConfigFileIdentifier.CONSTANTS, "minimumPassingScore");
        return courseIdScoreMap.entrySet().stream()
                .filter(e -> e.getValue().isFinalized())
                .filter(e -> e.getValue().getScore() >= minimumPassingScore)
                .mapToInt(e -> getCourseNumberOfCredits(databaseManager, e.getKey()))
                .sum();
    }

    public static int getCourseNumberOfCredits(DatabaseManager databaseManager, String courseId) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);
        return course.getNumberOfCredits();
    }

    public static List<CourseScoreDTO> getStudentCourseScoreDTOsWithId(DatabaseManager databaseManager, String studentId) {
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, studentId);
        return getStudentCourseScoreDTOs(databaseManager, student);
    }

    public static List<CourseScoreDTO> getStudentCourseScoreDTOsWithName(DatabaseManager databaseManager, String departmentId,
                                                                         String studentName) {
        Student student = getStudent(databaseManager, departmentId, studentName);
        return getStudentCourseScoreDTOs(databaseManager, student);
    }

    private static List<CourseScoreDTO> getStudentCourseScoreDTOs(DatabaseManager databaseManager, Student student) {
        Map<String, Score> courseIdScoreMap = student.getTranscript().getCourseIdScoreMap();
        List<CourseScoreDTO> courseScoreDTOs = new ArrayList<>();
        String studentId = student.getId();
        String studentName = student.fetchName();
        courseIdScoreMap.entrySet()
                .forEach(e -> {
                    Course course = IdentifiableFetchingUtils.getCourse(databaseManager, e.getKey());
                    CourseScoreDTO courseScoreDTO = new CourseScoreDTO();
                    courseScoreDTO.setCourseId(course.getId());
                    courseScoreDTO.setCourseName(course.getCourseName());
                    courseScoreDTO.setScoreString(e.getValue().fetchScoreString());
                    courseScoreDTO.setStudentId(studentId);
                    courseScoreDTO.setStudentName(studentName);
                    courseScoreDTOs.add(courseScoreDTO);
                });
        courseScoreDTOs.sort(courseScoreDTOComparator);
        return courseScoreDTOs;
    }

    public static List<CourseScoreDTO> getStudentTemporaryCourseScoreDTOs(DatabaseManager databaseManager, String studentId) {
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, studentId);
        Map<String, Score> courseIdScoreMap = student.getTranscript().getCourseIdScoreMap();
        List<CourseScoreDTO> courseScoreDTOs = new ArrayList<>();
        courseIdScoreMap.entrySet().stream()
                .filter(e -> !e.getValue().isFinalized())
                .forEach(e -> {
                    Course course = IdentifiableFetchingUtils.getCourse(databaseManager, e.getKey());
                    CourseScoreDTO courseScoreDTO = new CourseScoreDTO();
                    courseScoreDTO.setCourseId(course.getId());
                    courseScoreDTO.setCourseName(course.getCourseName());
                    courseScoreDTO.setScore(e.getValue().getScore());
                    courseScoreDTO.setStudentProtest(e.getValue().getStudentProtest());
                    courseScoreDTO.setProfessorResponse(e.getValue().getProfessorResponse());
                    courseScoreDTOs.add(courseScoreDTO);
                });
        courseScoreDTOs.sort(courseScoreDTOComparator);
        return courseScoreDTOs;
    }

    public static void submitProtest(DatabaseManager databaseManager, String protestingStudentId, String courseId,
                                     String protest) {
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, protestingStudentId);
        Score studentScoreInCourse = getStudentCourseScore(student.getTranscript().getCourseIdScoreMap(), courseId);
        studentScoreInCourse.setStudentProtest(protest);
    }

    private static Score getStudentCourseScore(Map<String, Score> courseIdScoreMap, String courseId) {
        return courseIdScoreMap.entrySet().stream()
                .filter(e -> e.getKey().equals(courseId) &&
                                !e.getValue().isFinalized())
                .map(Map.Entry::getValue)
                .findAny().orElse(null);
    }
}
