package server.network.clienthandling;

import shareables.models.pojos.users.User;
import shareables.models.pojos.users.UserIdentifier;
import shareables.network.DTOs.CourseDTO;
import shareables.network.DTOs.ProfessorDTO;
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

    public void courseAdded(ClientHandler clientHandler, String id) {
        Response response = new Response(ResponseStatus.OK, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "addedCourseFirstHalf") + id +
                ConfigManager.getString(ConfigFileIdentifier.TEXTS, "addedCourseSecondHalf"));
        clientHandler.respond(response);
    }

    public void dateIsSoonerThanNow(ClientHandler clientHandler) {
        Response response = new Response(ConfigManager.getString(ConfigFileIdentifier.TEXTS, "dateIsSoonerThanNow"));
        clientHandler.respond(response);
    }
}
