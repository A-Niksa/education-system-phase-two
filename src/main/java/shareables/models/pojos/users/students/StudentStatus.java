package shareables.models.pojos.users.students;

public enum StudentStatus {
    CURRENTLY_STUDYING("Currently studying"),
    GRADUATED("Graduated"),
    DROPPED_OUT("Dropped out");

    private String studentStatusString;

    private StudentStatus(String studentStatusString) {
        this.studentStatusString = studentStatusString;
    }

    public String toString() {
        return studentStatusString;
    }
}
