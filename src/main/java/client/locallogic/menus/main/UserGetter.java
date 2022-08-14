package client.locallogic.menus.main;

import client.controller.ClientController;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.Student;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

public class UserGetter {
    public static User getUser(String username, ClientController clientController) {
        Response response = clientController.getUser(username);
        return (User) response.get("user");
    }

    public static String getAdvisingProfessorName(Student student, ClientController clientController) {
        Response response = clientController.getAdvisingProfessorName(student.getId());
        return (String) response.get("advisingProfessorName");
    }
}
