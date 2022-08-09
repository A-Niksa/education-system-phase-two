package shareables.utils.timing.timekeeping;

public enum Weekday {
    SATURDAY("Saturday", 0),
    SUNDAY("Sunday", 1),
    MONDAY("Monday", 2),
    TUESDAY("Tuesday", 3),
    WEDNESDAY("Wednesday", 4),
    THURSDAY("Thursday", 5),
    FRIDAY("Friday", 6);

    private String weekdayString;
    private int index;

    Weekday(String weekdayString, int index) {
        this.weekdayString = weekdayString;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return weekdayString;
    }
}
