package server.network.clienthandling;

import shareables.models.pojos.academicrequests.AcademicRequestStatus;
import shareables.models.pojos.coursewares.EducationalItem;
import shareables.models.pojos.media.MediaFile;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.UserIdentifier;
import shareables.network.DTOs.academicrequests.RequestDTO;
import shareables.network.DTOs.coursewares.CalendarEventDTO;
import shareables.network.DTOs.coursewares.MaterialThumbnailDTO;
import shareables.network.DTOs.messaging.ContactProfileDTO;
import shareables.network.DTOs.messaging.ConversationDTO;
import shareables.network.DTOs.messaging.ConversationThumbnailDTO;
import shareables.network.DTOs.generalmodels.CourseDTO;
import shareables.network.DTOs.generalmodels.ProfessorDTO;
import shareables.network.DTOs.generalmodels.StudentDTO;
import shareables.network.DTOs.notifications.NotificationDTO;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.DTOs.standing.CourseScoreDTO;
import shareables.network.DTOs.standing.CourseStatsDTO;
import shareables.network.DTOs.standing.TranscriptDTO;
import shareables.network.DTOs.unitselection.CourseThumbnailDTO;
import shareables.network.requests.Request;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import java.time.LocalDateTime;
import java.util.List;

public class ResponseHandler {
    public void requestSuccessful(ClientHandler clientHandler) {
        Response response = new Response(ResponseStatus.OK);
        clientHandler.respond(response);
    }

    public void offlineModeDTOAcquired(ClientHandler clientHandler, OfflineModeDTO offlineModeDTO) {
        Response response = new Response(ResponseStatus.OK);
        response.put("offlineModeDTO", offlineModeDTO);
        clientHandler.respond(response);
    }

