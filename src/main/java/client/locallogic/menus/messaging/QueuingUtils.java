package client.locallogic.menus.messaging;

import client.locallogic.localdatabase.datamodels.QueuedMessage;
import shareables.models.pojos.media.MediaFile;
import shareables.models.pojos.messaging.MessageType;

public class QueuingUtils {
    public static QueuedMessage getTextQueuedMessage(String senderId, String messageText) {
        QueuedMessage queuedMessage = new QueuedMessage();
        queuedMessage.setMessageType(MessageType.TEXT);
        queuedMessage.setSenderId(senderId);
        queuedMessage.setMessageText(messageText);
        return queuedMessage;
    }

    public static QueuedMessage getMediaQueuedMessage(String senderId, MediaFile mediaFile) {
        QueuedMessage queuedMessage = new QueuedMessage();
        queuedMessage.setMessageType(MessageType.MEDIA);
        queuedMessage.setSenderId(senderId);
        queuedMessage.setMessageMediaFile(mediaFile);
        return queuedMessage;
    }
}
