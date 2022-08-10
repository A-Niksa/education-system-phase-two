package server.network.clienthandling.logicutils.enrolment;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.comparators.CourseDTOComparator;
import server.network.clienthandling.logicutils.comparators.ProfessorDTOComparator;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.CourseDTO;
import shareables.network.DTOs.ProfessorDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static server.network.clienthandling.logicutils.services.WeeklyScheduleUtils.initializeCourseDTO;

public class IdentifiableViewingUtils {
    private static CourseDTOComparator courseDTOComparator = new CourseDTOComparator();
    private static ProfessorDTOComparator professorDTOComparator = new ProfessorDTOComparator();

    public static List<CourseDTO> getActiveCourseDTOs(DatabaseManager databaseManager) {
        List<Identifiable> courses = databaseManager.getIdentifiables(DatasetIdentifier.COURSES);
        Predicate<Identifiable> predicate = e -> ((Course) e).isActive();
        List<CourseDTO> activeCourseDTOs = getCourseDTOsByPredicate(databaseManager, courses, predicate);
        activeCourseDTOs.sort(courseDTOComparator);
        return activeCourseDTOs;
    }

    public static List<CourseDTO> getDepartmentCourseDTOs(DatabaseManager databaseManager, String departmentId) {
        List<Identifiable> courses = databaseManager.getIdentifiables(DatasetIdentifier.COURSES);
        Predicate<Identifiable> predicate = e -> ((Course) e).getDepartmentId().equals(departmentId);
        List<CourseDTO> departmentCourseDTOs = getCourseDTOsByPredicate(databaseManager, courses, predicate);
        departmentCourseDTOs.sort(courseDTOComparator);
        return departmentCourseDTOs;
    }

    public static List<CourseDTO> getCourseDTOsByPredicate(DatabaseManager databaseManager, List<Identifiable> courses,
                                                           Predicate<Identifiable> predicate) {
        List<CourseDTO> courseDTOs = new ArrayList<>();
        courses.parallelStream()
                .filter(predicate)
                .forEach(e -> {
                    Course course = (Course) e;
                    CourseDTO courseDTO = initializeCourseDTO(databaseManager, course);
                    courseDTOs.add(courseDTO);
                });
        return courseDTOs;
    }

    public static List<ProfessorDTO> getProfessorDTOs(DatabaseManager databaseManager) {
        List<Identifiable> professors = databaseManager.getIdentifiables(DatasetIdentifier.PROFESSORS);
        List<ProfessorDTO> professorDTOs = getProfessorDTOsByPredicate(databaseManager, professors, e -> true);
        professorDTOs.sort(professorDTOComparator);
        return professorDTOs;
    }

    public static List<ProfessorDTO> getDepartmentProfessorDTOs(DatabaseManager databaseManager, String departmentId) {
        List<Identifiable> professors = databaseManager.getIdentifiables(DatasetIdentifier.PROFESSORS);
        Predicate<Identifiable> predicate = e -> ((Professor) e).getDepartmentId().equals(departmentId);
        List<ProfessorDTO> professorDTOs = getProfessorDTOsByPredicate(databaseManager, professors, predicate);
        return professorDTOs;
    }

    public static List<ProfessorDTO> getProfessorDTOsByPredicate(DatabaseManager databaseManager, List<Identifiable> professors,
                                                           Predicate<Identifiable> predicate) {
        List<ProfessorDTO> professorDTOs = new ArrayList<>();
        professors.parallelStream()
                .filter(predicate)
                .forEach(e -> {
                    Professor professor = (Professor) e;
                    ProfessorDTO professorDTO = initializeProfessorDTO(professor);
                    professorDTOs.add(professorDTO);
                });
        return professorDTOs;
    }

    private static ProfessorDTO initializeProfessorDTO(Professor professor) {
        ProfessorDTO professorDTO = new ProfessorDTO();
        professorDTO.setId(professor.getId());
        professorDTO.setName(professor.fetchName());
        professorDTO.setEmailAddress(professor.getEmailAddress());
        professorDTO.setOfficeNumber(professor.getOfficeNumber());
        professorDTO.setAcademicLevel(professor.getAcademicLevel());
        professorDTO.setAcademicRole(professor.getAcademicRole());
        return professorDTO;
    }
}