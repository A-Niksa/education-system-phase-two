package server.database.datamodels.requests;

import javax.persistence.Entity;
import java.util.Random;

@Entity
public class DormRequest extends Request {
    private Random random;

    public DormRequest() {
        random = new Random();
        determineRequestStatus();
    }

    private void determineRequestStatus() {
        if (random.nextBoolean()) {
            requestStatus = RequestStatus.ACCEPTED;
        } else {
            requestStatus = RequestStatus.REJECTED;
        }
    }
}
