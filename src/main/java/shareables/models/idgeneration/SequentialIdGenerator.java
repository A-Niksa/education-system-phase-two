package shareables.models.idgeneration;

public class SequentialIdGenerator {
    private static int idCounter;

    public SequentialIdGenerator() {
        idCounter = 0;
    }

    public int nextSequentialId() {
        return idCounter++;
    }
}
