package client.locallogic.localdatabase.management;

import client.controller.ClientController;
import client.locallogic.localdatabase.datamodels.QueuedMessage;
import shareables.models.pojos.messaging.MessageType;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class QueuedMessagesManager {
    private ClientController clientController;
    private String userId;
    private List<QueuedMessage> queuedMessages;
    private Deque<QueuedMessage> submittedMessagesQueue;

    public QueuedMessagesManager(ClientController clientController, String userId) {
        this.clientController = clientController;
        this.userId = userId;
        submittedMessagesQueue = new ArrayDeque<>();
        updateQueue();
    }

    private void updateQueue() {
        updateQueuedMessages();
        updateSubmittedMessagesQueue();
    }

    private void updateSubmittedMessagesQueue() {
        submittedMessagesQueue.clear();
        queuedMessages.stream()
                .filter(message -> !message.isSent())
                .forEach(message -> submittedMessagesQueue.offer(message));
    }

    private void updateQueuedMessages() {
        queuedMessages = clientController.getQueuedMessagesFromLocalDatabase();
    }

    public void sendQueuedMessages() {
        updateQueue();

        QueuedMessage earliestQueuedMessage = submittedMessagesQueue.poll();
        if (earliestQueuedMessage != null) {
            if (!earliestQueuedMessage.isSent()) {
                sendToAdmin(earliestQueuedMessage);
                clientController.removeQueuedMessage(earliestQueuedMessage);
                sleepShortly();
            }
        }
    }

    private void sleepShortly() {
        try {
            Thread.sleep(ConfigManager.getInt(ConfigFileIdentifier.CONSTANTS, "queueSleepingTime"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendToAdmin(QueuedMessage earliestQueuedMessage) {
        String adminId = ConfigManager.getString(ConfigFileIdentifier.CONSTANTS, "adminId");
        ArrayList<String> receiverIds = new ArrayList<>(List.of(adminId));
        if (earliestQueuedMessage.getMessageType() == MessageType.TEXT) {
            sendTextToAdmin(receiverIds, earliestQueuedMessage);
        } else if (earliestQueuedMessage.getMessageType() == MessageType.MEDIA) {
            sendMediaToAdmin(receiverIds, earliestQueuedMessage);
        }
    }

    private void sendTextToAdmin(ArrayList<String> adminReceiverId, QueuedMessage earliestQueuedMessage) {
        clientController.sendTextMessage(userId, adminReceiverId, earliestQueuedMessage.getMessageText());

        MasterLogger.clientInfo(clientController.getId(), "Text message sent to admin",
                "sendTextToAdmin", getClass());
    }

    private void sendMediaToAdmin(ArrayList<String> adminReceiverId, QueuedMessage earliestQueuedMessage) {
        clientController.sendMediaMessage(userId, adminReceiverId, earliestQueuedMessage.getMessageMediaFile());

        MasterLogger.clientInfo(clientController.getId(), "Media file sent to admin",
                "sendMediaToAdmin", getClass());
    }

    public void submitQueuedMessage(QueuedMessage queuedMessage) {
        clientController.submitQueuedMessage(queuedMessage);
    }
}
