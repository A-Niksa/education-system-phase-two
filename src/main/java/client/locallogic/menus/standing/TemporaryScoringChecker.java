package client.locallogic.menus.standing;

import java.util.HashMap;

public class TemporaryScoringChecker {
    public static boolean allStudentsHaveBeenTemporaryScores(HashMap<String, Double> studentIdTemporaryScoreMap,
                                                             int numberOfStudentsInCourse) {
        return getNumberOfTemporaryScores(studentIdTemporaryScoreMap) == numberOfStudentsInCourse;
    }

    private static int getNumberOfTemporaryScores(HashMap<String, Double> studentIdTemporaryScoreMap) {
        return (int) studentIdTemporaryScoreMap.entrySet().stream()
                .filter(e -> e.getValue() != -1.0)
                .count();
    }
}
