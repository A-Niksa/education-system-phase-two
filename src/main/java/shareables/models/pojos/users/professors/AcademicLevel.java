package shareables.models.pojos.users.professors;

public enum AcademicLevel {
    ASSISTANT("Assistant Professor"), // ostadyar
    ASSOCIATE("Associate Professor"), // daneshyar
    FULL("Full Professor"); // tamam

    private String academicLevelString;

    AcademicLevel(String academicLevelString) {
        this.academicLevelString = academicLevelString;
    }

    public String toString() {
        return academicLevelString;
    }
}
