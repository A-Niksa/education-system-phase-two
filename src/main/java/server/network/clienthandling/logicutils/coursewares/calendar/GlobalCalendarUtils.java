package server.network.clienthandling.logicutils.coursewares.calendar;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.comparators.coursewarecomparators.CalendarEventDTOComparator;
import server.network.clienthandling.logicutils.coursewares.general.CoursewaresViewUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.network.DTOs.coursewares.CalendarEventDTO;
import shareables.network.DTOs.coursewares.CalendarEventType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GlobalCalendarUtils {
    private static CalendarEventDTOComparator calendarEventDTOComparator;
    static {
        calendarEventDTOComparator = new CalendarEventDTOComparator();
    }

    public static List<CalendarEventDTO> getStudentGlobalCalendarEventDTOs(DatabaseManager databaseManager, String studentId,
                                                                           LocalDateTime selectedDate) {
        List<Course> activeCourses = CoursewaresViewUtils.getStudentActiveCourses(databaseManager, studentId);

        List<CalendarEventDTO> calendarEventDTOs = new ArrayList<>();
        calendarEventDTOs.addAll(getExamCalendarEventDTOs(activeCourses, selectedDate));
        calendarEventDTOs.addAll(getHomeworkCalendarEventDTOs(activeCourses, selectedDate));
        calendarEventDTOs.sort(calendarEventDTOComparator);

        return calendarEventDTOs;
    }

    public static List<CalendarEventDTO> getProfessorGlobalCalendarEventDTOs(DatabaseManager databaseManager,
                                                                             String professorId, LocalDateTime selectedDate) {
        List<Course> activeCourses = CoursewaresViewUtils.getProfessorActiveCourses(databaseManager, professorId);

        List<CalendarEventDTO> calendarEventDTOs = new ArrayList<>();
        calendarEventDTOs.addAll(getExamCalendarEventDTOs(activeCourses, selectedDate));
        calendarEventDTOs.addAll(getHomeworkCalendarEventDTOs(activeCourses, selectedDate));
        calendarEventDTOs.sort(calendarEventDTOComparator);

        return calendarEventDTOs;
    }

    private static List<CalendarEventDTO> getHomeworkCalendarEventDTOs(List<Course> activeCourses, LocalDateTime selectedDate) {
        return activeCourses.stream()
                .flatMap(course -> CourseCalendarUtils.getHomeworkCourseCalendarEventDTOs(course, selectedDate).stream())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static List<CalendarEventDTO> getExamCalendarEventDTOs(List<Course> activeCourses, LocalDateTime selectedDate) {
        return activeCourses.stream()
                .filter(course -> CourseCalendarUtils.areEventsInTheSameDay(course.getExamDate(), selectedDate))
                .map(GlobalCalendarUtils::initializeExamCalendarEventDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static CalendarEventDTO initializeExamCalendarEventDTO(Course course) {
        CalendarEventDTO calendarEventDTO = new CalendarEventDTO();

        calendarEventDTO.setCalendarEventType(CalendarEventType.EXAM);
        calendarEventDTO.setEventTitle("Final Exam");
        calendarEventDTO.setEventDate(course.getExamDate());
        calendarEventDTO.setCourseName(course.getCourseName());

        return calendarEventDTO;
    }
}
