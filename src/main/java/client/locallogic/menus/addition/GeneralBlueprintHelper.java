package client.locallogic.menus.addition;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class GeneralBlueprintHelper {
    public static LocalDateTime convertToLocalDateTime(Date selectedDate, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return LocalDateTime.of(year, month, day, hour, minute);
    }
}
