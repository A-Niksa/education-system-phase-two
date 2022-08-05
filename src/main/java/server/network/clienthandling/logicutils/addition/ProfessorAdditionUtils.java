package server.network.clienthandling.logicutils.addition;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.users.professors.AcademicLevel;
import shareables.models.pojos.users.professors.AcademicRole;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.requests.Request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfessorAdditionUtils {
    public static boolean studentsDoNotExistInDepartment(DatabaseManager databaseManager, String[] studentIds,
                                                         String departmentId) {
        if (studentIds.length == 1 &&
                studentIds[0].equals("")) {
            return false; // escaping the condition if the array is empty
        }

        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        List<String> departmentStudentIds = department.getStudentIds();
        return Arrays.stream(studentIds).anyMatch(id -> !departmentStudentIds.contains(id));
    }

    public static boolean anyStudentAlreadyHasAnAdvisor(DatabaseManager databaseManager, String[] studentIds) {
        if (studentIds.length == 1 &&
                studentIds[0].equals("")) {
            return false; // escaping the condition if the array is empty
        }

        return Arrays.stream(studentIds)
                .map(id -> IdentifiableFetchingUtils.getStudent(databaseManager, id))
                .anyMatch(student -> student.getAdvisingProfessorId() != null);
    }

    public static String addProfessorAndReturnId(DatabaseManager databaseManager, Request request) {
        AcademicLevel academicLevel = (AcademicLevel) request.get("academicLevel");
        String departmentId = (String) request.get("departmentId");
        // added professors have no administrative role by default:
        Professor professor = new Professor(AcademicRole.NORMAL, academicLevel, departmentId);
        professor.setPassword((String) request.get("password"));
        professor.setNationalId((String) request.get("nationalId"));
        professor.setFirstName((String) request.get("firstName"));
        professor.setLastName((String) request.get("lastName"));
        professor.setPhoneNumber((String) request.get("phoneNumber"));
        professor.setEmailAddress((String) request.get("emailAddress"));
        professor.setOfficeNumber((String) request.get("officeNumber"));
        String[] adviseeStudentIdsArray = (String[]) request.get("adviseeStudentIds");
        if (adviseeStudentIdsArray.length != 1 ||
                !adviseeStudentIdsArray[0].equals("")) {
            List<String> adviseeStudentIdsList = new ArrayList<>(Arrays.asList(adviseeStudentIdsArray));
            professor.setAdviseeStudentIds(adviseeStudentIdsList);
            updateAdvisingProfessorOfAdviseeStudents(databaseManager, adviseeStudentIdsList, professor.getId());
        }

        databaseManager.save(DatasetIdentifier.PROFESSORS, professor);

        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        department.addToProfessorIds(professor.getId());

        return professor.getId();
    }

    // TODO: unrelated to here but making student ids department independent (For sequential ones) or increasing capacity

    private static void updateAdvisingProfessorOfAdviseeStudents(DatabaseManager databaseManager,
                                                                 List<String> adviseeStudentIdsList, String advisingProfessorId) {
        adviseeStudentIdsList.stream()
                .map(id -> IdentifiableFetchingUtils.getStudent(databaseManager, id))
                .forEach(student -> student.setAdvisingProfessorId(advisingProfessorId));
    }
}
