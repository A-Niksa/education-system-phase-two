package client.locallogic.menus.services;

import shareables.network.DTOs.generalmodels.CourseDTO;
import shareables.utils.timing.timekeeping.WeekTime;
import shareables.utils.timing.timekeeping.Weekday;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class WeeklyScheduleParser {
    public static ArrayList<CourseDTO> getCourseDTOsOnWeekday(ArrayList<CourseDTO> courseDTOs, Weekday weekday) {
        return courseDTOs.stream()
                .filter(e -> courseHasClassOnWeekday(e, weekday))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static boolean courseHasClassOnWeekday(CourseDTO courseDTO, Weekday weekday) {
        return courseDTO.getWeeklyClassTimes().stream()
                .map(WeekTime::getWeekday)
                .anyMatch(w -> w == weekday);
    }
}
