package shareables.models.idgeneration;

import java.util.Date;

public abstract class IdentifiableWithTime extends Identifiable {
    protected Date date;

    public IdentifiableWithTime() {
        date = new Date();
    }

    @Override
    protected void initializeId() {
        id = idGenerator.nextId(this);
    }

    protected void initializeId(SequentialIdGenerator sequentialIdGenerator) {
        id = idGenerator.nextId(this, sequentialIdGenerator);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
