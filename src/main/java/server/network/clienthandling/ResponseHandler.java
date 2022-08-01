package server.network.clienthandling;

import shareables.models.pojos.users.User;
import shareables.models.pojos.users.UserIdentifier;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

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
}
