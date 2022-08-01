package server.network.clienthandling;

import server.database.management.DatabaseManager;
import shareables.network.requests.Request;
import shareables.network.requests.RequestIdentifier;

public class RequestMapper {
    private RequestHandler requestHandler;

    public RequestMapper(DatabaseManager databaseManager) {
        requestHandler = new RequestHandler(databaseManager);
    }

    public void mapRequestToHandlerMethod(ClientHandler clientHandler, Request request) {
        RequestIdentifier requestIdentifier = request.getRequestIdentifier();
        switch (requestIdentifier) {
            case LOG_IN:
                requestHandler.logIn(clientHandler, request);
                break;
            case CHANGE_PASSWORD:
                requestHandler.changePassword(clientHandler, request);
                break;
            case GET_USER:
                requestHandler.getUser(clientHandler, request);
                break;
            case CHANGE_EMAIL_ADDRESS:
                requestHandler.changeEmailAddress(clientHandler, request);
                break;
            case CHANGE_PHONE_NUMBER:
                requestHandler.changePhoneNumber(clientHandler, request);
                break;
            case GET_STUDENT_COURSE_DTOS:
                requestHandler.getStudentCourseDTOs(clientHandler, request);
                break;
            case GET_PROFESSOR_COURSE_DTOS:
                requestHandler.getProfessorCourseDTOs(clientHandler, request);
                break;
        }
    }
}
