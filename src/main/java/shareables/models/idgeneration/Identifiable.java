package shareables.models.idgeneration;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseIdSupplier;

public abstract class Identifiable {
    protected static IdGenerator idGenerator;
    static {
        idGenerator = new IdGenerator();
    }

    protected String id;

    protected static SequentialIdGenerator getNewSequentialIdGenerator() {
        return new SequentialIdGenerator(0); // starts counting from 0
    }

    protected static SequentialIdGenerator getNewSequentialIdGenerator(DatasetIdentifier datasetIdentifier) {
        return new SequentialIdGenerator(DatabaseIdSupplier.getDatasetIdCounter(datasetIdentifier));
    }

    protected abstract void initializeId();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
