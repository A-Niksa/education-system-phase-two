package client.gui.utils;

import shareables.models.pojos.users.students.DegreeLevel;
import shareables.utils.timing.timekeeping.Weekday;

public class EnumArrayUtils {
    public static String[] initializeDegreeLevels() {
        String[] degreeLevels = new String[3];
        int index = 0;
        for (DegreeLevel degreeLevel : DegreeLevel.values()) {
            degreeLevels[index++] = degreeLevel.toString();
        }
        return degreeLevels;
    }

    public static String[] initializeWeekdays() {
        String[] weekdays = new String[7];
        int index = 0;
        for (Weekday weekday : Weekday.values()) {
            weekdays[index++] = weekday.toString();
        }
        return weekdays;
    }
}
