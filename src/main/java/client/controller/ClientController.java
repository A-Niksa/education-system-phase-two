package client.controller;

import client.network.Client;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.network.requests.Request;
import shareables.network.requests.RequestIdentifier;
import shareables.network.responses.Response;

public class ClientController {
    private Client client;
    private RequestGenerator requestGenerator;

    public ClientController(Client client) {
        this.client = client;
        requestGenerator = new RequestGenerator();
    }

    public Response logIn(String username, String password, String captcha, String currentCaptcha) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.LOG_IN,
                new StringObjectMap("username", username), new StringObjectMap("password", password),
                new StringObjectMap("captcha", captcha), new StringObjectMap("currentCaptcha", currentCaptcha));
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

    public Response askForDorm() {
        Request request = requestGenerator.generateRequest(RequestIdentifier.ASK_FOR_DORM);
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

    public Response changeCourseLevel(String courseId, DegreeLevel newCourseLevel) {
        Request request = requestGenerator.generateRequest(RequestIdentifier.CHANGE_COURSE_LEVEL,
                new StringObjectMap("courseId", courseId), new StringObjectMap("newCourseLevel", newCourseLevel));
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

    public int getId() { // same as the id of the client
        return client.getId();
    }
}
