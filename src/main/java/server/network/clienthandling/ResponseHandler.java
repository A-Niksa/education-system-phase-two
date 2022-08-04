package server.network.clienthandling;

import shareables.models.pojos.users.User;
import shareables.models.pojos.users.UserIdentifier;
import shareables.network.DTOs.CourseDTO;
import shareables.network.DTOs.ProfessorDTO;
import shareables.network.DTOs.RequestDTO;
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

    public void courseAdded(ClientHandler clientHandler, String courseId) {
        Response response = new Response(ResponseStatus.OK, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "addedCourseFirstHalf") + courseId +
                ConfigManager.getString(ConfigFileIdentifier.TEXTS, "addedCourseSecondHalf"));
        clientHandler.respond(response);
    }

    public void dateIsSoonerThanNow(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS, "dateIsSoonerThanNow"));
        clientHandler.respond(response);
    }

    public void droppingOutSubmissionStatusAcquired(ClientHandler clientHandler, boolean academicRequestHasBeenSubmitted) {
        Response response = new Response(ResponseStatus.OK);
        response.put("academicRequestHasBeenSubmitted", academicRequestHasBeenSubmitted);
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
}
