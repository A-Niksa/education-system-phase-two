package server.network.clienthandling.logicutils.general;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.messaging.MessengerViewUtils;
import server.network.clienthandling.logicutils.services.WeeklyScheduleUtils;
import server.network.clienthandling.logicutils.standing.StandingViewUtils;
import server.network.clienthandling.logicutils.unitselection.sessionaddition.UnitSelectionTimeUtils;
import shareables.models.pojos.unitselection.UnitSelectionSession;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.UserIdentifier;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.specialusers.Admin;
import shareables.models.pojos.users.specialusers.MrMohseni;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import static server.network.clienthandling.logicutils.unitselection.sessionaddition.UnitSelectionTimeUtils.isCurrentTimeInTimeRange;

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
            initializeSpecialUserOfflineModeDTO(databaseManager, mrMohseni, offlineModeDTO);
            offlineModeDTO.setUserIdentifier(UserIdentifier.MR_MOHSENI);
        } else if (user.getUserIdentifier() == UserIdentifier.ADMIN) {
            Admin admin = (Admin) user;
            initializeSpecialUserOfflineModeDTO(databaseManager, admin, offlineModeDTO);
            offlineModeDTO.setUserIdentifier(UserIdentifier.ADMIN);
        }
        return offlineModeDTO;
    }

    private static void initializeSpecialUserOfflineModeDTO(DatabaseManager databaseManager, User user,
                                                            OfflineModeDTO offlineModeDTO) {
        initializeCommonUserFields(databaseManager, user, offlineModeDTO);
    }

    private static void initializeStudentOfflineModeDTO(DatabaseManager databaseManager, Student student,
                                                        OfflineModeDTO offlineModeDTO) {
        initializeCommonUserFields(databaseManager, student, offlineModeDTO);
        offlineModeDTO.setUserIdentifier(UserIdentifier.STUDENT);
        offlineModeDTO.setStudentStatus(student.getStudentStatus());
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

        UnitSelectionSession unitSelectionSession = UnitSelectionTimeUtils.getStudentFutureUnitSelectionSession(databaseManager,
                student);

        if (unitSelectionSession != null) {
            student.setEnrolmentTime(unitSelectionSession.getStartsAt());

            // access to uni selection depends on whether the student is allowed to enrol or not:
            offlineModeDTO.setTimeForUnitSelection(student.isPermittedToEnrol() &&
                    isCurrentTimeInTimeRange(unitSelectionSession.getStartsAt(), unitSelectionSession.getEndsAt()));

            offlineModeDTO.setEnrolmentTime(student.getEnrolmentTime());
        } else {
            offlineModeDTO.setTimeForUnitSelection(false);
            offlineModeDTO.setEnrolmentTime(null);
        }
    }

    private static void initializeProfessorOfflineModeDTO(DatabaseManager databaseManager, Professor professor,
                                                          OfflineModeDTO offlineModeDTO) {
        initializeCommonUserFields(databaseManager, professor, offlineModeDTO);
        offlineModeDTO.setUserIdentifier(UserIdentifier.PROFESSOR);
        offlineModeDTO.setOfficeNumber(professor.getOfficeNumber());
        offlineModeDTO.setAcademicLevel(professor.getAcademicLevel());
        offlineModeDTO.setAcademicRole(professor.getAcademicRole());
        offlineModeDTO.setTemporaryDeputy(professor.isTemporaryDeputy());
        offlineModeDTO.setCourseDTOs(WeeklyScheduleUtils.getProfessorCourseDTOs(databaseManager, professor.getId()));
    }

    private static void initializeCommonUserFields(DatabaseManager databaseManager, User user, OfflineModeDTO offlineModeDTO) {
        offlineModeDTO.setLastLogin(user.getLastLogin());
        offlineModeDTO.setName(user.fetchName());
        offlineModeDTO.setEmailAddress(user.getEmailAddress());
        offlineModeDTO.setProfilePicture(user.getProfilePicture());
        offlineModeDTO.setId(user.getId());
        offlineModeDTO.setNationalId(user.getNationalId());
        offlineModeDTO.setPhoneNumber(user.getPhoneNumber());
        offlineModeDTO.setDepartmentId(user.getDepartmentId());

        offlineModeDTO.setConversationThumbnailDTOs(
                MessengerViewUtils.getConversationThumbnailDTOs(databaseManager, user.getId())
        );

        offlineModeDTO.setOfflineMessengerDTO(
                OfflineMessengerUtils.getOfflineMessengerDTO(databaseManager, user.getMessenger())
        );
    }
}
