package shareables.models.idgeneration;

public abstract class Identifiable {
    protected static IdGenerator idGenerator;
    static {
        idGenerator = new IdGenerator();
    }

    protected String id;

    protected static SequentialIdGenerator getNewSequentialIdGenerator() {
        return new SequentialIdGenerator();
    }

    public abstract void initializeId();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
