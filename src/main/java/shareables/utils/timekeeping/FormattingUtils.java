package shareables.utils.timekeeping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class FormattingUtils {
    public static DateFormat getStandardDateFormat() {
        return new SimpleDateFormat("yyyy/MM/dd | HH:mm:ss");
    }

    public static DateTimeFormatter getStandardDateTimeFormatter() { // includes time in the day as well
        return DateTimeFormatter.ofPattern("yyyy/MM/dd | HH:mm:ss");
    }

    public static DateTimeFormatter getShortenedDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy/MM/dd");
    }
}
