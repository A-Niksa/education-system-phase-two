package server.network.clienthandling.logicutils.messaging.contactfetching;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.users.professors.AcademicRole;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.messaging.ContactProfileDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static server.network.clienthandling.logicutils.messaging.contactfetching.ContactFetchingUtils.getNotifiedContactProfileDTOs;

public class ProfessorContactFetchingUtils {
    public static List<ContactProfileDTO> getProfessorContactProfileDTOs(DatabaseManager databaseManager, String professorId) {
        List<ContactProfileDTO> contactProfileDTOs = new ArrayList<>();
        Professor professor = IdentifiableFetchingUtils.getProfessor(databaseManager, professorId);
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, professor.getDepartmentId());
        if (professor.getAcademicRole() == AcademicRole.DEPUTY || professor.getAcademicRole() == AcademicRole.DEAN) {
            contactProfileDTOs.addAll(getDepartmentStudentContactProfileDTOs(databaseManager, department));
        } else {
            contactProfileDTOs.addAll(getAdviseeStudentContactProfileDTOs(databaseManager, department, professorId));
        }
        contactProfileDTOs.addAll(getNotifiedContactProfileDTOs(databaseManager, professorId));
        return contactProfileDTOs;
    }

    private static List<ContactProfileDTO> getDepartmentStudentContactProfileDTOs(DatabaseManager databaseManager,
                                                                                  Department department) {
        return department.getStudentIds().stream()
                .map(id -> IdentifiableFetchingUtils.getStudent(databaseManager, id))
                .map(student -> {
                    return ContactFetchingUtils.initializeContactProfileDTO(student, false);
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static List<ContactProfileDTO> getAdviseeStudentContactProfileDTOs(DatabaseManager databaseManager,
                                                                               Department department, String professorId) {
        return department.getStudentIds().stream()
                .map(id -> IdentifiableFetchingUtils.getStudent(databaseManager, id))
                .filter(student -> student.getAdvisingProfessorId().equals(professorId))
                .map(student -> {
                    return ContactFetchingUtils.initializeContactProfileDTO(student, false);
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
