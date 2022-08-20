package client.locallogic.menus.coursewares;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {
    public static LocalDateTime convertToLocalDateTime(Date selectedDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return LocalDateTime.of(year, month, day, 0, 0);
    }
}
