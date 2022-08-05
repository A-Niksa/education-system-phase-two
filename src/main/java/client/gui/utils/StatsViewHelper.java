package client.gui.utils;

import shareables.network.DTOs.CourseStatsDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

public class StatsViewHelper {
    public static String getStatsDialogMessage(ConfigFileIdentifier configIdentifier, CourseStatsDTO courseStatsDTO) {
        int numberOfPassingStudents = courseStatsDTO.getNumberOfPassingStudents();
        int numberOfFailingStudents = courseStatsDTO.getNumberOfFailingStudents();
        String courseAverageScore = formatScore(courseStatsDTO.getCourseAverageScore());
        String courseAverageScoreWithoutFailingStudent = formatScore(courseStatsDTO.getCourseAverageScoreWithoutFailingStudents());

        String numberOfPassingStudentsPrompt = ConfigManager.getString(configIdentifier,
                "numberOfPassingStudentsPrompt");
        String numberOfFailingStudentsPrompt = ConfigManager.getString(configIdentifier,
                "numberOfFailingStudentsPrompt");
        String courseAverageScorePrompt = ConfigManager.getString(configIdentifier,
                "courseAverageScorePrompt");
        String courseAverageScoreWithoutFailingStudentsPrompt = ConfigManager.getString(configIdentifier,
                "courseAverageScoreWithoutFailingStudentsPrompt");

        String dialogMessage = numberOfPassingStudentsPrompt + numberOfPassingStudents + "\n" +
                numberOfFailingStudentsPrompt + numberOfFailingStudents + "\n" +
                courseAverageScorePrompt + courseAverageScore + "\n" +
                courseAverageScoreWithoutFailingStudentsPrompt + courseAverageScoreWithoutFailingStudent;

        return dialogMessage;
    }

    private static String formatScore(double score) {
        if (score == -1.0) {
            return "N/A";
        } else {
            return String.format("%.2f", score);
        }
    }
}
