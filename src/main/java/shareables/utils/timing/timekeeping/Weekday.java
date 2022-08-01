package shareables.utils.timing.timekeeping;

public enum Weekday {
    SATURDAY("Saturday"),
    SUNDAY("Sunday"),
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday");

    private String weekdayString;

    Weekday(String weekdayString) {
        this.weekdayString = weekdayString;
    }

    @Override
    public String toString() {
        return weekdayString;
    }
}
