package server.network.clienthandling.logicutils;

import server.database.datasets.DatasetIdentifier;
import shareables.models.pojos.users.User;
import server.database.management.DatabaseManager;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import java.util.Date;

public class LoginUtils {
    public static boolean hasBeenTooLongSinceLastLogin(User user) {
        if (user.getLastLogin() == null) return false;
        Date currentDate = new Date();
        double differenceInMillis = currentDate.getTime() - user.getLastLogin().getTime();
        double differenceInHours = differenceInMillis / (1000 * 3600);
        return differenceInHours >= ConfigManager.getDouble(ConfigFileIdentifier.CONSTANTS,
                "minimumTimeForChangingPassword");
    }

    public static User getUser(DatabaseManager databaseManager, String id) {
        User user = (User) databaseManager.get(DatasetIdentifier.STUDENTS, id);
        if (user == null) user = (User) databaseManager.get(DatasetIdentifier.PROFESSORS, id);
        // TODO: adding cases for mohseni and admin
        return user;
    }

    public static void changePassword(DatabaseManager databaseManager, User user, String newPassword) {
        user.setPassword(newPassword);
        user.updateLastLogin();
        // TODO: adding cases for mohseni and admin
        switch (user.getUserIdentifier()) {
            case STUDENT:
                databaseManager.update(DatasetIdentifier.STUDENTS, user);
                break;
            case PROFESSOR:
                databaseManager.update(DatasetIdentifier.PROFESSORS, user);
                break;
        }
    }
}
