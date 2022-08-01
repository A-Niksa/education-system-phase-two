package shareables.models.pojos.users.students;

public enum DegreeLevel {
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
