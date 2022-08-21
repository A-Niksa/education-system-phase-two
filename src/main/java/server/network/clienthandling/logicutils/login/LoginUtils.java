package server.network.clienthandling.logicutils.login;

import server.database.datasets.DatasetIdentifier;
import shareables.models.pojos.users.User;
import server.database.management.DatabaseManager;
import shareables.models.pojos.users.specialusers.Admin;
import shareables.models.pojos.users.specialusers.MrMohseni;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class LoginUtils {
    public static boolean hasBeenTooLongSinceLastLogin(User user) {
        if (user.getLastLogin() == null) return false;
        LocalDateTime currentDate = LocalDateTime.now();
        double differenceInSeconds = currentDate.toEpochSecond(ZoneOffset.UTC) -
                user.getLastLogin().toEpochSecond(ZoneOffset.UTC);
        double differenceInHours = differenceInSeconds / (3600);
        return differenceInHours >= ConfigManager.getDouble(ConfigFileIdentifier.CONSTANTS,
                "minimumTimeForChangingPassword");
    }

    public static User getUser(DatabaseManager databaseManager, String userId) {
        User user = (User) databaseManager.get(DatasetIdentifier.STUDENTS, userId);

        if (user == null) user = (User) databaseManager.get(DatasetIdentifier.PROFESSORS, userId);

        if (user == null) {
            user = (User) databaseManager.get(DatasetIdentifier.SPECIAL_USERS, userId);
        }

        if (user == null) {
            user = (User) databaseManager.get(DatasetIdentifier.SPECIAL_USERS, userId);
        }

        return user;
    }

    public static void changePassword(DatabaseManager databaseManager, User user, String newPassword) {
        user.setPassword(newPassword);
        user.updateLastLogin();
        switch (user.getUserIdentifier()) {
            case STUDENT:
                databaseManager.update(DatasetIdentifier.STUDENTS, user);
                break;
            case PROFESSOR:
                databaseManager.update(DatasetIdentifier.PROFESSORS, user);
                break;
            case ADMIN:
            case MR_MOHSENI:
                databaseManager.update(DatasetIdentifier.SPECIAL_USERS, user);
                break;
        }
    }
}
