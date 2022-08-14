package client.locallogic.menus.main;

import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.timing.formatting.FormattingUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateStringFormatter {
    private static DateTimeFormatter dateTimeFormatter;
    static {
        dateTimeFormatter = FormattingUtils.getExtensiveDateTimeFormatter();
    }

    public static String formatEnrolmentTime(LocalDateTime enrolmentTime) {
        if (enrolmentTime == null) {
            return ConfigManager.getString(ConfigFileIdentifier.TEXTS, "enrolmentTimeNotDetermined");
        } else {
            return format(enrolmentTime);
        }
    }

    public static String format(LocalDateTime localDateTime) {
        return dateTimeFormatter.format(localDateTime);
    }
}