    public void wrongUsernameOrPassword(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "wrongUsernameOrPassword"));
        clientHandler.respond(response);
    }

    public void wrongCaptcha(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS, "wrongCaptcha"));
        clientHandler.respond(response);
    }

    public void shouldChangePassword(ClientHandler clientHandler) {
        Response response =
                new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS, "hasBeenTooLongSinceLastLogin"));
        clientHandler.respond(response);
    }

    public void droppedOut(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS, "droppedOut"));
        clientHandler.respond(response);
    }

    public void validLogin(ClientHandler clientHandler, UserIdentifier userIdentifier) {
        Response response = new Response(ResponseStatus.OK);
        response.put("userIdentifier", userIdentifier);
        clientHandler.respond(response);
    }

    public void newPasswordIsSameAsOldOne(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS, "newPasswordIsSameAsOldOne"));
        clientHandler.respond(response);
    }

    public void userAcquired(ClientHandler clientHandler, User user) {
        Response response = new Response(ResponseStatus.OK);
        response.put("user", user);
        clientHandler.respond(response);
    }

    public void advisingProfessorNameAcquired(ClientHandler clientHandler, String advisingProfessorName) {
        Response response = new Response(ResponseStatus.OK);
        response.put("advisingProfessorName", advisingProfessorName);
        clientHandler.respond(response);
    }

    public void courseDTOsAcquired(ClientHandler clientHandler, List<CourseDTO> courseDTOs) {
        Response response = new Response(ResponseStatus.OK);
        response.put("courseDTOs", courseDTOs);
        clientHandler.respond(response);
    }

    public void professorDTOsAcquired(ClientHandler clientHandler, List<ProfessorDTO> professorDTOs) {
        Response response = new Response(ResponseStatus.OK);
        response.put("professorDTOs", professorDTOs);
        clientHandler.respond(response);
    }

    public void dormRequestDetermined(ClientHandler clientHandler, boolean willGetDorm) {
        Response response = new Response(ResponseStatus.OK);
        response.put("willGetDorm", willGetDorm);
        clientHandler.respond(response);
    }

    public void certificateGenerated(ClientHandler clientHandler, String certificateText) {
        Response response = new Response(ResponseStatus.OK);
        response.put("certificateText", certificateText);
        clientHandler.respond(response);
    }

    public void defenseTimeNotFound(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS, "defenseTimeNotFound"));
        clientHandler.respond(response);
    }

    public void defenseTimeAcquired(ClientHandler clientHandler, LocalDateTime defenseTime) {
        Response response = new Response(ResponseStatus.OK);
        response.put("defenseTime", defenseTime);
        clientHandler.respond(response);
    }

    public void professorsDoNotExistInDepartment(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "professorsDoNotExistInDepartment"));
        clientHandler.respond(response);
    }

    public void prerequisitesDoNotExist(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "prerequisitesDoNotExist"));
        clientHandler.respond(response);
    }

    public void corequisitesDoNotExist(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "corequisitesDoNotExist"));
        clientHandler.respond(response);
    }

    public void studentTAsDoNotExist(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "studentTAsDoNotExist"));
        clientHandler.respond(response);
    }

    public void studentsDoNotExistInDepartment(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "studentsDoNotExistInDepartment"));
        clientHandler.respond(response);
    }

    public void atLeastOneStudentAlreadyHasAnAdvisor(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "atLeastOneStudentAlreadyHasAnAdvisor"));
        clientHandler.respond(response);
    }

    public void courseAdded(ClientHandler clientHandler, String courseId) {
        Response response = new Response(ResponseStatus.OK, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "addedCourseFirstHalf") + courseId +
                ConfigManager.getString(ConfigFileIdentifier.TEXTS, "addedCourseSecondHalf"));
        clientHandler.respond(response);
    }

    public void professorAdded(ClientHandler clientHandler, String professorId) {
        Response response = new Response(ResponseStatus.OK, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "addedProfessorFirstHalf") + professorId +
                ConfigManager.getString(ConfigFileIdentifier.TEXTS, "addedProfessorSecondHalf"));
        clientHandler.respond(response);
    }

    public void studentAdded(ClientHandler clientHandler, String studentId) {
        Response response = new Response(ResponseStatus.OK, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "addedStudentFirstHalf") + studentId +
                ConfigManager.getString(ConfigFileIdentifier.TEXTS, "addedStudentSecondHalf"));
        clientHandler.respond(response);
    }

    public void dateIsSoonerThanNow(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS, "dateIsSoonerThanNow"));
        clientHandler.respond(response);
    }

    public void droppingOutSubmissionStatusAcquired(ClientHandler clientHandler, boolean academicRequestHasBeenSubmitted,
                                                    AcademicRequestStatus academicRequestStatus) {
        Response response = new Response(ResponseStatus.OK);
        response.put("academicRequestHasBeenSubmitted", academicRequestHasBeenSubmitted);
        response.put("academicRequestStatus", academicRequestStatus);
        clientHandler.respond(response);
    }

    public void requestDTOsAcquired(ClientHandler clientHandler, List<RequestDTO> requestDTOs) {
        Response response = new Response(ResponseStatus.OK);
        response.put("requestDTOs", requestDTOs);
        clientHandler.respond(response);
    }

    public void professorDoesNotExist(ClientHandler clientHandler, String professorId) {
        Response response = new Response(
                ConfigManager.getString(ConfigFileIdentifier.TEXTS, "professorDoesNotExistFirstHalf") +
                professorId + ConfigManager.getString(ConfigFileIdentifier.TEXTS, "professorDoesNotExistSecondHalf"));
        clientHandler.respond(response);
    }

    public void recommendationTextsAcquired(ClientHandler clientHandler, List<String> recommendationTexts) {
        Response response = new Response(ResponseStatus.OK);
        response.put("recommendationTexts", recommendationTexts);
        clientHandler.respond(response);
    }

    public void studentGPAIsNotHighEnough(ClientHandler clientHandler) {
        Response response = new Response(
                ConfigManager.getString(ConfigFileIdentifier.TEXTS, "GPANotHighEnoughFirstHalf")
                + ConfigManager.getDouble(ConfigFileIdentifier.CONSTANTS, "minimumMinorGPA")
                + ConfigManager.getString(ConfigFileIdentifier.TEXTS, "GPANotHighEnoughSecondHalf"));
        clientHandler.respond(response);
    }

    public void targetDepartmentIsTheCurrentDepartment(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "targetDepartmentIsTheCurrentDepartment"));
        clientHandler.respond(response);
    }

    public void currentlyMinoringAtTargetDepartment(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "currentlyMinoringAtTargetDepartment"));
        clientHandler.respond(response);
    }

    public void cannotMinorAtTwoPlaces(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "cannotMinorAtTwoPlaces"));
        clientHandler.respond(response);
    }

    public void transcriptDTOAcquired(ClientHandler clientHandler, TranscriptDTO transcriptDTO) {
        Response response = new Response(ResponseStatus.OK);
        response.put("transcriptDTO", transcriptDTO);
        clientHandler.respond(response);
    }

    public void courseScoreDTOsAcquired(ClientHandler clientHandler, List<CourseScoreDTO> courseScoreDTOs) {
        Response response = new Response(ResponseStatus.OK);
        response.put("courseScoreDTOs", courseScoreDTOs);
        clientHandler.respond(response);
    }

    public void stringArrayAcquired(ClientHandler clientHandler, String[] stringArray) {
        Response response = new Response(ResponseStatus.OK);
        response.put("stringArray", stringArray);
        clientHandler.respond(response);
    }

    public void notAllStudentsHaveBeenGivenTemporaryScores(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "notAllStudentsHaveBeenGivenTemporaryScores"));
        clientHandler.respond(response);
    }

    public void courseStatsDTOAcquired(ClientHandler clientHandler, CourseStatsDTO courseStatsDTO) {
        Response response = new Response(ResponseStatus.OK);
        response.put("courseStatsDTO", courseStatsDTO);
        clientHandler.respond(response);
    }

    public void notAllStudentScoresHaveBeenFinalized(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "notAllStudentScoresHaveBeenFinalized"));
        clientHandler.respond(response);
    }

    public void professorIsNotDepartmentDeputy(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "professorIsNotDepartmentDeputy"));
        clientHandler.respond(response);
    }

    public void professorIsAlreadyDepartmentDeputy(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "professorIsAlreadyDepartmentDeputy"));
        clientHandler.respond(response);
    }

    public void departmentAlreadyHasDeputy(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "departmentAlreadyHasDeputy"));
        clientHandler.respond(response);
    }

    public void cannotRemoveDepartmentDean(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "cannotRemoveDepartmentDean"));
        clientHandler.respond(response);
    }

    public void requestSuccessfulButDeanBecomesTemporaryDeputy(ClientHandler clientHandler) {
        Response response = new Response(ResponseStatus.OK, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "deputyResponsibilitiesDelegatedToDean"));
        clientHandler.respond(response);
    }

    public void cannotPromoteDeanToDeputy(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "cannotPromoteDeanToDeputy"));
        clientHandler.respond(response);
    }

    public void conversationThumbnailDTOsAcquired(ClientHandler clientHandler,
                                                  List<ConversationThumbnailDTO> conversationThumbnailDTOs) {
        Response response = new Response(ResponseStatus.OK);
        response.put("conversationThumbnailDTOs", conversationThumbnailDTOs);
        clientHandler.respond(response);
    }

    public void conversationDTOAcquired(ClientHandler clientHandler, ConversationDTO conversationDTO) {
        Response response = new Response(ResponseStatus.OK);
        response.put("conversationDTO", conversationDTO);
        clientHandler.respond(response);
    }

    public void mediaFileAcquired(ClientHandler clientHandler, MediaFile mediaFile) {
        Response response = new Response(ResponseStatus.OK);
        response.put("mediaFile", mediaFile);
        clientHandler.respond(response);
    }

    public void mediaFileDoesNotExist(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "mediaFileDoesNotExist"));
        clientHandler.respond(response);
    }

    public void contactProfileDTOsAcquired(ClientHandler clientHandler, List<ContactProfileDTO> contactProfileDTOs) {
        Response response = new Response(ResponseStatus.OK);
        response.put("contactProfileDTOs", contactProfileDTOs);
        clientHandler.respond(response);
    }

    public void atLeastOneContactDoesNotExist(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "atLeastOneContactDoesNotExist"));
        clientHandler.respond(response);
    }

    public void messageNotificationsSent(ClientHandler clientHandler) {
        Response response = new Response(ResponseStatus.OK, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "messageNotificationsSent"));
        clientHandler.respond(response);
    }

    public void notificationDTOsAcquired(ClientHandler clientHandler, List<NotificationDTO> notificationDTOs) {
        Response response = new Response(ResponseStatus.OK);
        response.put("notificationDTOs", notificationDTOs);
        clientHandler.respond(response);
    }

    public void notificationIsNotRequest(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "notificationIsNotRequest"));
        clientHandler.respond(response);
    }

    public void notificationHasAlreadyBeenDecidedOn(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "notificationHasAlreadyBeenDecidedOn"));
        clientHandler.respond(response);
    }

    public void cannotContactOnesSelf(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "cannotContactOnesSelf"));
        clientHandler.respond(response);
    }

    public void studentDTOAcquired(ClientHandler clientHandler, StudentDTO studentDTO) {
        Response response = new Response(ResponseStatus.OK);
        response.put("studentDTO", studentDTO);
        clientHandler.respond(response);
    }

    public void contactIdsAcquired(ClientHandler clientHandler, List<String> contactIds) {
        Response response = new Response(ResponseStatus.OK);
        response.put("contactIds", contactIds);
        clientHandler.respond(response);
    }

    public void startsSoonerThanNow(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS, "startsSoonerThanNow"));
        clientHandler.respond(response);
    }

    public void endsSoonerThanNow(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS, "endsSoonerThanNow"));
        clientHandler.respond(response);
    }

    public void endIsSoonerThanStart(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "endIsSoonerThanStart"));
        clientHandler.respond(response);
    }

    public void addedUnitSelectionTime(ClientHandler clientHandler) {
        Response response = new Response(ResponseStatus.OK, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "addedUnitSelectionTime"));
        clientHandler.respond(response);
    }

    public void courseThumbnailDTOsAcquired(ClientHandler clientHandler, List<CourseThumbnailDTO> courseThumbnailDTOs) {
        Response response = new Response(ResponseStatus.OK);
        response.put("courseThumbnailDTOs", courseThumbnailDTOs);
        clientHandler.respond(response);
    }

    public void courseHasNoCapacity(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "courseHasNoCapacity"));
        clientHandler.respond(response);
    }

    public void studentDoesNotSatisfyPrerequisites(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "studentDoesNotSatisfyPrerequisites"));
        clientHandler.respond(response);
    }

    public void classTimesCollideWithStudentClasses(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "classTimesCollideWithStudentClasses"));
        clientHandler.respond(response);
    }

    public void examDateCollidesWithStudentExams(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "examDateCollidesWithStudentExams"));
        clientHandler.respond(response);
    }

    public void cannotHaveTwoOrMoreTheologyCourses(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "cannotHaveTwoOrMoreTheologyCourses"));
        clientHandler.respond(response);
    }

    public void courseAcquired(ClientHandler clientHandler) {
        Response response = new Response(ResponseStatus.OK, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "courseAcquired"));
        clientHandler.respond(response);
    }

    public void removedCourseFromUnitSelectionAcquisitions(ClientHandler clientHandler) {
        Response response = new Response(ResponseStatus.OK, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "removedCourseFromAcquisitions"));
        clientHandler.respond(response);
    }

    public void coursePinnedToFavorites(ClientHandler clientHandler) {
        Response response = new Response(ResponseStatus.OK, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "coursePinnedToFavorites"));
        clientHandler.respond(response);
    }

    public void courseUnpinnedFromFavorites(ClientHandler clientHandler) {
        Response response = new Response(ResponseStatus.OK, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "courseUnpinnedFromFavorites"));
        clientHandler.respond(response);
    }

    public void courseAcquisitionRequestSent(ClientHandler clientHandler) {
        Response response = new Response(ResponseStatus.OK, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "courseAcquisitionRequestSent"));
        clientHandler.respond(response);
    }

    public void newGroupNumberIsSameAsPreviousGroupNumber(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "newGroupNumberIsSameAsPreviousGroupNumber"));
        clientHandler.respond(response);
    }

    public void courseGroupDoesNotExist(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "courseGroupDoesNotExist"));
        clientHandler.respond(response);
    }

    public void requestedGroupChangeDueToCourseLimitation(ClientHandler clientHandler) {
        Response response = new Response(ResponseStatus.OK, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "requestedGroupChangeDueToCourseLimitation"));
        clientHandler.respond(response);
    }

    public void changedCourseGroupNumber(ClientHandler clientHandler) {
        Response response = new Response(ResponseStatus.OK, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "changedCourseGroupNumber"));
        clientHandler.respond(response);
    }

    public void calendarEventDTOsAcquired(ClientHandler clientHandler, List<CalendarEventDTO> calendarEventDTOs) {
        Response response = new Response(ResponseStatus.OK);
        response.put("calendarEventDTOs", calendarEventDTOs);
        clientHandler.respond(response);
    }

    public void materialThumbnailDTOsAcquired(ClientHandler clientHandler, List<MaterialThumbnailDTO> materialThumbnailDTOs) {
        Response response = new Response(ResponseStatus.OK);
        response.put("materialThumbnailDTOs", materialThumbnailDTOs);
        clientHandler.respond(response);
    }

    public void studentDoesNotExist(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS, "studentDoesNotExist"));
        clientHandler.respond(response);
    }

    public void educationalItemsAcquired(ClientHandler clientHandler, List<EducationalItem> educationalItems) {
        Response response = new Response(ResponseStatus.OK);
        response.put("educationalItems", educationalItems);
        clientHandler.respond(response);
    }
}