package server.network.clienthandling.logicutils.addition;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import server.network.clienthandling.logicutils.messaging.AdminMessagingUtils;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.models.pojos.users.students.Student;
import shareables.models.pojos.users.students.StudentStatus;
import shareables.network.requests.Request;

public class StudentAdditionUtils {
    public static String addStudentAndReturnId(DatabaseManager databaseManager, Request request) {
        int yearOfEntry = (int) request.get("yearOfEntry");
        DegreeLevel degreeLevel = (DegreeLevel) request.get("degreeLevel");
        String departmentId = (String) request.get("departmentId");
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);

        Student student = new Student(yearOfEntry, degreeLevel, departmentId);
        student.setPassword((String) request.get("password"));
        student.setNationalId((String) request.get("nationalId"));
        student.setFirstName((String) request.get("firstName"));
        student.setLastName((String) request.get("lastName"));
        student.setPhoneNumber((String) request.get("phoneNumber"));
        student.setEmailAddress((String) request.get("emailAddress"));
        student.setStudentStatus((StudentStatus) request.get("studentStatus"));
        String advisingProfessorName = (String) request.get("advisingProfessorName");
        Professor advisingProfessor = getProfessor(databaseManager, department, advisingProfessorName);
        student.setAdvisingProfessorId(advisingProfessor.getId());

        databaseManager.save(DatasetIdentifier.STUDENTS, student);

        AdminMessagingUtils.addAdminHelpingMessageToUserMessenger(databaseManager, student);

        advisingProfessor.addToAdviseeStudentIds(student.getId());
        department.addToStudentIds(student.getId());


        return student.getId();
    }

    private static Professor getProfessor(DatabaseManager databaseManager, Department department, String professorName) {
        return department.getProfessorIds().stream()
                .map(id -> IdentifiableFetchingUtils.getProfessor(databaseManager, id))
                .filter(professor -> professor.fetchName().equals(professorName))
                .findAny().orElse(null);
    }
}
