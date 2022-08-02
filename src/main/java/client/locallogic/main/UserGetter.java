package client.locallogic.main;

import client.controller.ClientController;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.Student;
import shareables.network.responses.Response;

public class UserGetter {
    public static User getUser(String username, ClientController clientController) {
        Response response = clientController.getUser(username);
        return (User) response.get("user");
    }

    public static String getAdvisingProfessorName(Student student, ClientController clientController) {
        Professor advisingProfessor = (Professor) UserGetter.getUser(student.getAdvisingProfessorId(), clientController);
        return advisingProfessor.fetchName();
    }
}
