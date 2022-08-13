package server.network.clienthandling.logicutils.messaging.contactfetching;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.Student;
import shareables.models.pojos.users.students.StudentStatus;
import shareables.network.DTOs.messaging.ContactProfileDTO;

import java.util.ArrayList;
import java.util.List;

import static server.network.clienthandling.logicutils.messaging.contactfetching.ContactFetchingUtils.getNotifiedContactProfileDTOs;
import static server.network.clienthandling.logicutils.messaging.contactfetching.ContactFetchingUtils.initializeContactProfileDTO;

public class StudentContactFetchingUtils {
    public static List<ContactProfileDTO> getStudentContactProfileDTOs(DatabaseManager databaseManager, String studentId) {
        List<ContactProfileDTO> contactProfileDTOs = new ArrayList<>();
        contactProfileDTOs.addAll(getAdvisingProfessorContactProfileDTO(databaseManager, studentId));
        contactProfileDTOs.addAll(getStudentPeerContactProfileDTOs(databaseManager, studentId));
        contactProfileDTOs.addAll(getNotifiedContactProfileDTOs(databaseManager, studentId));
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
}
