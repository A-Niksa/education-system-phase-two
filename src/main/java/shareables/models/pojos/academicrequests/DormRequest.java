package shareables.models.pojos.academicrequests;

import shareables.models.idgeneration.SequentialIdGenerator;

import java.util.Random;

public class DormRequest extends AcademicRequest {
    private static SequentialIdGenerator sequentialIdGenerator;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator();
    }

    private Random randomGenerator;

    public DormRequest() {
        super(AcademicRequestIdentifier.DORM);
        initializeId(sequentialIdGenerator);
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
