package shareables.models.idgeneration;

public class SequentialIdGenerator {
    private static int idCounter;

    public SequentialIdGenerator(int initialIdCounter) {
        idCounter = initialIdCounter;
    }

    public int nextSequentialId() {
        return idCounter++;
    }

    // TODO: should be removed ->
    public static int getIdCounter() {
        return idCounter;
    }
}
