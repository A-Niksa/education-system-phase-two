package server.network.clienthandling.logicutils.unitselection.acquisition.errorutils;

import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.timing.timekeeping.DayTime;
import shareables.utils.timing.timekeeping.WeekTime;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class TimeCollisionUtils {
    public static boolean weeklyClassTimeListsCollide(List<WeekTime> weeklyClassTimesOfFirstCourse,
                                                  List<WeekTime> weeklyClassTimesOfSecondCourse) {
        for (WeekTime weeklyClassTimeOfFirstCourse : weeklyClassTimesOfFirstCourse) {
            for (WeekTime weeklyClassTimeOfSecondCourse : weeklyClassTimesOfSecondCourse) {
                if (weeklyClassTimesCollide(weeklyClassTimeOfFirstCourse, weeklyClassTimeOfSecondCourse)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean weeklyClassTimesCollide(WeekTime firstWeeklyClassTime, WeekTime secondWeeklyClassTime) {
        if (firstWeeklyClassTime.getWeekday() != secondWeeklyClassTime.getWeekday()) return false;

        DayTime firstStartingDayTime = firstWeeklyClassTime.getStartTime();
        DayTime firstEndingDayTime = firstWeeklyClassTime.getEndTime();
        DayTime secondStartingDayTime = secondWeeklyClassTime.getStartTime();
        DayTime secondEndingDayTime = secondWeeklyClassTime.getEndTime();

        return firstDayTimeIsSmallerThanSecondWeeklyTime(firstStartingDayTime, secondEndingDayTime) &&
                firstDayTimeIsSmallerThanSecondWeeklyTime(secondStartingDayTime, firstEndingDayTime);
    }

    private static boolean firstDayTimeIsSmallerThanSecondWeeklyTime(DayTime firstDayTime, DayTime secondDayTime) {
        return firstDayTime.compareTo(secondDayTime) < 0;
    }

    public static boolean examDatesCollide(LocalDateTime firstExamDate, LocalDateTime secondExamDate) {
        double minimumPermittedExamTimeDifference = ConfigManager.getDouble(ConfigFileIdentifier.CONSTANTS,
                "minimumExamTimeDifference");

        return getAbsoluteDifferenceInHours(firstExamDate, secondExamDate) < minimumPermittedExamTimeDifference;
    }

    private static double getAbsoluteDifferenceInHours(LocalDateTime firstDate, LocalDateTime secondDate) {
        double differenceInSeconds = firstDate.toEpochSecond(ZoneOffset.UTC) -
                secondDate.toEpochSecond(ZoneOffset.UTC);
        return Math.abs(differenceInSeconds / (3600));
    }
}
