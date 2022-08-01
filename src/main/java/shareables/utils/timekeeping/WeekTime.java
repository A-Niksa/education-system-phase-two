package shareables.utils.timekeeping;

public class WeekTime {
    private Weekday weekday;
    private DayTime startTime;
    private DayTime endTime;

    public WeekTime(Weekday weekday, DayTime startTime, DayTime endTime) {
        this.weekday = weekday;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Weekday getWeekday() {
        return weekday;
    }

    public String startTimeToString() {
        return startTime.toString();
    }

    public DayTime getStartTime() {
        return startTime;
    }

    public String endTimeToString() {
        return endTime.toString();
    }

    public DayTime getEndTime() {
        return endTime;
    }

    public boolean equals(WeekTime weekTime) {
        return weekday == weekTime.getWeekday() &&
                startTime.equals(weekTime.getStartTime()) &&
                endTime.equals(weekTime.getEndTime());
    }
}