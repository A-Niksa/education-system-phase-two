package shareables.models.pojos.academicrequests;

import server.database.datasets.DatasetIdentifier;
import shareables.models.idgeneration.SequentialIdGenerator;

import java.util.Date;

public class DefenseRequest extends AcademicRequest {
    private static SequentialIdGenerator sequentialIdGenerator;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator(DatasetIdentifier.DEFENSE_REQUESTS);
    }

    private Date requestedDefenseDate;

    public DefenseRequest() {
        super(AcademicRequestIdentifier.DEFENSE);
        initializeId(sequentialIdGenerator);
    }

    public Date getRequestedDefenseDate() {
        return requestedDefenseDate;
    }

    public void setRequestedDefenseDate(Date requestedDefenseDate) {
        this.requestedDefenseDate = requestedDefenseDate;
    }
}
