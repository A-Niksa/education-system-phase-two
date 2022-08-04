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
            case GET_DEFENSE_TIME:
                requestHandler.getDefenseTime(clientHandler, request);
                break;
            case ASK_FOR_DEFENSE_TIME:
                requestHandler.askForDefenseTime(clientHandler, request);
                break;
            case GET_DROPPING_OUT_SUBMISSION_STATUS:
                requestHandler.getDroppingOutSubmissionStatus(clientHandler, request);
                break;
            case ASK_FOR_DROPPING_OUT:
                requestHandler.askForDroppingOut(clientHandler, request);
                break;
            case ASK_FOR_RECOMMENDATION:
                requestHandler.askForRecommendation(clientHandler, request);
                break;
            case GET_STUDENT_RECOMMENDATION_TEXTS:
                requestHandler.getStudentRecommendationTexts(clientHandler, request);
                break;
            case GET_PROFESSOR_RECOMMENDATION_REQUEST_DTOS:
                requestHandler.getProfessorRecommendationRequestDTOs(clientHandler, request);
                break;
            case GET_STUDENT_MINOR_REQUEST_DTOS:
                requestHandler.getStudentMinorRequestDTOs(clientHandler, request);
                break;
            case ASK_FOR_MINOR:
                requestHandler.askForMinor(clientHandler, request);
                break;
            case GET_PROFESSOR_MINOR_REQUEST_DTOS:
                requestHandler.getProfessorMinorRequestDTOs(clientHandler, request);
                break;
            case ACCEPT_MINOR_REQUEST:
                requestHandler.acceptMinorRequest(clientHandler, request);
                break;
            case DECLINE_MINOR_REQUEST:
                requestHandler.declineMinorRequest(clientHandler, request);
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
            case GET_DEPARTMENT_DROPPING_OUT_REQUEST_DTOS:
                requestHandler.getDepartmentDroppingOutRequestDTOs(clientHandler, request);
                break;
            case ACCEPT_DROPPING_OUT_REQUEST:
                requestHandler.acceptDroppingOutRequest(clientHandler, request);
                break;
            case DECLINE_DROPPING_OUT_REQUEST:
                requestHandler.declineDroppingOutRequest(clientHandler, request);
                break;
            case ACCEPT_RECOMMENDATION_REQUEST:
                requestHandler.acceptRecommendationRequest(clientHandler, request);
                break;
            case DECLINE_RECOMMENDATION_REQUEST:
                requestHandler.declineRecommendationRequest(clientHandler, request);
                break;
            case GET_STUDENT_TRANSCRIPT_DTO:
                requestHandler.getStudentTranscriptDTO(clientHandler, request);
                break;
            case GET_STUDENT_COURSE_SCORE_DTOS:
                requestHandler.getStudentCourseScoreDTOs(clientHandler, request);
                break;
            case GET_STUDENT_TEMPORARY_COURSE_SCORE_DTOS:
                requestHandler.getStudentTemporaryCourseScoreDTOs(clientHandler, request);
                break;
            case SUBMIT_PROTEST:
                requestHandler.submitProtest(clientHandler, request);
                break;
            case GET_PROFESSOR_ACTIVE_COURSE_NAMES:
                requestHandler.getProfessorActiveCourseNames(clientHandler, request);
                break;
            case GET_COURSE_SCORE_DTOS_FOR_COURSE:
                requestHandler.getCourseScoreDTOsForCourse(clientHandler, request);
                break;
            case RESPOND_TO_PROTEST:
                requestHandler.respondToProtest(clientHandler, request);
                break;
            case SAVE_TEMPORARY_SCORES:
                requestHandler.saveTemporaryScores(clientHandler, request);
                break;
            case FINALIZE_SCORES:
                requestHandler.finalizeScores(clientHandler, request);
                break;
            case GET_COURSE_SCORE_DTOS_FOR_PROFESSOR:
                requestHandler.getCourseScoreDTOsForProfessor(clientHandler, request);
                break;
            case GET_COURSE_SCORE_DTOS_FOR_STUDENT:
                requestHandler.getCourseScoreDTOsForStudent(clientHandler, request);
                break;
            case GET_DEPARTMENT_COURSE_NAMES:
                requestHandler.getDepartmentCourseNames(clientHandler, request);
                break;
            case GET_DEPARTMENT_PROFESSOR_NAMES:
                requestHandler.getDepartmentProfessorNames(clientHandler, request);
                break;
            case GET_DEPARTMENT_STUDENT_IDS:
                requestHandler.getDepartmentStudentIds(clientHandler, request);
                break;
        }
    }
}
