package server.network.clienthandling.logicutils.enrolment;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.users.professors.AcademicLevel;
import shareables.models.pojos.users.professors.AcademicRole;
import shareables.models.pojos.users.professors.Professor;

import java.util.List;

public class ProfessorEditingUtils {
    public static void changeProfessorAcademicLevel(DatabaseManager databaseManager, String professorId,
                                                    String newAcademicLevelString) {
        AcademicLevel newAcademicLevel = getAcademicLevelWithString(newAcademicLevelString);
        Professor professor = IdentifiableFetchingUtils.getProfessor(databaseManager, professorId);
        professor.setAcademicLevel(newAcademicLevel);
    }

    private static AcademicLevel getAcademicLevelWithString(String academicLevelString) {
        AcademicLevel academicLevel;
        switch (academicLevelString) {
            case "Assistant Professor":
                academicLevel = AcademicLevel.ASSISTANT;
                break;
            case "Associate Professor":
                academicLevel = AcademicLevel.ASSOCIATE;
                break;
            case "Full Professor":
                academicLevel = AcademicLevel.FULL;
                break;
            default:
                academicLevel = null; // added for explicitness
        }
        return academicLevel;
    }

    public static void changeProfessorOfficeNumber(DatabaseManager databaseManager, String professorId,
                                                   String newOfficeNumber) {
        Professor professor = IdentifiableFetchingUtils.getProfessor(databaseManager, professorId);
        professor.setOfficeNumber(newOfficeNumber);
    }

    public static boolean isProfessorDepartmentDeputy(DatabaseManager databaseManager, String professorId, String departmentId) {
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        return professorId.equals(department.getDeputyId());
    }

    public static boolean isProfessorDepartmentDean(DatabaseManager databaseManager, String professorId, String departmentId) {
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        return professorId.equals(department.getDeanId());
    }

    public static boolean departmentHasDeputy(DatabaseManager databaseManager, String departmentId) {
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        return department.getDeputyId() != null;
    }

    public static void demoteProfessorFromDeputyRole(DatabaseManager databaseManager, String professorId,
                                                     String departmentId) {
        Professor professor = IdentifiableFetchingUtils.getProfessor(databaseManager, professorId);
        professor.setAcademicRole(AcademicRole.NORMAL);

        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        department.setDeputyId(null);
        /* note that the deputy can still accept/reject academic requests till a new prof is promoted. This is a
        design choice */
    }

    public static void promoteProfessorToDeputyRole(DatabaseManager databaseManager, String professorId,
                                                    String departmentId) {
        Professor professor = IdentifiableFetchingUtils.getProfessor(databaseManager, professorId);
        professor.setAcademicRole(AcademicRole.DEPUTY);

        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        String exDeputyId = department.getDeputyId();
        department.setDeputyId(professor.getId());

        RequestRedirectingUtils.redirectAcademicRequestsToNewDeputy(databaseManager, exDeputyId, professorId);
        /* didn't call professorId. In this way, we're sure that the professor with the given id exists since otherwise
        we'll get a null pointer exception */
    }

    public static void removeProfessor(DatabaseManager databaseManager, String professorId, String departmentId) {
        RequestRedirectingUtils.redirectAcademicRequestsToDeanIfNecessary(databaseManager, professorId, departmentId);
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        removeProfessorFromDepartment(professorId, department);
        removeProfessorFromAdvisors(databaseManager, department);
        removeProfessorFromActiveCourses(databaseManager, professorId, department);
        removeProfessorFromProfessorsDataset(databaseManager, professorId);
    }

    private static void removeProfessorFromDepartment(String professorId, Department department) {
        if (professorId.equals(department.getDeputyId())) {
            department.setDeputyId(null);
        }

        department.removeFromProfessorIds(professorId);
    }

    private static void removeProfessorFromAdvisors(DatabaseManager databaseManager, Department department) {
        List<String> departmentStudentIds = department.getStudentIds();
        departmentStudentIds.stream()
                .map(id -> IdentifiableFetchingUtils.getStudent(databaseManager, id))
                .forEach(student -> student.setAdvisingProfessorId(null));
    }

    private static void removeProfessorFromActiveCourses(DatabaseManager databaseManager, String professorId,
                                                         Department department) {
        List<String> departmentCourseIds = department.getCourseIds();
        departmentCourseIds.stream()
                .map(id -> IdentifiableFetchingUtils.getCourse(databaseManager, id))
                .filter(Course::isActive)
                .filter(course -> course.getTeachingProfessorIds().contains(professorId))
                .forEach(course -> course.removeFromTeachingProfessorIds(professorId));
    }

    private static void removeProfessorFromProfessorsDataset(DatabaseManager databaseManager, String professorId) {
        databaseManager.remove(DatasetIdentifier.PROFESSORS, professorId);
    }
}
