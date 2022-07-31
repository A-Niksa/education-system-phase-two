package server.network.clienthandling;

import shareables.models.pojos.users.User;
import shareables.models.pojos.users.UserIdentifier;
import shareables.models.pojos.users.students.Student;
import shareables.models.pojos.users.students.StudentStatus;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.login.LoginUtils;
import shareables.network.requests.Request;

import java.util.Date;

public class RequestHandler {
    private DatabaseManager databaseManager;
    private ResponseHandler responseHandler;

    public RequestHandler(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        responseHandler = new ResponseHandler();
    }

    public void login(ClientHandler clientHandler, Request request) {
//        User user = databaseManager.fetch(User.class, (String) request.get("username"));
        User user = null;
        // TODO
        if (user == null ||
                !request.get("username").equals(user.getId()) || !request.get("password").equals(user.getPassword())) {
            responseHandler.wrongUsernameOrPassword(clientHandler);
        } else if (!request.get("captcha").equals(request.get("currentCaptcha"))) {
            responseHandler.wrongCaptcha(clientHandler);
        } else if (LoginUtils.hasBeenTooLongSinceLastLogin(user)) {
            responseHandler.shouldChangePassword(clientHandler);
        } else if (user.getUserIdentifier() == UserIdentifier.STUDENT) {
            Student student = (Student) user;
            if (student.getStudentStatus() == StudentStatus.DROPPED_OUT) {
                responseHandler.droppedOut(clientHandler); // TODO: test this
            }
        } else {
            user.setLastLogin(new Date()); // setting last login to now
            responseHandler.validLogin(clientHandler, user.getUserIdentifier());
        }
    }

    public void changePassword(ClientHandler clientHandler, Request request) {
//        User user = databaseManager.fetch(User.class, (String) request.get("username"));
        User user = null;
        // TODO
        if (user == null || user.getPassword().equals(request.get("newPassword"))) {
            responseHandler.newPasswordIsSameAsOldOne(clientHandler);
        } else {
            LoginUtils.changePassword(user, (String) request.get("newPassword"), databaseManager);
            responseHandler.requestSuccessful(clientHandler);
        }
    }
}