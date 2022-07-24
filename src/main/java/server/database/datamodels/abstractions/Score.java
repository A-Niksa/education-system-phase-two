package server.database.datamodels.abstractions;

import javax.persistence.*;

@Entity
public class Score {
    @Id
    private double score;
    @Column
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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
