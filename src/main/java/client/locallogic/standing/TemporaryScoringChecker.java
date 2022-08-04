package client.locallogic.standing;

import java.util.HashMap;

public class TemporaryScoringChecker {
    public static boolean allStudentsHaveBeenTemporaryScores(HashMap<String, Double> studentIdTemporaryScoreMap,
                                                             int numberOfStudentsInCourse) {
        return studentIdTemporaryScoreMap.size() == numberOfStudentsInCourse;
    }
}
