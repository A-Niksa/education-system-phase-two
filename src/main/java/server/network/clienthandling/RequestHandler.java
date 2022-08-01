package server.network.clienthandling;

import shareables.models.pojos.users.User;
import shareables.models.pojos.users.UserIdentifier;
import shareables.models.pojos.users.students.Student;
import shareables.models.pojos.users.students.StudentStatus;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.login.LoginUtils;
import shareables.network.requests.Request;

import java.util.Date;

public class RequestHandler { // TODO: logging, perhaps?
    private DatabaseManager databaseManager;
    private ResponseHandler responseHandler;

    public RequestHandler(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        responseHandler = new ResponseHandler();
    }

    public void logIn(ClientHandler clientHandler, Request request) {
        User user = LoginUtils.getUser((String) request.get("username"), databaseManager);
        if (user == null ||
                !request.get("username").equals(user.getId()) || !request.get("password").equals(user.getPassword())) {
            responseHandler.wrongUsernameOrPassword(clientHandler);
        } else if (!request.get("captcha").equals(request.get("currentCaptcha"))) {
            responseHandler.wrongCaptcha(clientHandler);
        } else if (LoginUtils.hasBeenTooLongSinceLastLogin(user)) { // TODO: set proper time in config
            responseHandler.shouldChangePassword(clientHandler);
        } else if (user.getUserIdentifier() == UserIdentifier.STUDENT &&
                ((Student) user).getStudentStatus() == StudentStatus.DROPPED_OUT) {
            responseHandler.droppedOut(clientHandler);
        } else {
            user.updateLastLogin(); // setting last login to now
            responseHandler.validLogin(clientHandler, user.getUserIdentifier());
        }
    }

    public void changePassword(ClientHandler clientHandler, Request request) {
        User user = LoginUtils.getUser((String) request.get("username"), databaseManager);
        if (user == null || user.getPassword().equals(request.get("newPassword"))) {
            responseHandler.newPasswordIsSameAsOldOne(clientHandler);
        } else {
            LoginUtils.changePassword(user, (String) request.get("newPassword"), databaseManager);
            responseHandler.requestSuccessful(clientHandler);
        }
    }

    public void getUser(ClientHandler clientHandler, Request request) {
        User user = LoginUtils.getUser((String) request.get("username"), databaseManager);
        responseHandler.userAcquired(clientHandler, user);
    }

    public void changeEmailAddress(ClientHandler clientHandler, Request request) {
        User user = LoginUtils.getUser((String) request.get("username"), databaseManager);
        user.setEmailAddress((String) request.get("newEmailAddress"));
        responseHandler.requestSuccessful(clientHandler);
    }

    public void changePhoneNumber(ClientHandler clientHandler, Request request) {
        User user = LoginUtils.getUser((String) request.get("username"), databaseManager);
        user.setPhoneNumber((String) request.get("newPhoneNumber"));
        responseHandler.requestSuccessful(clientHandler);
    }
}