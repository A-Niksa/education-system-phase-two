package server.network.clienthandling.logicutils.login;

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

    public static void changePassword(User user, String newPassword, DatabaseManager databaseManager) {
        user.setPassword(newPassword);
        databaseManager.update(user);
    }
}
