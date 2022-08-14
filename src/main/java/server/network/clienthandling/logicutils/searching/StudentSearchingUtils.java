package server.network.clienthandling.logicutils.searching;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.comparators.StudentProfileDTOComparator;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.StudentDTO;
import shareables.network.DTOs.messaging.ContactIdentifier;
import shareables.network.DTOs.messaging.ContactProfileDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentSearchingUtils {
    private static StudentProfileDTOComparator studentProfileDTOComparator;
    static {
        studentProfileDTOComparator = new StudentProfileDTOComparator();
    }

    public static List<ContactProfileDTO> getAllStudentContactProfileDTOs(DatabaseManager databaseManager) {
        return getAllStudentsStream(databaseManager)
                .map(StudentSearchingUtils::initializeStudentContactProfileDTO)
                .sorted(studentProfileDTOComparator)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<ContactProfileDTO> getFilteredStudentContactProfileDTOs(DatabaseManager databaseManager,
                                                                               String studentIdStartsWith) {
        return getAllStudentsStream(databaseManager)
                .filter(student -> studentIdStartsWithGivenKey(student.getId(), studentIdStartsWith))
                .map(StudentSearchingUtils::initializeStudentContactProfileDTO)
                .sorted(studentProfileDTOComparator)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static boolean studentIdStartsWithGivenKey(String studentId, String key) {
        return studentId.startsWith(key);
    }

    private static Stream<Student> getAllStudentsStream(DatabaseManager databaseManager) {
        List<Identifiable> students = databaseManager.getIdentifiables(DatasetIdentifier.STUDENTS);
        return students.stream()
                .map(identifiable -> (Student) identifiable);
    }

    private static ContactProfileDTO initializeStudentContactProfileDTO(User user) {
        ContactProfileDTO contactProfileDTO = new ContactProfileDTO();
        contactProfileDTO.setContactId(user.getId());
        contactProfileDTO.setContactName(user.fetchName());

        Student student = (Student) user;
        contactProfileDTO.setYearOfEntry(student.getYearOfEntry());
        contactProfileDTO.setContactIdentifier(getStudentContactIdentifier(student));

        return contactProfileDTO;
    }

    private static ContactIdentifier getStudentContactIdentifier(Student student) {
        switch (student.getDegreeLevel()) {
            case UNDERGRADUATE:
                return ContactIdentifier.UNDERGRADUATE_STUDENT;
            case GRADUATE:
                return ContactIdentifier.GRADUATE_STUDENT;
            case PHD:
                return ContactIdentifier.PHD_STUDENT;
        }
        return null;
    }

    public static StudentDTO getStudentDTO(DatabaseManager databaseManager, String studentId) {
        return getAllStudentsStream(databaseManager)
                .filter(student -> student.getId().equals(studentId))
                .map(student -> initializeStudentDTO(databaseManager, student))
                .findAny().orElse(null);
    }

    private static StudentDTO initializeStudentDTO(DatabaseManager databaseManager, Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setNationalId(student.getNationalId());
        studentDTO.setProfilePicture(student.getProfilePicture());
        studentDTO.setName(student.fetchName());
        studentDTO.setPhoneNumber(student.getPhoneNumber());
        studentDTO.setEmailAddress(student.getEmailAddress());
        studentDTO.setGPAString(student.fetchGPAString());
        studentDTO.setYearOfEntry(student.getYearOfEntry());
        studentDTO.setDegreeLevel(student.getDegreeLevel());
        studentDTO.setStudentStatus(student.getStudentStatus());

        Professor advisingProfessor = IdentifiableFetchingUtils.getProfessor(databaseManager,
                student.getAdvisingProfessorId());
        if (advisingProfessor == null) {
            studentDTO.setAdvisingProfessorName(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                    "noAdvisingProfessorFound"));
        } else {
            studentDTO.setAdvisingProfessorName(advisingProfessor.fetchName());
        }

        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, student.getDepartmentId());
        studentDTO.setDepartmentName(department.getDepartmentName());

        return studentDTO;
    }
}
