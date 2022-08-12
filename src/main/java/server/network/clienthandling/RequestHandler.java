package server.network.clienthandling;

import server.network.clienthandling.logicutils.addition.CourseAdditionUtils;
import server.network.clienthandling.logicutils.addition.ProfessorAdditionUtils;
import server.network.clienthandling.logicutils.addition.StudentAdditionUtils;
import server.network.clienthandling.logicutils.enrolment.CourseEditingUtils;
import server.network.clienthandling.logicutils.enrolment.IdentifiableViewingUtils;
import server.network.clienthandling.logicutils.enrolment.ProfessorEditingUtils;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import server.network.clienthandling.logicutils.general.OfflineModeUtils;
import server.network.clienthandling.logicutils.login.LoginUtils;
import server.network.clienthandling.logicutils.main.MainMenuUtils;
import server.network.clienthandling.logicutils.messaging.DownloadingUtils;
import server.network.clienthandling.logicutils.messaging.MessageSendingUtils;
import server.network.clienthandling.logicutils.messaging.MessengerViewUtils;
import server.network.clienthandling.logicutils.services.*;
import server.network.clienthandling.logicutils.standing.StandingManagementUtils;
import server.network.clienthandling.logicutils.standing.StandingMasteryUtils;
import server.network.clienthandling.logicutils.standing.StandingViewUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.abstractions.DepartmentName;
import shareables.models.pojos.academicrequests.AcademicRequestStatus;
import shareables.models.pojos.media.MediaFile;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.UserIdentifier;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.models.pojos.users.students.Student;
import shareables.models.pojos.users.students.StudentStatus;
import server.database.management.DatabaseManager;
import shareables.network.DTOs.*;
import shareables.network.requests.Request;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RequestHandler { // TODO: logging, perhaps?
    private DatabaseManager databaseManager;
    private ResponseHandler responseHandler;

    public RequestHandler(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        responseHandler = new ResponseHandler();
    }

    public void respondToConnectionPing(ClientHandler clientHandler) {
        responseHandler.requestSuccessful(clientHandler);
    }

    public void logIn(ClientHandler clientHandler, Request request) { // TODO: two people logging in at the same time
        User user = LoginUtils.getUser(databaseManager, (String) request.get("username"));
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

    public void getOfflineModeDTO(ClientHandler clientHandler, Request request) {
        OfflineModeDTO offlineModeDTO = OfflineModeUtils.getOfflineModeDTO(databaseManager,
                (String) request.get("username"));
        responseHandler.offlineModeDTOAcquired(clientHandler, offlineModeDTO);
    }

    public void changePassword(ClientHandler clientHandler, Request request) {
        User user = LoginUtils.getUser(databaseManager, (String) request.get("username"));
        if (user == null || user.getPassword().equals(request.get("newPassword"))) {
            responseHandler.newPasswordIsSameAsOldOne(clientHandler);
        } else {
            LoginUtils.changePassword(databaseManager, user, (String) request.get("newPassword"));
            responseHandler.requestSuccessful(clientHandler);
        }
    }

    public void getUser(ClientHandler clientHandler, Request request) {
        User user = LoginUtils.getUser(databaseManager, (String) request.get("username"));
        responseHandler.userAcquired(clientHandler, user);
    }

    public void getAdvisingProfessorName(ClientHandler clientHandler, Request request) {
        String advisingProfessorName = MainMenuUtils.getStudentAdvisingProfessorName(databaseManager,
                (String) request.get("username"));
        responseHandler.advisingProfessorNameAcquired(clientHandler, advisingProfessorName);
    }

    public void changeEmailAddress(ClientHandler clientHandler, Request request) {
        User user = IdentifiableFetchingUtils.getUser(databaseManager, (String) request.get("username"));
        user.setEmailAddress((String) request.get("newEmailAddress"));
        responseHandler.requestSuccessful(clientHandler);
    }

    public void changePhoneNumber(ClientHandler clientHandler, Request request) {
        User user = IdentifiableFetchingUtils.getUser(databaseManager, (String) request.get("username"));
        user.setPhoneNumber((String) request.get("newPhoneNumber"));
        responseHandler.requestSuccessful(clientHandler);
    }

    public void getStudentCourseDTOs(ClientHandler clientHandler, Request request) {
        List<CourseDTO> courseDTOs = WeeklyScheduleUtils.getStudentCourseDTOs(databaseManager, (String) request.get("username"));
        responseHandler.courseDTOsAcquired(clientHandler, courseDTOs);
    }

    public void getProfessorCourseDTOs(ClientHandler clientHandler, Request request) {
        List<CourseDTO> courseDTOs = WeeklyScheduleUtils.getProfessorCourseDTOs(databaseManager, (String) request.get("username"));
        responseHandler.courseDTOsAcquired(clientHandler, courseDTOs);
    }

    public void getActiveCourseDTOs(ClientHandler clientHandler, Request request) {
        List<CourseDTO> activeCourseDTOs = IdentifiableViewingUtils.getActiveCourseDTOs(databaseManager);
        responseHandler.courseDTOsAcquired(clientHandler, activeCourseDTOs);
    }

    public void getDepartmentCourseDTOs(ClientHandler clientHandler, Request request) {
        List<CourseDTO> departmentCourseDTOs = IdentifiableViewingUtils.getDepartmentCourseDTOs(databaseManager,
                (String) request.get("departmentId"));
        responseHandler.courseDTOsAcquired(clientHandler, departmentCourseDTOs);
    }

    public void getDepartmentProfessorDTOs(ClientHandler clientHandler, Request request) {
        List<ProfessorDTO> departmentProfessorDTOs = IdentifiableViewingUtils.getDepartmentProfessorDTOs(databaseManager,
                (String) request.get("departmentId"));
        responseHandler.professorDTOsAcquired(clientHandler, departmentProfessorDTOs);

    }

    public void getProfessorDTOs(ClientHandler clientHandler, Request request) {
        List<ProfessorDTO> professorDTOs = IdentifiableViewingUtils.getProfessorDTOs(databaseManager);
        responseHandler.professorDTOsAcquired(clientHandler, professorDTOs);
    }

    public void askForDorm(ClientHandler clientHandler, Request request) {
        boolean willGetDorm = RequestSubmissionUtils.willGetDorm();
        responseHandler.dormRequestDetermined(clientHandler, willGetDorm);
    }

    public void askForCertificate(ClientHandler clientHandler, Request request) {
        String certificateText = RequestSubmissionUtils.getCertificateText(databaseManager, (String) request.get("username"));
        responseHandler.certificateGenerated(clientHandler, certificateText);
    }

    public void getDefenseTime(ClientHandler clientHandler, Request request) {
        // defense time can be null as well:
        LocalDateTime defenseTime = RequestSubmissionUtils.getDefenseTime(databaseManager, (String) request.get("username"));
        if (defenseTime == null) {
            responseHandler.defenseTimeNotFound(clientHandler);
        } else {
            responseHandler.defenseTimeAcquired(clientHandler, defenseTime);
        }
    }

    public void askForDefenseTime(ClientHandler clientHandler, Request request) {
        Date date = (Date) request.get("date");
        if (RequestSubmissionUtils.dateIsSoonerThanNow(date, (int) request.get("hour"), (int) request.get("minute"))) {
            responseHandler.dateIsSoonerThanNow(clientHandler);
        } else {
            RequestSubmissionUtils.reserveDefenseTime(databaseManager, request);
            responseHandler.requestSuccessful(clientHandler);
        }
    }

    public void getDroppingOutSubmissionStatus(ClientHandler clientHandler, Request request) {
        String studentId = (String) request.get("username");
        boolean academicRequestHasBeenSubmitted = RequestSubmissionUtils.studentHasSubmittedDroppingOutRequest(databaseManager,
                studentId);
        AcademicRequestStatus academicRequestStatus = RequestSubmissionUtils.getDroppingOutRequestStatus(databaseManager,
                studentId);
        responseHandler.droppingOutSubmissionStatusAcquired(clientHandler, academicRequestHasBeenSubmitted,
                academicRequestStatus);
    }

    public void askForDroppingOut(ClientHandler clientHandler, Request request) {
        RequestSubmissionUtils.submitDroppingOutRequest(databaseManager, (String) request.get("username"));
        responseHandler.requestSuccessful(clientHandler);
    }

    public void changeCourseName(ClientHandler clientHandler, Request request) {
        Course course = CourseEditingUtils.getCourse(databaseManager, (String) request.get("courseId"));
        course.setCourseName((String) request.get("newCourseName"));
        responseHandler.requestSuccessful(clientHandler);
    }

    public void changeCourseNumberOfCredits(ClientHandler clientHandler, Request request) {
        Course course = CourseEditingUtils.getCourse(databaseManager, (String) request.get("courseId"));
        course.setNumberOfCredits((int) request.get("newNumberOfCredits"));
        responseHandler.requestSuccessful(clientHandler);
    }

    public void changeCourseDegreeLevel(ClientHandler clientHandler, Request request) {
        Course course = CourseEditingUtils.getCourse(databaseManager, (String) request.get("courseId"));
        course.setDegreeLevel((DegreeLevel) request.get("newDegreeLevel"));
        responseHandler.requestSuccessful(clientHandler);
    }

    public void changeCourseTeachingProfessors(ClientHandler clientHandler, Request request) {
        Course course = CourseEditingUtils.getCourse(databaseManager, (String) request.get("courseId"));
        String[] newTeachingProfessorNames = (String[]) request.get("newTeachingProfessorNames");
        if (!CourseEditingUtils.professorsExistInDepartment(databaseManager, newTeachingProfessorNames
                , course.getDepartmentId())) {
            responseHandler.professorsDoNotExistInDepartment(clientHandler);
        } else {
            CourseEditingUtils.changeTeachingProfessors(databaseManager, course, newTeachingProfessorNames,
                    course.getDepartmentId());
            responseHandler.requestSuccessful(clientHandler);
        }
    }

    public void removeCourse(ClientHandler clientHandler, Request request) {
        CourseEditingUtils.removeCourse(databaseManager, (String) request.get("courseId"));
        responseHandler.requestSuccessful(clientHandler);
    }

    public void addCourse(ClientHandler clientHandler, Request request) {
        String[] teachingProfessorNames = (String[]) request.get("teachingProfessorNames");
        if (!CourseEditingUtils.professorsExistInDepartment(databaseManager, teachingProfessorNames,
                (String) request.get("departmentId"))) {
            responseHandler.professorsDoNotExistInDepartment(clientHandler);
        } else {
            String courseId = CourseAdditionUtils.addCourseAndReturnId(databaseManager, request);
            responseHandler.courseAdded(clientHandler, courseId);
        }
    }

    public void addProfessor(ClientHandler clientHandler, Request request) {
        String[] adviseeStudentIds = (String[]) request.get("adviseeStudentIds");
        if (ProfessorAdditionUtils.studentsDoNotExistInDepartment(databaseManager, adviseeStudentIds,
                (String) request.get("departmentId"))) {
            responseHandler.studentsDoNotExistInDepartment(clientHandler);
        } else if (ProfessorAdditionUtils.anyStudentAlreadyHasAnAdvisor(databaseManager, adviseeStudentIds)) {
            responseHandler.atLeastOneStudentAlreadyHasAnAdvisor(clientHandler);
        } else {
            String professorId = ProfessorAdditionUtils.addProfessorAndReturnId(databaseManager, request);
            responseHandler.professorAdded(clientHandler, professorId);
        }
    }

    public void addStudent(ClientHandler clientHandler, Request request) {
        String studentId = StudentAdditionUtils.addStudentAndReturnId(databaseManager, request);
        responseHandler.studentAdded(clientHandler, studentId);
    }

    public void getDepartmentDroppingOutRequestDTOs(ClientHandler clientHandler, Request request) {
        List<RequestDTO> departmentDroppingOutRequestDTOs = RequestManagementUtils.getDroppingOutRequestDTOs(databaseManager,
                (String) request.get("departmentId"));
        responseHandler.requestDTOsAcquired(clientHandler, departmentDroppingOutRequestDTOs);
    }

    public void acceptDroppingOutRequest(ClientHandler clientHandler, Request request) {
        RequestManagementUtils.acceptDroppingOutRequest(databaseManager, (String) request.get("requestingStudentId"),
                (String) request.get("academicRequestId"));
        responseHandler.requestSuccessful(clientHandler);
    }

    public void declineDroppingOutRequest(ClientHandler clientHandler, Request request) {
        RequestManagementUtils.declineDroppingOutRequest(databaseManager, (String) request.get("academicRequestId"));
        responseHandler.requestSuccessful(clientHandler);
    }

    public void askForRecommendation(ClientHandler clientHandler, Request request) {
        String receivingProfessorId = (String) request.get("receivingProfessorId");
        if (!RequestSubmissionUtils.professorExists(databaseManager, receivingProfessorId)) {
            responseHandler.professorDoesNotExist(clientHandler, receivingProfessorId);
        } else {
            RequestSubmissionUtils.submitRecommendationRequest(databaseManager,
                    (String) request.get("requestingStudentId"), receivingProfessorId);
            responseHandler.requestSuccessful(clientHandler);
        }
    }

    public void getStudentRecommendationTexts(ClientHandler clientHandler, Request request) {
        List<String> studentRecommendationTexts = RequestSubmissionUtils.getStudentRecommendationTexts(databaseManager,
                (String) request.get("username"));
        responseHandler.recommendationTextsAcquired(clientHandler, studentRecommendationTexts);
    }

    public void getProfessorRecommendationRequestDTOs(ClientHandler clientHandler, Request request) {
        List<RequestDTO> professorRecommendationRequestDTOs = RequestManagementUtils.getProfessorRecommendationRequestDTOs(
                databaseManager, (String) request.get("receivingProfessorId"));
        responseHandler.requestDTOsAcquired(clientHandler, professorRecommendationRequestDTOs);
    }

    public void acceptRecommendationRequest(ClientHandler clientHandler, Request request) {
        RequestManagementUtils.acceptRecommendationRequest(databaseManager, (String) request.get("academicRequestId"));
        responseHandler.requestSuccessful(clientHandler);
    }

    public void declineRecommendationRequest(ClientHandler clientHandler, Request request) {
        RequestManagementUtils.declineRecommendationRequest(databaseManager, (String) request.get("academicRequestId"));
        responseHandler.requestSuccessful(clientHandler);
    }

    public void getStudentMinorRequestDTOs(ClientHandler clientHandler, Request request) {
        List<RequestDTO> studentMinorRequestDTOs = MinorSubmissionUtils.getStudentMinorRequestDTOs(databaseManager,
                (String) request.get("username"));
        responseHandler.requestDTOsAcquired(clientHandler, studentMinorRequestDTOs);
    }

    public void askForMinor(ClientHandler clientHandler, Request request) {
        String requestingStudentId = (String) request.get("requestingStudentId");
        String originDepartmentId = (String) request.get("originDepartmentId");
        String targetDepartmentNameString = (String) request.get("targetDepartmentNameString");
        if (!MinorSubmissionUtils.studentHasSufficientlyHighGPAForMinor(databaseManager, requestingStudentId)) {
            responseHandler.studentGPAIsNotHighEnough(clientHandler);
        } else if (MinorSubmissionUtils.targetDepartmentIsSameAsCurrentDepartment(originDepartmentId,
                targetDepartmentNameString)) {
            responseHandler.targetDepartmentIsTheCurrentDepartment(clientHandler);
        } else if (MinorSubmissionUtils.isCurrentlyMinoringAtTargetDepartment(databaseManager, requestingStudentId,
                targetDepartmentNameString)) {
            responseHandler.currentlyMinoringAtTargetDepartment(clientHandler);
        } else if (MinorSubmissionUtils.studentIsMinoringSomewhere(databaseManager, requestingStudentId)) {
            responseHandler.cannotMinorAtTwoPlaces(clientHandler);
        } else  {
            MinorSubmissionUtils.submitMinorRequest(databaseManager, requestingStudentId, originDepartmentId,
                    targetDepartmentNameString);
            responseHandler.requestSuccessful(clientHandler);
        }
    }

    public void getProfessorMinorRequestDTOs(ClientHandler clientHandler, Request request) {
        List<RequestDTO> professorMinorRequestDTOs = MinorManagementUtils.getProfessorMinorRequestDTOs(databaseManager,
                (String) request.get("username"));
        responseHandler.requestDTOsAcquired(clientHandler, professorMinorRequestDTOs);
    }

    public void acceptMinorRequest(ClientHandler clientHandler, Request request) {
        String academicRequestId = (String) request.get("academicRequestId");
        String acceptingDepartmentId = (String) request.get("departmentId");
        MinorManagementUtils.acceptMinorRequest(databaseManager, academicRequestId, acceptingDepartmentId);
        responseHandler.requestSuccessful(clientHandler);
    }

    public void declineMinorRequest(ClientHandler clientHandler, Request request) {
        String academicRequestId = (String) request.get("academicRequestId");
        String decliningDepartmentId = (String) request.get("departmentId");
        MinorManagementUtils.declineMinorRequest(databaseManager, academicRequestId, decliningDepartmentId);
        responseHandler.requestSuccessful(clientHandler);
    }

    public void getStudentTranscriptDTOWithId(ClientHandler clientHandler, Request request) {
        TranscriptDTO studentTranscriptDTO = StandingViewUtils.getStudentTranscriptDTOWithId(databaseManager,
                (String) request.get("username"));
        responseHandler.transcriptDTOAcquired(clientHandler, studentTranscriptDTO);
    }

    public void getStudentTranscriptDTOWithName(ClientHandler clientHandler, Request request) {
        TranscriptDTO studentTranscriptDTO = StandingViewUtils.getStudentTranscriptDTOWithName(databaseManager,
                (String) request.get("departmentId"), (String) request.get("studentName"));
        responseHandler.transcriptDTOAcquired(clientHandler, studentTranscriptDTO);
    }

    public void getStudentCourseScoreDTOsWithId(ClientHandler clientHandler, Request request) {
        List<CourseScoreDTO> studentCourseScoreDTOs = StandingViewUtils.getStudentCourseScoreDTOsWithId(databaseManager,
                (String) request.get("username"));
        responseHandler.courseScoreDTOsAcquired(clientHandler, studentCourseScoreDTOs);
    }

    public void getStudentCourseScoreDTOsWithName(ClientHandler clientHandler, Request request) {
        List<CourseScoreDTO> studentCourseScoreDTOs = StandingViewUtils.getStudentCourseScoreDTOsWithName(databaseManager,
                (String) request.get("departmentId"), (String) request.get("studentName"));
        responseHandler.courseScoreDTOsAcquired(clientHandler, studentCourseScoreDTOs);
    }

    public void getStudentTemporaryCourseScoreDTOs(ClientHandler clientHandler, Request request) {
        List<CourseScoreDTO> studentTemporaryCourseScoreDTOs = StandingViewUtils.getStudentTemporaryCourseScoreDTOs(
                databaseManager, (String) request.get("username"));
        responseHandler.courseScoreDTOsAcquired(clientHandler, studentTemporaryCourseScoreDTOs);
    }

    public void submitProtest(ClientHandler clientHandler, Request request) {
        String protestingStudentId = (String) request.get("username");
        String courseId = (String) request.get("courseId");
        String protest = (String) request.get("protest");
        StandingViewUtils.submitProtest(databaseManager, protestingStudentId, courseId, protest);
        responseHandler.requestSuccessful(clientHandler);
    }

    public void getProfessorActiveCourseNames(ClientHandler clientHandler, Request request) {
        String[] professorActiveCourseNames = StandingManagementUtils.getProfessorActiveCourseNames(databaseManager,
                (String) request.get("username"));
        responseHandler.stringArrayAcquired(clientHandler, professorActiveCourseNames);
    }

    public void getCourseScoreDTOsForCourse(ClientHandler clientHandler, Request request) {
        String departmentId = (String) request.get("departmentId");
        String courseName = (String) request.get("courseName");
        List<CourseScoreDTO> courseScoreDTOsForCourse = StandingManagementUtils.getCourseScoreDTOsForCourse(databaseManager,
                departmentId, courseName);
        responseHandler.courseScoreDTOsAcquired(clientHandler, courseScoreDTOsForCourse);
    }

    public void respondToProtest(ClientHandler clientHandler, Request request) {
        String courseId = (String) request.get("courseId");
        String protestingStudentId = (String) request.get("studentId");
        String responseToProtest = (String) request.get("protestResponse");
        StandingManagementUtils.respondToProtest(databaseManager, courseId, protestingStudentId, responseToProtest);
        responseHandler.requestSuccessful(clientHandler);
    }

    public void saveTemporaryScores(ClientHandler clientHandler, Request request) {
        String departmentId = (String) request.get("departmentId");
        String courseName = (String) request.get("courseName");
        Map<String, Double> temporaryScoresMap = (Map<String, Double>) request.get("temporaryScoresMap");
        StandingManagementUtils.saveTemporaryScores(databaseManager, departmentId, courseName, temporaryScoresMap);
        responseHandler.requestSuccessful(clientHandler);
    }

    public void finalizeScores(ClientHandler clientHandler, Request request) {
        String departmentId = (String) request.get("departmentId");
        String courseName = (String) request.get("courseName");
        if (!StandingManagementUtils.allStudentsHaveBeenTemporaryScores(databaseManager, departmentId, courseName)) {
            responseHandler.notAllStudentsHaveBeenGivenTemporaryScores(clientHandler);
        } else {
            StandingManagementUtils.finalizeScores(databaseManager, departmentId, courseName);
            responseHandler.requestSuccessful(clientHandler);
        }
    }

    public void getCourseScoreDTOsForProfessor(ClientHandler clientHandler, Request request) {
        String departmentId = (String) request.get("departmentId");
        String professorName = (String) request.get("professorName");
        List<CourseScoreDTO> courseScoreDTOsForProfessor = StandingMasteryUtils.getCourseScoreDTOsForProfessor(databaseManager,
                departmentId, professorName);
        responseHandler.courseScoreDTOsAcquired(clientHandler, courseScoreDTOsForProfessor);
    }

    public void getCourseScoreDTOsForStudent(ClientHandler clientHandler, Request request) {
        String departmentId = (String) request.get("departmentId");
        String studentId = (String) request.get("studentId");
        List<CourseScoreDTO> courseScoreDTOsForStudent = StandingMasteryUtils.getCourseScoreDTOsForStudent(databaseManager,
                departmentId, studentId);
        responseHandler.courseScoreDTOsAcquired(clientHandler, courseScoreDTOsForStudent);
    }

    public void getDepartmentCourseNames(ClientHandler clientHandler, Request request) {
        String[] departmentCourseNames = StandingMasteryUtils.getDepartmentCourseNames(databaseManager,
                (String) request.get("departmentId"));
        responseHandler.stringArrayAcquired(clientHandler, departmentCourseNames);
    }

    public void getDepartmentProfessorNames(ClientHandler clientHandler, Request request) {
        String[] departmentProfessorNames = StandingMasteryUtils.getDepartmentProfessorNames(databaseManager,
                (String) request.get("departmentId"));
        responseHandler.stringArrayAcquired(clientHandler, departmentProfessorNames);
    }

    public void getDepartmentStudentIds(ClientHandler clientHandler, Request request) {
        String[] departmentStudentIds = StandingMasteryUtils.getDepartmentStudentIds(databaseManager,
                (String) request.get("departmentId"));
        responseHandler.stringArrayAcquired(clientHandler, departmentStudentIds);
    }

    public void getDepartmentStudentNames(ClientHandler clientHandler, Request request) {
        String[] departmentStudentNames = StandingMasteryUtils.getDepartmentStudentNames(databaseManager,
                (String) request.get("departmentId"));
        responseHandler.stringArrayAcquired(clientHandler, departmentStudentNames);
    }

    public void getCourseStatsDTO(ClientHandler clientHandler, Request request) {
        String courseName = (String) request.get("courseName");
        DepartmentName departmentName = (DepartmentName) request.get("departmentName");
        Course course = StandingMasteryUtils.getCourse(databaseManager, courseName, departmentName);
        if (!StandingMasteryUtils.allStudentScoresHaveBeenFinalized(databaseManager, course)) {
            responseHandler.notAllStudentScoresHaveBeenFinalized(clientHandler);
        } else {
            CourseStatsDTO courseStatsDTO = StandingMasteryUtils.getCourseStatsDTO(databaseManager, course);
            responseHandler.courseStatsDTOAcquired(clientHandler, courseStatsDTO);
        }
    }

    public void changeProfessorAcademicLevel(ClientHandler clientHandler, Request request) {
        ProfessorEditingUtils.changeProfessorAcademicLevel(databaseManager, (String) request.get("professorId"),
                (String) request.get("newAcademicLevelString"));
        responseHandler.requestSuccessful(clientHandler);
    }

    public void changeProfessorOfficeNumber(ClientHandler clientHandler, Request request) {
        ProfessorEditingUtils.changeProfessorOfficeNumber(databaseManager, (String) request.get("professorId"),
                (String) request.get("newOfficeNumber"));
        responseHandler.requestSuccessful(clientHandler);
    }

    public void demoteProfessorFromDeputyRole(ClientHandler clientHandler, Request request) {
        String professorId = (String) request.get("professorId");
        String departmentId = (String) request.get("departmentId");
        if (!ProfessorEditingUtils.isProfessorDepartmentDeputy(databaseManager, professorId, departmentId)) {
            responseHandler.professorIsNotDepartmentDeputy(clientHandler);
        } else {
            ProfessorEditingUtils.demoteProfessorFromDeputyRole(databaseManager, professorId, departmentId);
            responseHandler.requestSuccessful(clientHandler);
        }
    }

    public void promoteProfessorToDeputyRole(ClientHandler clientHandler, Request request) {
        String professorId = (String) request.get("professorId");
        String departmentId = (String) request.get("departmentId");
        if (ProfessorEditingUtils.isProfessorDepartmentDeputy(databaseManager, professorId, departmentId)) {
            responseHandler.professorIsAlreadyDepartmentDeputy(clientHandler);
        } else if (ProfessorEditingUtils.departmentHasDeputy(databaseManager, departmentId)) {
            responseHandler.departmentAlreadyHasDeputy(clientHandler);
        } else if (ProfessorEditingUtils.isProfessorDepartmentDean(databaseManager, professorId, departmentId)) {
            responseHandler.cannotPromoteDeanToDeputy(clientHandler);
        } else {
            ProfessorEditingUtils.promoteProfessorToDeputyRole(databaseManager, professorId, departmentId);
            responseHandler.requestSuccessful(clientHandler);
        }
    }

    public void removeProfessor(ClientHandler clientHandler, Request request) {
        String professorId = (String) request.get("professorId");
        String departmentId = (String) request.get("departmentId");
        if (ProfessorEditingUtils.isProfessorDepartmentDean(databaseManager, professorId, departmentId)) {
            responseHandler.cannotRemoveDepartmentDean(clientHandler);
        } else if (ProfessorEditingUtils.isProfessorDepartmentDeputy(databaseManager, professorId, departmentId)) {
            ProfessorEditingUtils.removeProfessor(databaseManager, professorId, departmentId);
            responseHandler.requestSuccessfulButDeanBecomesTemporaryDeputy(clientHandler);
        } else {
            ProfessorEditingUtils.removeProfessor(databaseManager, professorId, departmentId);
            responseHandler.requestSuccessful(clientHandler);
        }
    }

    public void getConversationThumbnailDTOs(ClientHandler clientHandler, Request request) {
        List<ConversationThumbnailDTO> conversationThumbnailDTOs = MessengerViewUtils.getConversationThumbnailDTOs(
                databaseManager, (String) request.get("username"));
        responseHandler.conversationThumbnailDTOsAcquired(clientHandler, conversationThumbnailDTOs);
    }

    public void getContactConversationDTO(ClientHandler clientHandler, Request request) {
        String userId = (String) request.get("username");
        String contactId = (String) request.get("contactId");
        ConversationDTO conversationDTO = MessengerViewUtils.getContactConversationDTO(databaseManager,
                userId, contactId); // sorts conversations per time as well
        responseHandler.conversationDTOAcquired(clientHandler, conversationDTO);
    }

    public void sendTextMessage(ClientHandler clientHandler, Request request) {
        String senderId = (String) request.get("senderId");
        String receiverId = (String) request.get("receiverId");
        String messageText = (String) request.get("messageText");
        MessageSendingUtils.sendTextMessage(databaseManager, senderId, receiverId, messageText);
        responseHandler.requestSuccessful(clientHandler);
    }

    public void sendMediaMessage(ClientHandler clientHandler, Request request) {
        String senderId = (String) request.get("senderId");
        String receiverId = (String) request.get("receiverId");
        MediaFile messageMedia = (MediaFile) request.get("messageMedia");
        MessageSendingUtils.sendMediaMessage(databaseManager, senderId, receiverId, messageMedia);
        responseHandler.requestSuccessful(clientHandler);

    }

    public void downloadMediaFromConversation(ClientHandler clientHandler, Request request) {
        String userId = (String) request.get("username");
        String contactId = (String) request.get("contactId");
        String mediaId = (String) request.get("mediaId");
        if (!DownloadingUtils.mediaFileExistsInConversation(databaseManager, userId, contactId, mediaId)) {
            responseHandler.mediaFileDoesNotExist(clientHandler);
        } else {
            MediaFile mediaFile = DownloadingUtils.getMediaFileFromConversation(databaseManager, userId, contactId, mediaId);
            responseHandler.mediaFileAcquired(clientHandler, mediaFile);
        }
    }
}