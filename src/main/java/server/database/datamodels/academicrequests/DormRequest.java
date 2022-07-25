package server.database.datamodels.academicrequests;

import javax.persistence.Entity;
import java.util.Random;

@Entity
public class DormRequest extends AcademicRequest {
    private Random randomGenerator;

    public DormRequest() {
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
