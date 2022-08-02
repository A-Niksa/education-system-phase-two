package shareables.models.pojos.academicrequests;

import server.database.datasets.DatasetIdentifier;
import shareables.models.idgeneration.SequentialIdGenerator;

public class DroppingOutRequest extends AcademicRequest {
    private static SequentialIdGenerator sequentialIdGenerator;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator(DatasetIdentifier.DROPPING_OUT_REQUESTS);
    }

    public DroppingOutRequest() {
        super(AcademicRequestIdentifier.DROPPING_OUT);
        initializeId(sequentialIdGenerator);
    }
}
