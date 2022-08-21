package shareables.models.idgeneration;

public class SequentialIdGenerator {
    private int idCounter;

    public SequentialIdGenerator(int initialIdCounter) {
        idCounter = initialIdCounter;
    }

    public int nextSequentialId() {
        return idCounter++;
    }

    public int getIdCounter() {
        return idCounter;
    }
}
