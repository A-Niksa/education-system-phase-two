package shareables.models.idgeneration;

public class SequentialIdGenerator {
    private int idCounter;

    public SequentialIdGenerator(int initialIdCounter) {
        idCounter = initialIdCounter;
    }

    public int nextSequentialId() {
        return idCounter++;
    }

    // TODO: should be removed ->
    public int getIdCounter() {
        return idCounter;
    }
}
