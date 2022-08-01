package shareables.utils.timing.timekeeping;

import java.time.LocalDateTime;

public class Timestamp { // TODO: delete if necessary
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public static Timestamp now() {
        Timestamp timestamp = new Timestamp();
        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        timestamp.setDate(currentLocalDateTime);
        return timestamp;
    }

    private void setDate(LocalDateTime localDateTime) {
        setDay(localDateTime); // year/month/day
        setTime(localDateTime); // hour/minute/second
    }

    private void setDay(LocalDateTime localDateTime) { // sets year/month/day
        setYear(localDateTime.getYear());
        setMonth(localDateTime.getMonthValue());
        setDay(localDateTime.getDayOfMonth());
    }

    private void setTime(LocalDateTime localDateTime) { // sets hour/minute/second
        setHour(localDateTime.getHour());
        setMinute(localDateTime.getMinute());
        setSecond(localDateTime.getSecond());
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public String toString() {
        String template = "%04d-%02d-%02d-%02d-%02d-%02d"; // year-month-day-hour-minute-second
        return String.format(template, year, month, day, hour, minute, second);
    }
}
