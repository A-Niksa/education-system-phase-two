package shareables.utils.timing.timekeeping;

import shareables.utils.timing.formatting.FormattingUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeManager {
    public static String getTime() {
        DateTimeFormatter dateTimeFormatter = FormattingUtils.getExtensiveDateTimeFormatter();
        LocalDateTime currentTime = LocalDateTime.now();
        return dateTimeFormatter.format(currentTime);
    }

    public static String getDate() {
        DateTimeFormatter dateTimeFormatter = FormattingUtils.getStandardDateTimeFormatter();
        LocalDateTime currentTime = LocalDateTime.now();
        return dateTimeFormatter.format(currentTime);
    }
}
