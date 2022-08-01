package shareables.utils.timekeeping;

public class DayTime {
    private int hour;
    private int minute;
    private int second;

    public DayTime() {
    }

    public DayTime(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    @Override
    public String toString() {
        String hh = String.format("%02d", hour);
        String mm = String.format("%02d", minute);
        String ss = String.format("%02d", second);
        return hh + ":" + mm + ":" + ss;
    }

    public boolean equals(DayTime dayTime) {
        return hour == dayTime.getHour() &&
                minute == dayTime.getMinute() &&
                second == dayTime.getSecond();
    }
}
