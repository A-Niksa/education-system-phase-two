package shareables.models.pojos.academicrequests;

public enum AcademicRequestStatus {
    SUBMITTED("Submitted"),
    ACCEPTED("Accepted"),
    DECLINED("Declined");

    private String academicRequestStatusString;

    AcademicRequestStatus(String academicRequestStatusString) {
        this.academicRequestStatusString = academicRequestStatusString;
    }

    @Override
    public String toString() {
        return academicRequestStatusString;
    }
}
