package shareables.models.pojos.abstractions;

public class Score {
    private double score;
    private boolean isFinalized;

    public Score() {
        isFinalized = false; // default value
    }

    public Score(boolean isFinalized, double score) {
        this.isFinalized = isFinalized;
        this.score = score;
    }

    public boolean isFinalized() {
        return isFinalized;
    }

    public void setFinalized(boolean finalized) {
        isFinalized = finalized;
    }

    public String fetchScoreString() {
        return isFinalized ? String.valueOf(score) : "N/A";
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
