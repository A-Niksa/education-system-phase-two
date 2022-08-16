package shareables.utils.timing.timekeeping;

public class DayTime implements Comparable {
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

    @Override
    public int compareTo(Object o) {
        DayTime anotherDayTime = (DayTime) o;
        int anotherHour = anotherDayTime.getHour();
        int anotherMinute = anotherDayTime.getMinute();
        int anotherSecond = anotherDayTime.getSecond();

        if (hour == anotherHour && minute == anotherMinute && second == anotherSecond) return 0;

        if (hour < anotherHour) {
            return -1;
        } else {
            if (hour == anotherHour) {
                if (minute < anotherMinute) {
                    return -1;
                } else {
                    if (minute == anotherMinute) {
                        if (second < anotherSecond) {
                            return -1;
                        } else { // second > anotherSecond
                            return 1;
                        }
                    } else { // minute > anotherMinute
                        return 1;
                    }
                }
            } else { // hour > anotherHour
                return 1;
            }
        }
    }
}