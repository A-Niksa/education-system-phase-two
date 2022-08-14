package client.controller;

import client.locallogic.localdatabase.datamodels.QueuedMessage;
import client.locallogic.localdatabase.datasets.LocalDatasetIdentifier;
import client.network.Client;
import shareables.models.pojos.abstractions.DepartmentName;
import shareables.models.pojos.media.MediaFile;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.models.pojos.users.students.StudentStatus;
import shareables.network.DTOs.messaging.ContactProfileDTO;
import shareables.network.blueprints.Blueprint;
import shareables.network.requests.Request;
import shareables.network.requests.RequestIdentifier;
import shareables.network.responses.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ClientController {
    private Client client;
    private RequestGenerator requestGenerator;

    public ClientController(Client client) {
        this.client = client;
        requestGenerator = new RequestGenerator();
    }

    public void attemptServerConnection() {
        client.startClientNetwork();
    }

    public void startClientLocalDatabaseManager(String currentUserId) {
        client.startLocalDatabaseManager(currentUserId);
    }

    // TODO: having two users log in at the same time

    public void saveLocalDatabase() {
        client.getLocalDatabaseManager().saveDatabase();
    }

    public void loadLocalDatabase() {
        client.getLocalDatabaseManager().loadDatabase();
    }

    public List<QueuedMessage> getQueuedMessagesFromLocalDatabase() {
        return client.getLocalDatabaseManager().getAllQueuedMessages();
    }

    public void submitQueuedMessage(QueuedMessage queuedMessage) {
        client.getLocalDatabaseManager()
                .save(LocalDatasetIdentifier.QUEUED_MESSAGES, queuedMessage);
    }

    public void removeQueuedMessage(QueuedMessage queuedMessage) {
        client.getLocalDatabaseManager()
                .remove(LocalDatasetIdentifier.QUEUED_MESSAGES, queuedMessage.getId());
    }

    public Response logIn(String username, String password, String captcha, String currentCaptcha) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.LOG_IN,
                new StringObjectMap("username", username), new StringObjectMap("password", password),
                new StringObjectMap("captcha", captcha), new StringObjectMap("currentCaptcha", currentCaptcha));
        return client.sendAndListen(request);
    }

    public Response getOfflineModeDTO(String username) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_OFFLINE_MODE_DTO,
                new StringObjectMap("username", username));
        return client.sendAndListen(request);
    }

    public Response changePassword(String username, String newPassword) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.CHANGE_PASSWORD,
                new StringObjectMap("username", username), new StringObjectMap("newPassword", newPassword));
        return client.sendAndListen(request);
    }

    public Response getUser(String username) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_USER,
                new StringObjectMap("username", username));
        return client.sendAndListen(request);
    }

    public Response getAdvisingProfessorName(String studentId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_ADVISING_PROFESSOR_NAME,
                new StringObjectMap("username", studentId));
        return client.sendAndListen(request);
    }

    public Response changeEmailAddress(String username, String newEmailAddress) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.CHANGE_EMAIL_ADDRESS,
                new StringObjectMap("username", username), new StringObjectMap("newEmailAddress", newEmailAddress));
        return client.sendAndListen(request);
    }

    public Response changePhoneNumber(String username, String newPhoneNumber) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.CHANGE_PHONE_NUMBER,
                new StringObjectMap("username", username), new StringObjectMap("newPhoneNumber", newPhoneNumber));
        return client.sendAndListen(request);
    }

    public Response getStudentCourseDTOs(String username) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_STUDENT_COURSE_DTOS,
                new StringObjectMap("username", username));
        return client.sendAndListen(request);
    }

    public Response getProfessorCourseDTOs(String username) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_PROFESSOR_COURSE_DTOS,
                new StringObjectMap("username", username));
        return client.sendAndListen(request);
    }

    public Response getDepartmentCourseDTOs(String departmentId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_DEPARTMENT_COURSE_DTOS,
                new StringObjectMap("departmentId", departmentId));
        return client.sendAndListen(request);
    }

    public Response getDepartmentProfessorDTOs(String departmentId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_DEPARTMENT_PROFESSOR_DTOS,
                new StringObjectMap("departmentId", departmentId));
        return client.sendAndListen(request);
    }

    public Response askForDorm() {
        Request request = requestGenerator.generateRequest(RequestIdentifier.ASK_FOR_DORM);
        return client.sendAndListen(request);
    }

    public Response askForCertificate(String username) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.ASK_FOR_CERTIFICATE,
                new StringObjectMap("username", username));
        return client.sendAndListen(request);
    }

    public Response getDefenseTime(String username) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_DEFENSE_TIME,
                new StringObjectMap("username", username));
        return client.sendAndListen(request);
    }

    public Response askForDefenseTime(String username, Date date, int hour, int minute) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.ASK_FOR_DEFENSE_TIME,
                new StringObjectMap("username", username), new StringObjectMap("date", date),
                new StringObjectMap("hour", hour), new StringObjectMap("minute", minute));
        return client.sendAndListen(request);
    }

    public Response getDroppingOutSubmissionStatus(String username) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_DROPPING_OUT_SUBMISSION_STATUS,
                new StringObjectMap("username", username));
        return client.sendAndListen(request);
    }

    public Response askForDroppingOut(String username) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.ASK_FOR_DROPPING_OUT,
                new StringObjectMap("username", username));
        return client.sendAndListen(request);
    }

    public Response getActiveCourseDTOs() {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_ACTIVE_COURSE_DTOS);
        return client.sendAndListen(request);
    }

    public Response getProfessorDTOs() {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_PROFESSOR_DTOS);
        return client.sendAndListen(request);
    }

    public Response changeCourseName(String courseId, String newCourseName) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.CHANGE_COURSE_NAME,
                new StringObjectMap("courseId", courseId), new StringObjectMap("newCourseName", newCourseName));
        return client.sendAndListen(request);
    }

    public Response changeCourseNumberOfCredits(String courseId, int newNumberOfCredits) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.CHANGE_COURSE_NUMBER_OF_CREDITS,
                new StringObjectMap("courseId", courseId),
                new StringObjectMap("newNumberOfCredits", newNumberOfCredits));
        return client.sendAndListen(request);
    }

    public Response changeCourseDegreeLevel(String courseId, DegreeLevel newDegreeLevel) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.CHANGE_COURSE_DEGREE_LEVEL,
                new StringObjectMap("courseId", courseId), new StringObjectMap("newDegreeLevel", newDegreeLevel));
        return client.sendAndListen(request);
    }

    public Response changeTeachingProfessors(String courseId, String[] newTeachingProfessorNames) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.CHANGE_COURSE_TEACHING_PROFESSORS,
                new StringObjectMap("courseId", courseId),
                new StringObjectMap("newTeachingProfessorNames", newTeachingProfessorNames));
        return client.sendAndListen(request);
    }

    public Response removeCourse(String courseId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.REMOVE_COURSE,
                new StringObjectMap("courseId", courseId));
        return client.sendAndListen(request);
    }

    public Response changeProfessorAcademicLevel(String professorId, String newAcademicLevelString) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.CHANGE_PROFESSOR_ACADEMIC_LEVEL,
                new StringObjectMap("professorId", professorId),
                new StringObjectMap("newAcademicLevelString", newAcademicLevelString));
        return client.sendAndListen(request);
    }

    public Response changeProfessorOfficeNumber(String professorId, String newOfficeNumber) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.CHANGE_PROFESSOR_OFFICE_NUMBER,
                new StringObjectMap("professorId", professorId),
                new StringObjectMap("newOfficeNumber", newOfficeNumber));
        return client.sendAndListen(request);
    }

    public Response demoteProfessorFromDeputyRole(String professorId, String departmentId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.DEMOTE_FROM_DEPUTY,
                new StringObjectMap("professorId", professorId), new StringObjectMap("departmentId", departmentId));
        return client.sendAndListen(request);
    }

    public Response promoteProfessorToDeputyRole(String professorId, String departmentId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.PROMOTE_TO_DEPUTY,
                new StringObjectMap("professorId", professorId), new StringObjectMap("departmentId", departmentId));
        return client.sendAndListen(request);
    }

    public Response removeProfessor(String professorId, String departmentId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.REMOVE_PROFESSOR,
                new StringObjectMap("professorId", professorId), new StringObjectMap("departmentId", departmentId));
        return client.sendAndListen(request);
    }

    public Response addCourse(Blueprint blueprint) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.ADD_COURSE, blueprint.getFields());
        return client.sendAndListen(request);
    }

    public Response addProfessor(Blueprint blueprint) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.ADD_PROFESSOR, blueprint.getFields());
        return client.sendAndListen(request);
    }

    public Response addStudent(Blueprint blueprint) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.ADD_STUDENT, blueprint.getFields());
        return client.sendAndListen(request);
    }

    public Response getDepartmentDroppingOutRequestDTOs(String departmentId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_DEPARTMENT_DROPPING_OUT_REQUEST_DTOS,
                new StringObjectMap("departmentId", departmentId));
        return client.sendAndListen(request);
    }

    public Response acceptDroppingOutRequest(String requestingStudentId, String academicRequestId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.ACCEPT_DROPPING_OUT_REQUEST,
                new StringObjectMap("requestingStudentId", requestingStudentId),
                new StringObjectMap("academicRequestId", academicRequestId));
        return client.sendAndListen(request);
    }

    public Response declineDroppingOutRequest(String academicRequestId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.DECLINE_DROPPING_OUT_REQUEST,
                new StringObjectMap("academicRequestId", academicRequestId));
        return client.sendAndListen(request);
    }

    public Response askForRecommendation(String requestingStudentId, String receivingProfessorId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.ASK_FOR_RECOMMENDATION,
                new StringObjectMap("requestingStudentId", requestingStudentId),
                new StringObjectMap("receivingProfessorId", receivingProfessorId));
        return client.sendAndListen(request);
    }

    public Response getStudentRecommendationTexts(String studentId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_STUDENT_RECOMMENDATION_TEXTS,
                new StringObjectMap("username", studentId));
        return client.sendAndListen(request);
    }

    public Response getProfessorRecommendationRequestDTOs(String receivingProfessorId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_PROFESSOR_RECOMMENDATION_REQUEST_DTOS,
                new StringObjectMap("receivingProfessorId", receivingProfessorId));
        return client.sendAndListen(request);
    }

    public Response acceptRecommendationRequest(String academicRequestId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.ACCEPT_RECOMMENDATION_REQUEST,
                new StringObjectMap("academicRequestId", academicRequestId));
        return client.sendAndListen(request);
    }

    public Response declineRecommendationRequest(String academicRequestId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.DECLINE_RECOMMENDATION_REQUEST,
                new StringObjectMap("academicRequestId", academicRequestId));
        return client.sendAndListen(request);
    }

    public Response getStudentMinorRequestDTOs(String studentId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_STUDENT_MINOR_REQUEST_DTOS,
                new StringObjectMap("username", studentId));
        return client.sendAndListen(request);
    }

    public Response askForMinor(String requestingStudentId, String originDepartmentId, String targetDepartmentNameString) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.ASK_FOR_MINOR,
                new StringObjectMap("requestingStudentId", requestingStudentId),
                new StringObjectMap("originDepartmentId", originDepartmentId),
                new StringObjectMap("targetDepartmentNameString", targetDepartmentNameString));
        return client.sendAndListen(request);
    }

    public Response getProfessorMinorRequestDTOs(String professorId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_PROFESSOR_MINOR_REQUEST_DTOS,
                new StringObjectMap("username", professorId));
        return client.sendAndListen(request);
    }

    public Response acceptMinorRequest(String academicRequestId, String departmentId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.ACCEPT_MINOR_REQUEST,
                new StringObjectMap("academicRequestId", academicRequestId),
                new StringObjectMap("departmentId", departmentId));
        return client.sendAndListen(request);
    }

    public Response declineMinorRequest(String academicRequestId, String departmentId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.DECLINE_MINOR_REQUEST,
                new StringObjectMap("academicRequestId", academicRequestId),
                new StringObjectMap("departmentId", departmentId));
        return client.sendAndListen(request);
    }

    public Response getStudentTranscriptDTOWithId(String studentId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_STUDENT_TRANSCRIPT_DTO_WITH_ID,
                new StringObjectMap("username", studentId));
        return client.sendAndListen(request);
    }

    public Response getStudentTranscriptDTOWithName(String departmentId, String studentName) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_STUDENT_TRANSCRIPT_DTO_WITH_NAME,
                new StringObjectMap("departmentId", departmentId), new StringObjectMap("studentName", studentName));
        return client.sendAndListen(request);
    }

    public Response getStudentCourseScoreDTOsWithId(String studentId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_STUDENT_COURSE_SCORE_DTOS_WITH_ID,
                new StringObjectMap("username", studentId));
        return client.sendAndListen(request);
    }

    public Response getStudentCourseScoreDTOsWithName(String departmentId, String studentName) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_STUDENT_COURSE_SCORE_DTOS_WITH_NAME,
                new StringObjectMap("departmentId", departmentId), new StringObjectMap("studentName", studentName));
        return client.sendAndListen(request);
    }

    public Response getStudentTemporaryCourseScoreDTOs(String studentId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_STUDENT_TEMPORARY_COURSE_SCORE_DTOS,
                new StringObjectMap("username", studentId));
        return client.sendAndListen(request);
    }

    public Response submitProtest(String courseId, String protestingStudentId, String protest) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.SUBMIT_PROTEST,
                new StringObjectMap("courseId", courseId), new StringObjectMap("username", protestingStudentId),
                new StringObjectMap("protest", protest));
        return client.sendAndListen(request);
    }

    public Response getProfessorActiveCourseNames(String professorId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_PROFESSOR_ACTIVE_COURSE_NAMES,
                new StringObjectMap("username", professorId));
        return client.sendAndListen(request);
    }

    public Response getCourseScoreDTOsForCourse(String departmentId, String courseName) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_COURSE_SCORE_DTOS_FOR_COURSE,
                new StringObjectMap("departmentId", departmentId), new StringObjectMap("courseName", courseName));
        return client.sendAndListen(request);
    }

    public Response respondToProtest(String courseId, String studentId, String protestResponse) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.RESPOND_TO_PROTEST,
                new StringObjectMap("courseId", courseId), new StringObjectMap("studentId", studentId),
                new StringObjectMap("protestResponse", protestResponse));
        return client.sendAndListen(request);
    }

    public Response saveTemporaryScores(String departmentId, String courseName,
                                        HashMap<String, Double> studentIdTemporaryScoreMap) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.SAVE_TEMPORARY_SCORES,
                new StringObjectMap("departmentId", departmentId), new StringObjectMap("courseName", courseName),
                new StringObjectMap("temporaryScoresMap", studentIdTemporaryScoreMap));
        return client.sendAndListen(request);
    }

    public Response finalizeScores(String departmentId, String courseName) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.FINALIZE_SCORES,
                new StringObjectMap("departmentId", departmentId),
                new StringObjectMap("courseName", courseName));
        return client.sendAndListen(request);
    }

    public Response getCourseScoreDTOsForProfessor(String departmentId, String professorName) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_COURSE_SCORE_DTOS_FOR_PROFESSOR,
                new StringObjectMap("departmentId", departmentId), new StringObjectMap("professorName", professorName));
        return client.sendAndListen(request);
    }

    public Response getCourseScoreDTOsForStudent(String departmentId, String studentId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_COURSE_SCORE_DTOS_FOR_STUDENT,
                new StringObjectMap("departmentId", departmentId), new StringObjectMap("studentId", studentId));
        return client.sendAndListen(request);
    }

    public Response getDepartmentStudentIds(String departmentId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_DEPARTMENT_STUDENT_IDS,
                new StringObjectMap("departmentId", departmentId));
        return client.sendAndListen(request);
    }

    public Response getDepartmentStudentNames(String departmentId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_DEPARTMENT_STUDENT_NAMES,
                new StringObjectMap("departmentId", departmentId));
        return client.sendAndListen(request);
    }

    public Response getDepartmentProfessorNames(String departmentId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_DEPARTMENT_PROFESSOR_NAMES,
                new StringObjectMap("departmentId", departmentId));
        return client.sendAndListen(request);
    }

    public Response getDepartmentCourseNames(String departmentId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_DEPARTMENT_COURSE_NAMES,
                new StringObjectMap("departmentId", departmentId));
        return client.sendAndListen(request);
    }

    public Response getCourseStatsDTO(String courseName, DepartmentName departmentName) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_COURSE_STATS_DTO,
                new StringObjectMap("courseName", courseName),
                new StringObjectMap("departmentName", departmentName));
        return client.sendAndListen(request);
    }

    public Response getConversationThumbnailDTOs(String username) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_CONVERSATION_THUMBNAIL_DTOS,
                new StringObjectMap("username", username));
        return client.sendAndListen(request);
    }

    public Response getContactConversationDTO(String username, String contactId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_CONTACT_CONVERSATION_DTO,
                new StringObjectMap("username", username), new StringObjectMap("contactId", contactId));
        return client.sendAndListen(request);
    }

    public Response sendTextMessage(String senderId, ArrayList<String> receiverIds, String messageText) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.SEND_TEXT_MESSAGE,
                new StringObjectMap("senderId", senderId), new StringObjectMap("receiverIds", receiverIds),
                new StringObjectMap("messageText", messageText));
        return client.sendAndListen(request);
    }

    public Response sendMediaMessage(String senderId, ArrayList<String> receiverIds, MediaFile messageMedia) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.SEND_MEDIA_MESSAGE,
                new StringObjectMap("senderId", senderId), new StringObjectMap("receiverIds", receiverIds),
                new StringObjectMap("messageMedia", messageMedia));
        return client.sendAndListen(request);
    }

    public Response downloadMediaFromConversation(String username, String contactId, String mediaId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.DOWNLOAD_MEDIA_FROM_CONVERSATION,
                new StringObjectMap("username", username), new StringObjectMap("contactId", contactId),
                new StringObjectMap("mediaId", mediaId));
        return client.sendAndListen(request);
    }

    public Response getStudentContactProfileDTOs(String username) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_STUDENT_CONTACT_PROFILE_DTOS,
                new StringObjectMap("username", username));
        return client.sendAndListen(request);
    }

    public Response getProfessorContactProfileDTOs(String username) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_PROFESSOR_CONTACT_PROFILE_DTOS,
                new StringObjectMap("username", username));
        return client.sendAndListen(request);
    }

    public Response checkIfContactIdsExist(ArrayList<String> contactIds) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.CHECK_IF_CONTACT_IDS_EXIST,
                new StringObjectMap("contactIds", contactIds));
        return client.sendAndListen(request);
    }

    public Response sendMessageNotificationsIfNecessary(ArrayList<String> contactIds,
                                                        ArrayList<ContactProfileDTO> contactProfileDTOs, String username) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.SEND_MESSAGE_NOTIFICATIONS_IF_NECESSARY,
                new StringObjectMap("contactIds", contactIds),
                new StringObjectMap("contactProfileDTOs", contactProfileDTOs),
                new StringObjectMap("username", username));
        return client.sendAndListen(request);
    }

    public Response getNotificationDTOs(String username) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_NOTIFICATION_DTOS,
                new StringObjectMap("username", username));
        return client.sendAndListen(request);
    }

    public Response acceptNotification(String username, String notificationId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.ACCEPT_NOTIFICATION,
                new StringObjectMap("username", username), new StringObjectMap("notificationId", notificationId));
        return client.sendAndListen(request);
    }

    public Response declineNotification(String username, String notificationId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.DECLINE_NOTIFICATION,
                new StringObjectMap("username", username), new StringObjectMap("notificationId", notificationId));
        return client.sendAndListen(request);
    }

    public Response getAllStudentContactProfileDTOs() {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_ALL_STUDENT_CONTACT_PROFILE_DTOS);
        return client.sendAndListen(request);
    }

    public Response getFilteredStudentContactProfileDTOs(String studentIdStartsWith) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_FILTERED_STUDENT_CONTACT_PROFILE_DTOS,
                new StringObjectMap("studentIdStartsWith", studentIdStartsWith));
        return client.sendAndListen(request);
    }

    public Response getStudentDTO(String studentId) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_STUDENT_DTO,
                new StringObjectMap("studentId", studentId));
        return client.sendAndListen(request);
    }

    public Response getMrMohseniContactProfileDTOs(String username) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_MR_MOHSENI_CONTACT_PROFILE_DTOS,
                new StringObjectMap("username", username));
        return client.sendAndListen(request);
    }

    public Response getFilteredContactIds(ArrayList<String> contactIds, int yearOfEntry, DegreeLevel degreeLevel,
                                          StudentStatus studentStatus) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.GET_FILTERED_CONTACT_IDS,
                new StringObjectMap("contactIds", contactIds), new StringObjectMap("yearOfEntry", yearOfEntry),
                new StringObjectMap("degreeLevel", degreeLevel), new StringObjectMap("studentStatus", studentStatus));
        return client.sendAndListen(request);
    }

    public boolean isClientOnline() {
        return client.isOnline();
    }

    public int getId() { // same as the id of the client
        return client.getId();
    }

    public void setClientId(int nextClientId) {
        client.setId(nextClientId);
    }
}
