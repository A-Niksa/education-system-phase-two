package server.network.clienthandling.logicutils.standing;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.abstractions.Score;

import java.util.Map;

public class CalculationOfGPAUtils {
    public static double calculateGPA(DatabaseManager databaseManager, Map<String, Score> courseIdScoreMap) {
        int totalNumberOfCredits = getTotalNumberOfCredits(databaseManager, courseIdScoreMap);
        double totalSumOfScores = getTotalSumOfScores(databaseManager, courseIdScoreMap);
        if (totalNumberOfCredits == 0) {
            return -1.0;
        } else {
            return totalSumOfScores / totalNumberOfCredits;
        }
    }

    private static double getTotalSumOfScores(DatabaseManager databaseManager, Map<String, Score> courseIdScoreMap) {
        return courseIdScoreMap.entrySet().stream()
                .filter(e -> e.getValue().isFinalized())
                .mapToDouble(e -> getWeightedScore(databaseManager, e.getKey(), e.getValue().getScore()))
                .sum();
    }

    private static double getWeightedScore(DatabaseManager databaseManager, String courseId, Double score) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);
        return course.getNumberOfCredits() * score;
    }

    private static int getTotalNumberOfCredits(DatabaseManager databaseManager, Map<String, Score> courseIdScoreMap) {
        return courseIdScoreMap.entrySet().stream()
                .filter(e -> e.getValue().isFinalized())
                .mapToInt(e -> StandingViewUtils.getCourseNumberOfCredits(databaseManager, e.getKey()))
                .sum();
    }
}
