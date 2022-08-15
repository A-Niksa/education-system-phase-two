package client.locallogic.menus.addition;

import client.locallogic.general.EnumStringMapper;
import shareables.models.pojos.abstractions.TermIdentifier;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.network.blueprints.Blueprint;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.timing.timekeeping.DayTime;
import shareables.utils.timing.timekeeping.WeekTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CourseBlueprintHelper {
    public static String getProcessedCourseId(String courseId) {
        String courseIdFieldMessage = ConfigManager.getString(ConfigFileIdentifier.GUI_COURSE_ADDER,
                "courseIdFieldM");
        if (courseId.equals(courseIdFieldMessage)) {
            return "";
        } else {
            return courseId;
        }
    }

    public static TermIdentifier getTermIdentifier(String termIdentifierString) {
        String[] termIdentifierArray = termIdentifierString.split("-");
        int year = Integer.parseInt(termIdentifierArray[0]);
        int semester = Integer.parseInt(termIdentifierArray[1]);
        return new TermIdentifier(year, semester);
    }

    public static LocalDateTime convertToLocalDateTime(Date selectedDate, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return LocalDateTime.of(year, month, day, hour, minute);
    }

    public static ArrayList<WeekTime> getWeeklyClassTimes(String firstClassWeekdayString, int firstClassStartHour,
                                                           int firstClassStartMinute, int firstClassEndHour,
                                                           int firstClassEndMinute, String secondClassWeekdayString,
                                                           int secondClassStartHour, int secondClassStartMinute,
                                                           int secondClassEndHour, int secondClassEndMinute) {
        ArrayList<WeekTime> weeklyClassTimes = new ArrayList<>();
        WeekTime firstWeeklyClassTime = getWeekTime(firstClassWeekdayString, firstClassStartHour, firstClassStartMinute,
                firstClassEndHour, firstClassEndMinute);
        WeekTime secondWeeklyClassTime = getWeekTime(secondClassWeekdayString, secondClassStartHour, secondClassStartMinute,
                secondClassEndHour, secondClassEndMinute);
        weeklyClassTimes.add(firstWeeklyClassTime);
        weeklyClassTimes.add(secondWeeklyClassTime);
        return weeklyClassTimes;
    }

    public static WeekTime getWeekTime(String weekdayString, int startHour, int startMinute, int endHour,
                                        int endMinute) {
        DayTime classStartTime = new DayTime(startHour, startMinute, 0);
        DayTime classEndTime = new DayTime(endHour, endMinute, 0);
        WeekTime weekTime = new WeekTime(EnumStringMapper.getWeekday(weekdayString), classStartTime, classEndTime);
        return weekTime;
    }
}
