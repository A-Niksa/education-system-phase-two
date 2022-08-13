package server.network.clienthandling.logicutils.messaging.contactfetching;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.notifications.Notification;
import shareables.models.pojos.notifications.NotificationIdentifier;
import shareables.models.pojos.notifications.NotificationStatus;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.UserIdentifier;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.Student;
import shareables.models.pojos.users.students.StudentStatus;
import shareables.network.DTOs.messaging.ContactIdentifier;
import shareables.network.DTOs.messaging.ContactProfileDTO;

import java.util.ArrayList;
import java.util.List;

public class StudentContactFetchingUtils {
    public static List<ContactProfileDTO> getStudentContactProfileDTOs(DatabaseManager databaseManager, String studentId) {
        List<ContactProfileDTO> contactProfileDTOs = new ArrayList<>();
        contactProfileDTOs.addAll(getAdvisingProfessorContactProfileDTO(databaseManager, studentId));
        contactProfileDTOs.addAll(getStudentPeerContactProfileDTOs(databaseManager, studentId));
        contactProfileDTOs.addAll(getStudentNotifiedContactProfileDTOs(databaseManager, studentId));
        return contactProfileDTOs;
    }

    private static List<ContactProfileDTO> getAdvisingProfessorContactProfileDTO(DatabaseManager databaseManager,
                                                                           String studentId) {
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, studentId);
        Professor advisingProfessor = IdentifiableFetchingUtils.getProfessor(databaseManager, student.getAdvisingProfessorId());
        List<ContactProfileDTO> advisingProfessorContactProfileDTOList = new ArrayList<>(List.of(
            initializeContactProfileDTO(advisingProfessor, true)
        ));
        return advisingProfessorContactProfileDTOList;
    }

    private static List<ContactProfileDTO> getStudentPeerContactProfileDTOs(DatabaseManager databaseManager, String studentId) {
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, studentId);
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, student.getDepartmentId());
        List<ContactProfileDTO> peerContactProfileDTOs = new ArrayList<>();
        department.getStudentIds().stream()
                .map(id -> IdentifiableFetchingUtils.getStudent(databaseManager, id))
                .filter(fetchedStudent -> fetchedStudent.getStudentStatus() != StudentStatus.DROPPED_OUT)
                .filter(fetchedStudent -> fetchedStudent.getDegreeLevel() == student.getDegreeLevel())
                .filter(fetchedStudent -> fetchedStudent.getYearOfEntry() == student.getYearOfEntry())
                .filter(fetchedStudent -> !fetchedStudent.getId().equals(studentId))
                .forEach(peer -> {
                    peerContactProfileDTOs.add(initializeContactProfileDTO(peer, false));
                });
        return peerContactProfileDTOs;
    }

    private static List<ContactProfileDTO> getStudentNotifiedContactProfileDTOs(DatabaseManager databaseManager, String studentId) {
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, studentId);
        List<Notification> notifications = student.getNotificationsManager().getNotifications();
        List<ContactProfileDTO> notifiedContactProfileDTOs = new ArrayList<>();
        notifications.stream()
                .filter(notification -> notification.getNotificationIdentifier() == NotificationIdentifier.MESSAGE_REQUEST)
                .filter(notification -> notification.getNotificationStatus() == NotificationStatus.ACCEPTED)
                .map(notification -> IdentifiableFetchingUtils.getUser(databaseManager, notification.getReceiverId()))
                .forEach(notifiedUser -> {
                    notifiedContactProfileDTOs.add(initializeContactProfileDTO(notifiedUser, false));
                });
        return notifiedContactProfileDTOs;
    }

    private static ContactProfileDTO initializeContactProfileDTO(User user, boolean isAdvisingProfessor) {
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
}
