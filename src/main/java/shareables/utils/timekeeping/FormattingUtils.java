package shareables.utils.timekeeping;

import java.time.format.DateTimeFormatter;

public class FormattingUtils {
    public static DateTimeFormatter getExtensiveDateTimeFormatter() { // includes time in the day as well
        return DateTimeFormatter.ofPattern("yyyy/MM/dd | HH:mm:ss");
    }

    public static DateTimeFormatter getStandardDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy/MM/dd");
    }
}
