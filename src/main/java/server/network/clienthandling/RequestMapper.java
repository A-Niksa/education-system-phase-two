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
            case GET_ACTIVE_COURSE_DTOS:
                requestHandler.getActiveCourseDTOs(clientHandler, request);
                break;
            case GET_DEPARTMENT_COURSE_DTOS:
                requestHandler.getDepartmentCourseDTOs(clientHandler, request);
                break;
            case GET_PROFESSOR_DTOS:
                requestHandler.getProfessorDTOs(clientHandler, request);
                break;
            case ASK_FOR_DORM:
                requestHandler.askForDorm(clientHandler, request);
                break;
            case ASK_FOR_CERTIFICATE:
                requestHandler.askForCertificate(clientHandler, request);
                break;
            case CHANGE_COURSE_NAME:
                requestHandler.changeCourseName(clientHandler, request);
                break;
            case CHANGE_COURSE_NUMBER_OF_CREDITS:
                requestHandler.changeCourseNumberOfCredits(clientHandler, request);
                break;
            case CHANGE_COURSE_LEVEL:
                requestHandler.changeDegreeLevel(clientHandler, request);
                break;
            case CHANGE_COURSE_TEACHING_PROFESSORS:
                requestHandler.changeCourseTeachingProfessors(clientHandler, request);
                break;
            case REMOVE_COURSE:
                requestHandler.removeCourse(clientHandler, request);
                break;
            case ADD_COURSE:
                requestHandler.addCourse(clientHandler, request);
                break;
        }
    }
}
