package shareables.network.DTOs.coursewares;

public enum CalendarEventType {
    EXAM("Exam"),
    HOMEWORK("Homework");

    private String calendarEventTypeString;

    CalendarEventType(String calendarEventTypeString) {
        this.calendarEventTypeString = calendarEventTypeString;
    }

    @Override
    public String toString() {
        return calendarEventTypeString;
    }
}
