package server.network.clienthandling.logicutils.coursewares;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.comparators.CalendarEventDTOComparator;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.coursewares.Homework;
import shareables.network.DTOs.coursewares.CalendarEventDTO;
import shareables.network.DTOs.coursewares.CalendarEventType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseCalendarUtils {
    private static CalendarEventDTOComparator calendarEventDTOComparator;
    static {
        calendarEventDTOComparator = new CalendarEventDTOComparator();
    }

    public static List<CalendarEventDTO> getCourseCalendarEventDTOs(DatabaseManager databaseManager, String courseId,
                                                                    LocalDateTime calendarDate) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);

        return course.getCoursewareManager().getHomeworks().stream()
                .filter(homework -> areEventsInTheSameDay(calendarDate, homework.getPermissibleSubmittingTime()))
                .map(CourseCalendarUtils::initializeShortenedCalendarEventDTO)
                .sorted(calendarEventDTOComparator)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static CalendarEventDTO initializeShortenedCalendarEventDTO(Homework homework) {
        CalendarEventDTO calendarEventDTO = new CalendarEventDTO();
        calendarEventDTO.setCalendarEventType(CalendarEventType.HOMEWORK);
        calendarEventDTO.setEventTitle(homework.getTitle());
        calendarEventDTO.setEventDate(homework.getPermissibleSubmittingTime());

        return calendarEventDTO;
    }

    private static boolean areEventsInTheSameDay(LocalDateTime firstEventDate, LocalDateTime secondEventDate) {
        int firstEventDay = firstEventDate.getDayOfMonth();
        int firstEventMonth = firstEventDate.getMonthValue();
        int firstEventYear = firstEventDate.getYear();

        int secondEventDay = secondEventDate.getDayOfMonth();
        int secondEventMonth = secondEventDate.getMonthValue();
        int secondEventYear = secondEventDate.getYear();

        return firstEventDay == secondEventDay &&
                firstEventMonth == secondEventMonth &&
                firstEventYear == secondEventYear;
    }
}
