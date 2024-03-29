package server.network.clienthandling;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.unitselection.validation.UnitSelectionManager;
import shareables.network.requests.Request;
import shareables.network.requests.RequestIdentifier;

public class RequestMapper {
    private DatabaseManager databaseManager;
    private RequestHandler requestHandler;
    private UnitSelectionManager unitSelectionManager;

    public RequestMapper(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;

        requestHandler = new RequestHandler(databaseManager);
        unitSelectionManager = new UnitSelectionManager();
    }

    public void mapRequestToHandlerMethod(ClientHandler clientHandler, Request request) {
        unitSelectionManager.validateAnyUnitSelectionSessionsIfNecessary(databaseManager);

        RequestIdentifier requestIdentifier = request.getRequestIdentifier();
        switch (requestIdentifier) {
            case CONNECTION_PING:
                requestHandler.respondToConnectionPing(clientHandler);
                break;
            case LOG_IN:
                requestHandler.logIn(clientHandler, request);
                break;
            case GET_OFFLINE_MODE_DTO:
                requestHandler.getOfflineModeDTO(clientHandler, request);
                break;
            case CHANGE_PASSWORD:
                requestHandler.changePassword(clientHandler, request);
                break;
            case GET_USER:
                requestHandler.getUser(clientHandler, request);
                break;
            case GET_ADVISING_PROFESSOR_NAME:
                requestHandler.getAdvisingProfessorName(clientHandler, request);
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
            case GET_DEPARTMENT_PROFESSOR_DTOS:
                requestHandler.getDepartmentProfessorDTOs(clientHandler, request);
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
            case CHANGE_COURSE_DEGREE_LEVEL:
                requestHandler.changeCourseDegreeLevel(clientHandler, request);
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
            case CHANGE_PROFESSOR_ACADEMIC_LEVEL:
                requestHandler.changeProfessorAcademicLevel(clientHandler, request);
                break;
            case CHANGE_PROFESSOR_OFFICE_NUMBER:
                requestHandler.changeProfessorOfficeNumber(clientHandler, request);
                break;
            case DEMOTE_FROM_DEPUTY:
                requestHandler.demoteProfessorFromDeputyRole(clientHandler, request);
                break;
            case PROMOTE_TO_DEPUTY:
                requestHandler.promoteProfessorToDeputyRole(clientHandler, request);
                break;
            case ADD_PROFESSOR:
                requestHandler.addProfessor(clientHandler, request);
                break;
            case REMOVE_PROFESSOR:
                requestHandler.removeProfessor(clientHandler, request);
                break;
            case ADD_STUDENT:
                requestHandler.addStudent(clientHandler, request);
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
            case GET_STUDENT_TRANSCRIPT_DTO_WITH_ID:
                requestHandler.getStudentTranscriptDTOWithId(clientHandler, request);
                break;
            case GET_STUDENT_TRANSCRIPT_DTO_WITH_NAME:
                requestHandler.getStudentTranscriptDTOWithName(clientHandler, request);
                break;
            case GET_STUDENT_COURSE_SCORE_DTOS_WITH_ID:
                requestHandler.getStudentCourseScoreDTOsWithId(clientHandler, request);
                break;
            case GET_STUDENT_COURSE_SCORE_DTOS_WITH_NAME:
                requestHandler.getStudentCourseScoreDTOsWithName(clientHandler, request);
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
            case GET_DEPARTMENT_STUDENT_NAMES:
                requestHandler.getDepartmentStudentNames(clientHandler, request);
                break;
            case GET_COURSE_STATS_DTO:
                requestHandler.getCourseStatsDTO(clientHandler, request);
                break;
            case GET_CONVERSATION_THUMBNAIL_DTOS:
                requestHandler.getConversationThumbnailDTOs(clientHandler, request);
                break;
            case GET_CONTACT_CONVERSATION_DTO:
                requestHandler.getContactConversationDTO(clientHandler, request);
                break;
            case SEND_TEXT_MESSAGE:
                requestHandler.sendTextMessage(clientHandler, request);
                break;
            case SEND_MEDIA_MESSAGE:
                requestHandler.sendMediaMessage(clientHandler, request);
                break;
            case DOWNLOAD_MEDIA_FROM_CONVERSATION:
                requestHandler.downloadMediaFromConversation(clientHandler, request);
                break;
            case DOWNLOAD_MEDIA_FROM_EDUCATIONAL_MATERIAL:
                requestHandler.downloadMediaFromEducationalMaterial(clientHandler, request);
            case GET_STUDENT_CONTACT_PROFILE_DTOS:
                requestHandler.getStudentContactProfileDTOs(clientHandler, request);
                break;
            case GET_PROFESSOR_CONTACT_PROFILE_DTOS:
                requestHandler.getProfessorContactProfileDTOs(clientHandler, request);
                break;
            case CHECK_IF_CONTACT_IDS_EXIST:
                requestHandler.checkIfContactIdsExist(clientHandler, request);
                break;
            case SEND_MESSAGE_NOTIFICATIONS_IF_NECESSARY:
                requestHandler.sendMessageNotificationsIfNecessary(clientHandler, request);
                break;
            case GET_NOTIFICATION_DTOS:
                requestHandler.getNotificationDTOs(clientHandler, request);
                break;
            case ACCEPT_NOTIFICATION:
                requestHandler.acceptNotification(clientHandler, request);
                break;
            case DECLINE_NOTIFICATION:
                requestHandler.declineNotification(clientHandler, request);
                break;
            case GET_MR_MOHSENI_CONTACT_PROFILE_DTOS:
                requestHandler.getMrMohseniContactProfileDTOs(clientHandler, request);
                break;
            case GET_ALL_STUDENT_CONTACT_PROFILE_DTOS:
                requestHandler.getAllStudentContactProfileDTOs(clientHandler, request);
                break;
            case GET_FILTERED_STUDENT_CONTACT_PROFILE_DTOS:
                requestHandler.getFilteredStudentContactProfileDTOs(clientHandler, request);
                break;
            case GET_STUDENT_DTO:
                requestHandler.getStudentDTO(clientHandler, request);
                break;
            case GET_FILTERED_CONTACT_IDS:
                requestHandler.getFilteredContactIds(clientHandler, request);
                break;
            case ADD_UNIT_SELECTION_SESSION:
                requestHandler.addUnitSelectionSession(clientHandler, request);
                break;
            case GET_DEPARTMENT_COURSE_THUMBNAIL_DTOS:
                requestHandler.getDepartmentCourseThumbnailDTOs(clientHandler, request);
                break;
            case GET_DEPARTMENT_COURSE_THUMBNAIL_DTOS_ALPHABETICALLY:
                requestHandler.getDepartmentCourseThumbnailDTOsAlphabetically(clientHandler, request);
                break;
            case GET_DEPARTMENT_COURSE_THUMBNAIL_DTOS_IN_EXAM_DATE_ORDER:
                requestHandler.getDepartmentCourseThumbnailDTOsInExamDateOrder(clientHandler, request);
                break;
            case GET_DEPARTMENT_COURSE_THUMBNAIL_DTOS_IN_DEGREE_LEVEL_ORDER:
                requestHandler.getDepartmentCourseThumbnailDTOsInDegreeLevelOrder(clientHandler, request);
                break;
            case ACQUIRE_COURSE:
                requestHandler.acquireCourse(clientHandler, request);
                break;
            case REMOVE_ACQUIRED_COURSE:
                requestHandler.removeAcquiredCourse(clientHandler, request);
                break;
            case PIN_COURSE_TO_FAVORITES:
                requestHandler.pinCourseToFavorites(clientHandler, request);
                break;
            case UNPIN_COURSE_FROM_FAVORITES:
                requestHandler.unpinCourseFromFavorites(clientHandler, request);
                break;
            case REQUEST_COURSE_ACQUISITION:
                requestHandler.requestCourseAcquisition(clientHandler, request);
                break;
            case GET_COURSE_GROUPS_THUMBNAIL_DTOS:
                requestHandler.getCourseGroupsThumbnailDTOs(clientHandler, request);
                break;
            case CHANGE_GROUP_NUMBER:
                requestHandler.changeGroupNumber(clientHandler, request);
                break;
            case GET_PINNED_COURSE_THUMBNAIL_DTOS:
                requestHandler.getPinnedCourseThumbnailDTOs(clientHandler, request);
                break;
            case GET_STUDENT_COURSEWARE_THUMBNAIL_DTOS:
                requestHandler.getStudentCoursewareThumbnailDTOs(clientHandler, request);
                break;
            case GET_PROFESSOR_COURSEWARE_THUMBNAIL_DTOS:
                requestHandler.getProfessorCoursewareThumbnailDTOs(clientHandler, request);
                break;
            case GET_CALENDAR_EVENT_DTOS:
                requestHandler.getCalendarEventDTOs(clientHandler, request);
                break;
            case GET_MATERIAL_THUMBNAIL_DTOS:
                requestHandler.getMaterialThumbnailDTOs(clientHandler, request);
                break;
            case GET_HOMEWORK_THUMBNAIL_DTOS:
                requestHandler.getHomeworkThumbnailDTOs(clientHandler, request);
                break;
            case ADD_STUDENT_TO_COURSE:
                requestHandler.addStudentToCourse(clientHandler, request);
                break;
            case ADD_TA_TO_COURSE:
                requestHandler.addTeachingAssistantToCourse(clientHandler, request);
                break;
            case ADD_EDUCATIONAL_MATERIAL_ITEMS:
                requestHandler.saveEducationalMaterialItems(clientHandler, request);
                break;
            case EDIT_EDUCATIONAL_MATERIAL_ITEMS:
                requestHandler.editEducationalMaterialItems(clientHandler, request);
                break;
            case GET_COURSE_MATERIAL_EDUCATIONAL_ITEMS:
                requestHandler.getCourseMaterialEducationalItems(clientHandler, request);
                break;
            case REMOVE_COURSE_EDUCATIONAL_MATERIAL:
                requestHandler.removeCourseEducationalMaterial(clientHandler, request);
                break;
            case REMOVE_ALL_COURSE_EDUCATIONAL_MATERIALS:
                requestHandler.removeAllCourseEducationalMaterials(clientHandler, request);
                break;
            case GET_TEACHING_ASSISTANCE_STATUS:
                requestHandler.getTeachingAssistanceStatus(clientHandler, request);
                break;
            case SAVE_HOMEWORK:
                requestHandler.saveHomework(clientHandler, request);
                break;
            case GET_HOMEWORK_DESCRIPTION:
                requestHandler.getHomeworkDescription(clientHandler, request);
                break;
            case GET_HOMEWORK_PDF:
                requestHandler.getHomeworkPDF(clientHandler, request);
                break;
            case GET_HOMEWORK_SUBMISSION_TYPE:
                requestHandler.getHomeworkSubmissionType(clientHandler, request);
                break;
            case SUBMIT_HOMEWORK_TEXT:
                requestHandler.submitHomeworkText(clientHandler, request);
                break;
            case SUBMIT_HOMEWORK_MEDIA:
                requestHandler.submitHomeworkMedia(clientHandler, request);
                break;
            case GET_SUBMISSION_THUMBNAIL_DTOS:
                requestHandler.getSubmissionThumbnailDTOs(clientHandler, request);
                break;
            case GET_SUBMISSION_TEXT:
                requestHandler.getSubmissionText(clientHandler, request);
                break;
            case DOWNLOAD_SUBMISSION_MEDIA_FILE:
                requestHandler.getSubmissionMediaFile(clientHandler, request);
                break;
            case SCORE_STUDENT_SUBMISSION:
                requestHandler.scoreStudentSubmission(clientHandler, request);
                break;
            case GET_STUDENT_GLOBAL_CALENDAR_EVENT_DTOS:
                requestHandler.getStudentGlobalCalendarEventDTOs(clientHandler, request);
                break;
            case GET_PROFESSOR_GLOBAL_CALENDAR_EVENT_DTOS:
                requestHandler.getProfessorGlobalCalendarEventDTOs(clientHandler, request);
                break;
            case GET_STUDENT_SCORE:
                requestHandler.getStudentScore(clientHandler, request);
                break;
            case CHECK_DEADLINE_CONSTRAINTS:
                requestHandler.checkDeadlineConstraints(clientHandler, request);
                break;
        }
    }
}