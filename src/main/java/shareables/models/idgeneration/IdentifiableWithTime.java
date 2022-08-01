package shareables.models.idgeneration;

import java.time.LocalDateTime;
import java.util.Date;

public abstract class IdentifiableWithTime extends Identifiable {
    protected LocalDateTime date;

    public IdentifiableWithTime() {
        date = LocalDateTime.now();
    }

    @Override
    protected void initializeId() {
        id = idGenerator.nextId(this);
    }

    protected void initializeId(SequentialIdGenerator sequentialIdGenerator) {
        id = idGenerator.nextId(this, sequentialIdGenerator);
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
