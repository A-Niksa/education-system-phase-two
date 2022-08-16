package shareables.models.pojos.users.students;

public enum DegreeLevel { // can correspond to degree level as well
    UNDERGRADUATE("Undergraduate", 0),
    GRADUATE("Graduate", 1),
    PHD("PhD", 2);

    private String degreeLevelString;
    private int degreeLevelSortingValue;

    DegreeLevel(String degreeLevelString, int degreeLevelSortingValue) {
        this.degreeLevelString = degreeLevelString;
        this.degreeLevelSortingValue = degreeLevelSortingValue;
    }

    public int getDegreeLevelSortingValue() {
        return degreeLevelSortingValue;
    }

    @Override
    public String toString() {
        return degreeLevelString;
    }
}
