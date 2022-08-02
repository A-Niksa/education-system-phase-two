package server.network.clienthandling;

import server.network.clienthandling.logicutils.*;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.UserIdentifier;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.models.pojos.users.students.Student;
import shareables.models.pojos.users.students.StudentStatus;
import server.database.management.DatabaseManager;
import shareables.network.DTOs.CourseDTO;
import shareables.network.DTOs.ProfessorDTO;
import shareables.network.requests.Request;

import java.util.List;

public class RequestHandler { // TODO: logging, perhaps?
    private DatabaseManager databaseManager;
    private ResponseHandler responseHandler;

    public RequestHandler(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        responseHandler = new ResponseHandler();
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

    public void changeEmailAddress(ClientHandler clientHandler, Request request) {
        User user = LoginUtils.getUser(databaseManager, (String) request.get("username"));
        user.setEmailAddress((String) request.get("newEmailAddress"));
        responseHandler.requestSuccessful(clientHandler);
    }

    public void changePhoneNumber(ClientHandler clientHandler, Request request) {
        User user = LoginUtils.getUser(databaseManager, (String) request.get("username"));
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

    public void getProfessorDTOs(ClientHandler clientHandler, Request request) {
        List<ProfessorDTO> professorDTOs = IdentifiableViewingUtils.getProfessorDTOs(databaseManager);
        responseHandler.professorDTOsAcquired(clientHandler, professorDTOs);
    }

    public void askForDorm(ClientHandler clientHandler, Request request) {
        boolean willGetDorm = AcademicRequestUtils.willGetDorm();
        responseHandler.dormRequestDetermined(clientHandler, willGetDorm);
    }

    public void askForCertificate(ClientHandler clientHandler, Request request) {
        String certificateText = AcademicRequestUtils.getCertificateText(databaseManager, (String) request.get("username"));
        responseHandler.certificateGenerated(clientHandler, certificateText);
    }

    public void changeCourseName(ClientHandler clientHandler, Request request) {
        Course course = IdentifiableEditingUtils.getCourse(databaseManager, (String) request.get("courseId"));
        course.setCourseName((String) request.get("newCourseName"));
        responseHandler.requestSuccessful(clientHandler);
    }

    public void changeCourseNumberOfCredits(ClientHandler clientHandler, Request request) {
        Course course = IdentifiableEditingUtils.getCourse(databaseManager, (String) request.get("courseId"));
        course.setNumberOfCredits((int) request.get("newNumberOfCredits"));
        responseHandler.requestSuccessful(clientHandler);
    }

    public void changeDegreeLevel(ClientHandler clientHandler, Request request) {
        Course course = IdentifiableEditingUtils.getCourse(databaseManager, (String) request.get("courseId"));
        course.setDegreeLevel((DegreeLevel) request.get("newDegreeLevel"));
        responseHandler.requestSuccessful(clientHandler);
    }

    public void changeCourseTeachingProfessors(ClientHandler clientHandler, Request request) {
        Course course = IdentifiableEditingUtils.getCourse(databaseManager, (String) request.get("courseId"));
        String[] newTeachingProfessorNames = (String[]) request.get("newTeachingProfessorNames");
        if (!IdentifiableEditingUtils.professorsExistInDepartment(databaseManager, newTeachingProfessorNames
                , course.getDepartmentId())) {
            responseHandler.professorsDoNotExistInDepartment(clientHandler);
        } else {
            IdentifiableEditingUtils.changeTeachingProfessors(databaseManager, course, newTeachingProfessorNames,
                    course.getDepartmentId());
            responseHandler.requestSuccessful(clientHandler);
        }
    }

    public void removeCourse(ClientHandler clientHandler, Request request) {
        IdentifiableEditingUtils.removeCourse(databaseManager, (String) request.get("courseId"));
        responseHandler.requestSuccessful(clientHandler);
    }

    public void addCourse(ClientHandler clientHandler, Request request) {
        String[] teachingProfessorNames = (String[]) request.get("teachingProfessorNames");
        if (!IdentifiableEditingUtils.professorsExistInDepartment(databaseManager, teachingProfessorNames,
                (String) request.get("departmentId"))) {
            responseHandler.professorsDoNotExistInDepartment(clientHandler);
        } else {
            String id = CourseAdditionUtils.addCourseAndReturnId(databaseManager, request);
            responseHandler.courseAdded(clientHandler, id);
        }
    }
}