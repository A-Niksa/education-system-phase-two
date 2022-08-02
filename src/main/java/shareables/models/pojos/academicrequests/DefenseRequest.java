package shareables.models.pojos.academicrequests;

import server.database.datasets.DatasetIdentifier;
import shareables.models.idgeneration.SequentialIdGenerator;

import java.time.LocalDateTime;

public class DefenseRequest extends AcademicRequest {
    private static SequentialIdGenerator sequentialIdGenerator;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator(DatasetIdentifier.DEFENSE_REQUESTS);
    }

    private LocalDateTime requestedDefenseTime;

    public DefenseRequest() {
        super(AcademicRequestIdentifier.DEFENSE);
        initializeId(sequentialIdGenerator);
    }

    public LocalDateTime getRequestedDefenseTime() {
        return requestedDefenseTime;
    }

    public void setRequestedDefenseTime(LocalDateTime requestedDefenseTime) {
        this.requestedDefenseTime = requestedDefenseTime;
    }
}
