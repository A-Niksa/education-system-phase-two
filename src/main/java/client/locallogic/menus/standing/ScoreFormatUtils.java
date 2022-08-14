package client.locallogic.menus.standing;

public class ScoreFormatUtils {
    public static boolean isInValidRange(Double score) {
        boolean isEqualOrGreaterThanZero = score >= 0;
        boolean isEqualOrSmallerThanTwenty = score <= 20;
        return isEqualOrGreaterThanZero && isEqualOrSmallerThanTwenty;
    }

    public static double roundToTheNearestQuarter(Double score) {
        return Math.round(4 * score) / 4.0; // '/ 4.0' instead of '/ 4' since Math.round() returns long
    }

    public static String getScoreString(Double score) {
        return score == -1.0 ? "N/A" : String.valueOf(score);
    }
}
