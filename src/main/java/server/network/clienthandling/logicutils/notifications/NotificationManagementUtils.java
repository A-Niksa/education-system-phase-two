package server.network.clienthandling.logicutils.notifications;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.comparators.NotificationDTOComparator;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.notifications.Notification;
import shareables.models.pojos.notifications.NotificationStatus;
import shareables.models.pojos.users.User;
import shareables.network.DTOs.notifications.NotificationDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationManagementUtils {
    private static NotificationDTOComparator notificationDTOComparator;
    static {
        notificationDTOComparator = new NotificationDTOComparator();
    }

    public static List<NotificationDTO> getNotificationDTOs(DatabaseManager databaseManager, String userId) {
        User user = IdentifiableFetchingUtils.getUser(databaseManager, userId);
        List<Notification> notifications = user.getNotificationsManager().getNotifications();
        List<NotificationDTO> notificationDTOs = new ArrayList<>();

        notifications.stream()
                        .filter(notification -> notification.getReceiverId().equals(userId))
                        .forEach(notification -> {
                            notificationDTOs.add(initializeNotificationDTO(databaseManager, notification));
                        });

        notificationDTOs.sort(notificationDTOComparator);
        Collections.reverse(notificationDTOs);
        return notificationDTOs;
    }

    private static NotificationDTO initializeNotificationDTO(DatabaseManager databaseManager, Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notification.getId());
        notificationDTO.setNotificationDate(notification.getNotificationDate());
        notificationDTO.setSentByUser(notification.isSentByUser());
        notificationDTO.setSenderId(notification.getSenderId());
        User user = IdentifiableFetchingUtils.getUser(databaseManager, notification.getSenderId());
        notificationDTO.setSenderName(user.fetchName());
        notificationDTO.setRequest(notification.isRequest());
        notificationDTO.setNotificationIdentifier(notification.getNotificationIdentifier());
        notificationDTO.setNotificationText(notification.getNotificationText());
        notificationDTO.setNotificationStatus(notification.getNotificationStatus());
        return notificationDTO;
    }
}
