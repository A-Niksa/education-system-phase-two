package server.network.clienthandling.logicutils.messaging.contactfetching;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.notifications.Notification;
import shareables.models.pojos.notifications.NotificationIdentifier;
import shareables.models.pojos.notifications.NotificationStatus;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.UserIdentifier;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.messaging.ContactIdentifier;
import shareables.network.DTOs.messaging.ContactProfileDTO;

import java.util.ArrayList;
import java.util.List;

public class ContactFetchingUtils {
    public static boolean contactIdsExist(DatabaseManager databaseManager, List<String> contactIds) {
        return contactIds.stream()
                .allMatch(id -> IdentifiableFetchingUtils.getUser(databaseManager, id) != null);
    }

    public static ContactProfileDTO initializeContactProfileDTO(User user, boolean isAdvisingProfessor) {
        ContactProfileDTO contactProfileDTO = new ContactProfileDTO();
        contactProfileDTO.setContactId(user.getId());
        contactProfileDTO.setContactName(user.fetchName());
        contactProfileDTO.setContactIdentifier(getContactIdentifier(user, isAdvisingProfessor));
        return contactProfileDTO;
    }

    private static ContactIdentifier getContactIdentifier(User user, boolean isAdvisingProfessor) {
        if (isAdvisingProfessor) {
            return ContactIdentifier.ADVISING_PROFESSOR;
        }

        if (user.getUserIdentifier() == UserIdentifier.STUDENT) {
            Student student = (Student) user;
            switch (student.getDegreeLevel()) {
                case UNDERGRADUATE:
                    return ContactIdentifier.UNDERGRADUATE_STUDENT;
                case GRADUATE:
                    return ContactIdentifier.GRADUATE_STUDENT;
                case PHD:
                    return ContactIdentifier.PHD_STUDENT;
            }
        } else if (user.getUserIdentifier() == UserIdentifier.PROFESSOR) {
            return ContactIdentifier.PROFESSOR;
        } else if (user.getUserIdentifier() == UserIdentifier.MR_MOHSENI) {
            return ContactIdentifier.MR_MOHSENI;
        } else if (user.getUserIdentifier() == UserIdentifier.ADMIN) {
            return ContactIdentifier.ADMIN;
        }

        return null;
    }

    public static List<ContactProfileDTO> getNotifiedContactProfileDTOs(DatabaseManager databaseManager,
                                                                                  String userId) {
        User user = IdentifiableFetchingUtils.getUser(databaseManager, userId);
        List<Notification> notifications = user.getNotificationsManager().getNotifications();
        List<ContactProfileDTO> notifiedContactProfileDTOs = new ArrayList<>();
        notifications.stream()
                .filter(notification -> notification.getNotificationIdentifier() == NotificationIdentifier.MESSAGE_REQUEST)
                .filter(notification -> notification.getNotificationStatus() == NotificationStatus.ACCEPTED)
                .filter(notification -> !notification.getReceiverId().equals(userId))
                .map(notification -> IdentifiableFetchingUtils.getUser(databaseManager, notification.getReceiverId()))
                .forEach(notifiedUser -> {
                    notifiedContactProfileDTOs.add(initializeContactProfileDTO(notifiedUser, false));
                });
        return notifiedContactProfileDTOs;
    }
}
