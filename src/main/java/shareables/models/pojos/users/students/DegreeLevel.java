package shareables.models.pojos.users.students;

public enum DegreeLevel { // can correspond to degree level as well
    UNDERGRADUATE("Undergraduate"),
    GRADUATE("Graduate"),
    PHD("PhD");

    private String degreeLevelString;

    DegreeLevel(String degreeLevelString) {
        this.degreeLevelString = degreeLevelString;
    }

    @Override
    public String toString() {
        return degreeLevelString;
    }
}
