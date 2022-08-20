package shareables.network.DTOs.coursewares;

import shareables.utils.timing.formatting.FormattingUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CalendarEventDTO {
    private static DateTimeFormatter extensiveDateTimeFormatter;
    static {
        extensiveDateTimeFormatter = FormattingUtils.getExtensiveDateTimeFormatter();
    }

    private String courseName;
    private String eventTitle;
    private CalendarEventType calendarEventType;
    private LocalDateTime eventDate;

    public CalendarEventDTO() {
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public CalendarEventType getCalendarEventType() {
        return calendarEventType;
    }

    public void setCalendarEventType(CalendarEventType calendarEventType) {
        this.calendarEventType = calendarEventType;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String toShortenedString() {
        return "<html>" +
                    eventTitle +
                    "<br/>" +
                    "Sharp deadline: " + extensiveDateTimeFormatter.format(eventDate) +
                "</html>";
    }
}
