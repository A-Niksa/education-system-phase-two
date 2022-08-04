package server.network.clienthandling.logicutils.standing;

import server.database.management.DatabaseManager;
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
        int numberOfPassedCredits = 0;
        return courseIdScoreMap.entrySet().stream()
                .filter(e -> e.getValue().isFinalized())
                .filter(e -> e.getValue().getScore() >= minimumPassingScore)
                .mapToInt(e -> getCourseNumberOfCredits(databaseManager, e.getKey()))
                .sum();
    }

    private static int getCourseNumberOfCredits(DatabaseManager databaseManager, String courseId) {
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
}
