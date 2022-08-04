package server.network.clienthandling.logicutils.standing;

import server.database.management.DatabaseManager;
import server.network.clienthandling.ClientHandler;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Course;
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
    public static TranscriptDTO getStudentTranscriptDTO(DatabaseManager databaseManager, String studentId) {
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, studentId);
        Transcript transcript = student.getTranscript();
        TranscriptDTO transcriptDTO = new TranscriptDTO();
        int numberOfPassedCredits = getNumberOfPassedCredits(databaseManager, transcript.getCourseIdScoreMap());
        transcriptDTO.setNumberOfPassedCredits(numberOfPassedCredits);
        transcriptDTO.setGPAString(transcript.fetchGPAString());
        return transcriptDTO;
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

    public static List<CourseScoreDTO> getStudentCourseScoreDTOs(DatabaseManager databaseManager, String studentId) {
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, studentId);
        Map<String, Score> courseIdScoreMap = student.getTranscript().getCourseIdScoreMap();
        List<CourseScoreDTO> courseScoreDTOs = new ArrayList<>();
        courseIdScoreMap.entrySet()
                .forEach(e -> {
                    Course course = IdentifiableFetchingUtils.getCourse(databaseManager, e.getKey());
                    CourseScoreDTO courseScoreDTO = new CourseScoreDTO();
                    courseScoreDTO.setCourseId(course.getId());
                    courseScoreDTO.setCourseName(course.getCourseName());
                    courseScoreDTO.setScoreString(e.getValue().fetchScoreString());
                    courseScoreDTOs.add(courseScoreDTO);
                });
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
