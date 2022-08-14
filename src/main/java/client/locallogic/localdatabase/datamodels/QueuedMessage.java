package client.locallogic.localdatabase.datamodels;

import shareables.models.pojos.messaging.Message;

public class QueuedMessage extends Message {
    private boolean isSent;

    public QueuedMessage() {
        isSent = false;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }
}
