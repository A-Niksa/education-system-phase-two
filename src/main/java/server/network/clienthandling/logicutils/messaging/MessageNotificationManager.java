package server.network.clienthandling.logicutils.messaging;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.notifications.Notification;
import shareables.models.pojos.notifications.NotificationIdentifier;
import shareables.models.pojos.users.User;
import shareables.network.DTOs.messaging.ContactProfileDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageNotificationManager {
    public static boolean sendMessageNotificationIfNecessary(DatabaseManager databaseManager, List<String> contactIds,
                                                             List<ContactProfileDTO> contactProfileDTOs, String userId) {
        List<String> currentContactIds = contactProfileDTOs.stream()
                .map(ContactProfileDTO::getContactId)
                .collect(Collectors.toCollection(ArrayList::new));
        boolean shouldSendMessageNotification = contactIds.stream()
                .anyMatch(id -> !currentContactIds.contains(id));

        if (shouldSendMessageNotification) sendMessageNotifications(databaseManager, contactIds, currentContactIds, userId);

        return shouldSendMessageNotification;
    }

    private static void sendMessageNotifications(DatabaseManager databaseManager, List<String> contactIds,
                                                 List<String> currentContactIds, String userId) {
        List<String> contactIdsToNotify = getContactIdsToNotify(contactIds, currentContactIds);

        User sender = IdentifiableFetchingUtils.getUser(databaseManager, userId);

        contactIdsToNotify.stream()
                .map(id -> IdentifiableFetchingUtils.getUser(databaseManager, id))
                .forEach(contact -> {
                    Notification notification = new Notification();
                    notification.setNotificationIdentifier(NotificationIdentifier.MESSAGE_REQUEST);
                    notification.setSentByUser(true);
                    notification.setSenderId(userId);
                    notification.setReceiverId(contact.getId());
                    notification.setRequest(true);
                    notification.setNotificationText(
                            ConfigManager.getString(ConfigFileIdentifier.TEXTS, "letMeMessageYou")
                    );

                    contact.getNotificationsManager().addToNotifications(notification);
                    sender.getNotificationsManager().addToNotifications(notification);
                });
    }

    private static List<String> getContactIdsToNotify(List<String> contactIds, List<String> currentContactIds) {
        return contactIds.stream()
                .filter(id -> !currentContactIds.contains(id))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
