package shareables.models.pojos.academicrequests;

import java.util.Random;

public class DormRequest extends AcademicRequest {
    private Random randomGenerator;

    public DormRequest() {
        super(AcademicRequestIdentifier.DORM);
        randomGenerator = new Random();
        determineRequestStatus();
    }

    private void determineRequestStatus() {
        if (randomGenerator.nextBoolean()) {
            academicRequestStatus = AcademicRequestStatus.ACCEPTED;
        } else {
            academicRequestStatus = AcademicRequestStatus.REJECTED;
        }
    }
}
