package shareables.models.pojos.coursewares;

public enum SubmissionType {
    TEXT("Text"),
    MEDIA_FILE("Media File");

    private String submissionTypeString;

    SubmissionType(String submissionTypeString) {
        this.submissionTypeString = submissionTypeString;
    }

    @Override
    public String toString() {
        return submissionTypeString;
    }
}
