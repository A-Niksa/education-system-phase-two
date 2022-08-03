package server.network.clienthandling.logicutils.enrolment;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
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
    public static List<CourseDTO> getActiveCourseDTOs(DatabaseManager databaseManager) {
        List<Identifiable> courses = databaseManager.getIdentifiables(DatasetIdentifier.COURSES);
        Predicate<Identifiable> predicate = e -> ((Course) e).isActive();
        List<CourseDTO> activeCourseDTOs = getCourseDTOsByPredicate(courses, predicate);
        return activeCourseDTOs;
    }

    public static List<CourseDTO> getDepartmentCourseDTOs(DatabaseManager databaseManager, String departmentId) {
        List<Identifiable> courses = databaseManager.getIdentifiables(DatasetIdentifier.COURSES);
        Predicate<Identifiable> predicate = e -> ((Course) e).getDepartmentId().equals(departmentId);
        List<CourseDTO> departmentCourseDTOs = getCourseDTOsByPredicate(courses, predicate);
        return departmentCourseDTOs;
    }

    public static List<CourseDTO> getCourseDTOsByPredicate(List<Identifiable> courses, Predicate<Identifiable> predicate) {
        List<CourseDTO> courseDTOs = new ArrayList<>();
        courses.parallelStream()
                .filter(predicate)
                .forEach(e -> {
                    Course course = (Course) e;
                    CourseDTO courseDTO = initializeCourseDTO(course);
                    courseDTOs.add(courseDTO);
                });
        return courseDTOs;
    }

    public static List<ProfessorDTO> getProfessorDTOs(DatabaseManager databaseManager) {
        List<Identifiable> professors = databaseManager.getIdentifiables(DatasetIdentifier.PROFESSORS);
        List<ProfessorDTO> professorDTOs = new ArrayList<>();
        professors.parallelStream()
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
        return professorDTO;
    }
}