package client.locallogic.menus.coursewares;

public class ScoreFormattingUtils {
    public static String getScoreString(Double score) {
        if (score == null) {
            return "Not scored";
        } else {
            return String.format("%.2f", score);
        }
    }
}
