package server.network.clienthandling.logicutils.comparators;

import shareables.network.DTOs.notifications.NotificationDTO;

import java.util.Comparator;

public class NotificationDTOComparator implements Comparator<NotificationDTO> {
    @Override
    public int compare(NotificationDTO firstNotificationDTO, NotificationDTO secondNotificationDTO) {
        return firstNotificationDTO.getNotificationDate()
                .compareTo(secondNotificationDTO.getNotificationDate());
    }
}
