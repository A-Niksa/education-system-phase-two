package client.locallogic.services;

import shareables.models.pojos.users.students.Student;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

public class GPARequirementChecker {
    public static boolean studentHasSufficientlyHighGPAForMinor(Student student) {
        double minimumMinorGPA = ConfigManager.getDouble(ConfigFileIdentifier.CONSTANTS, "minimumMinorGPA");
        return student.fetchGPA() >= minimumMinorGPA;
    }
}
