package server.network.clienthandling.logicutils.general;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.services.WeeklyScheduleUtils;
import server.network.clienthandling.logicutils.standing.StandingViewUtils;
import shareables.models.pojos.abstractions.DepartmentName;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.UserIdentifier;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.specialusers.Admin;
import shareables.models.pojos.users.specialusers.MrMohseni;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

public class OfflineModeUtils {
    public static OfflineModeDTO getOfflineModeDTO(DatabaseManager databaseManager, String userId) {
        OfflineModeDTO offlineModeDTO = new OfflineModeDTO();
        User user = IdentifiableFetchingUtils.getUser(databaseManager, userId);
        if (user.getUserIdentifier() == UserIdentifier.STUDENT) {
            Student student = (Student) user;
            initializeStudentOfflineModeDTO(databaseManager, student, offlineModeDTO);
        } else if (user.getUserIdentifier() == UserIdentifier.PROFESSOR) {
            Professor professor = (Professor) user;
            initializeProfessorOfflineModeDTO(databaseManager, professor, offlineModeDTO);
        } else if (user.getUserIdentifier() == UserIdentifier.MR_MOHSENI) {
            MrMohseni mrMohseni = (MrMohseni) user;
            initializeSpecialUserOfflineModeDTO(mrMohseni, offlineModeDTO);
        } else if (user.getUserIdentifier() == UserIdentifier.ADMIN) {
            Admin admin = (Admin) user;
            initializeSpecialUserOfflineModeDTO(admin, offlineModeDTO);
        }
        return offlineModeDTO;
    }

    private static void initializeSpecialUserOfflineModeDTO(User user, OfflineModeDTO offlineModeDTO) {
        initializeCommonUserFields(user, offlineModeDTO);
    }

    private static void initializeStudentOfflineModeDTO(DatabaseManager databaseManager, Student student,
                                                        OfflineModeDTO offlineModeDTO) {
        initializeCommonUserFields(student, offlineModeDTO);
        offlineModeDTO.setUserIdentifier(UserIdentifier.STUDENT);
        offlineModeDTO.setStudentStatus(student.getStudentStatus());
        offlineModeDTO.setEnrolmentTime(student.getEnrolmentTime());
        offlineModeDTO.setGPAString(student.fetchGPAString());
        offlineModeDTO.setYearOfEntry(student.getYearOfEntry());
        offlineModeDTO.setDegreeLevel(student.getDegreeLevel());
        offlineModeDTO.setTranscriptDTO(StandingViewUtils.getStudentTranscriptDTOWithId(databaseManager,
                student.getId()));
        offlineModeDTO.setCourseDTOs(WeeklyScheduleUtils.getStudentCourseDTOs(databaseManager, student.getId()));
        offlineModeDTO.setCourseScoreDTOs(StandingViewUtils.getStudentCourseScoreDTOsWithId(databaseManager,
                student.getId()));

        String permissionToEnrolPrompt;
        if (student.isPermittedToEnrol()) {
            permissionToEnrolPrompt = ConfigManager.getString(ConfigFileIdentifier.TEXTS, "isAllowedToEnrol");
        } else {
            permissionToEnrolPrompt = ConfigManager.getString(ConfigFileIdentifier.TEXTS, "isNotAllowedToEnrol");
        }
        offlineModeDTO.setPermissionToEnrolPrompt(permissionToEnrolPrompt);

        Professor advisingProfessor = IdentifiableFetchingUtils.getProfessor(databaseManager, student.getAdvisingProfessorId());
        if (advisingProfessor != null) {
            offlineModeDTO.setAdvisingProfessorName(advisingProfessor.fetchName());
        } else {
            String noAdvisingProfessorPrompt =
                    ConfigManager.getString(ConfigFileIdentifier.TEXTS, "noAdvisingProfessorFound");
            offlineModeDTO.setAdvisingProfessorName(noAdvisingProfessorPrompt);
        }
    }

    private static void initializeProfessorOfflineModeDTO(DatabaseManager databaseManager, Professor professor,
                                                          OfflineModeDTO offlineModeDTO) {
        initializeCommonUserFields(professor, offlineModeDTO);
        offlineModeDTO.setUserIdentifier(UserIdentifier.PROFESSOR);
        offlineModeDTO.setOfficeNumber(professor.getOfficeNumber());
        offlineModeDTO.setAcademicLevel(professor.getAcademicLevel());
        offlineModeDTO.setAcademicRole(professor.getAcademicRole());
        offlineModeDTO.setTemporaryDeputy(professor.isTemporaryDeputy());
        offlineModeDTO.setCourseDTOs(WeeklyScheduleUtils.getProfessorCourseDTOs(databaseManager, professor.getId()));
    }

    private static void initializeCommonUserFields(User user, OfflineModeDTO offlineModeDTO) {
        offlineModeDTO.setLastLogin(user.getLastLogin());
        offlineModeDTO.setName(user.fetchName());
        offlineModeDTO.setEmailAddress(user.getEmailAddress());
        offlineModeDTO.setProfilePicture(user.getProfilePicture());
        offlineModeDTO.setId(user.getId());
        offlineModeDTO.setNationalId(user.getNationalId());
        offlineModeDTO.setPhoneNumber(user.getPhoneNumber());
        offlineModeDTO.setDepartmentId(user.getDepartmentId());
        // TODO: setting offlineMessengerDTO
    }

    private static DepartmentName getDepartmentNameWithId(String departmentId) {
        DepartmentName departmentName;
        switch (departmentId) {
            case "1":
                departmentName = DepartmentName.MATHEMATICS;
                break;
            case "2":
                departmentName = DepartmentName.PHYSICS;
                break;
            case "3":
                departmentName = DepartmentName.ECONOMICS;
                break;
            case "4":
                departmentName = DepartmentName.CHEMISTRY;
                break;
            case "5":
                departmentName = DepartmentName.AEROSPACE_ENGINEERING;
                break;
            default:
                departmentName = null; // this branch added for explicitness
        }
        return departmentName;
    }
}
