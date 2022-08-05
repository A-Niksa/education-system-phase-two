package server.network.clienthandling.logicutils.main;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.Student;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

public class MainMenuUtils {
    public static String getStudentAdvisingProfessorName(DatabaseManager databaseManager, String studentId) {
        Student adviseeStudent = IdentifiableFetchingUtils.getStudent(databaseManager, studentId);
        Professor advisingProfessor = IdentifiableFetchingUtils.getProfessor(databaseManager,
                adviseeStudent.getAdvisingProfessorId());
        if (advisingProfessor == null) {
            return ConfigManager.getString(ConfigFileIdentifier.TEXTS, "noAdvisingProfessorFound");
        } else {
            return advisingProfessor.fetchName();
        }
    }
}
