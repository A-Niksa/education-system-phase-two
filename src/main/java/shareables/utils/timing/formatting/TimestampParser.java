package shareables.utils.timing.formatting;

import shareables.utils.timing.timekeeping.Timestamp;

public class TimestampParser {
    private static TimestampParser timestampParser;

    private TimestampParser() {}

    private static TimestampParser getInstance() {
        if (timestampParser == null) {
            timestampParser = new TimestampParser();
        }

        return timestampParser;
    }

    public static Timestamp toTimestamp(String timestampString) {
        Timestamp timestamp = new Timestamp();
        getInstance().setDate(timestampString, timestamp);
        return timestamp;
    }

    private void setDate(String timestampString, Timestamp timestamp) {
        setDay(timestampString, timestamp);
        setTime(timestampString, timestamp);
    }

    private void setDay(String timestampString, Timestamp timestamp) {
        int year = Integer.parseInt(timestampString.substring(0, 4));
        int month = Integer.parseInt(timestampString.substring(5, 7));
        int day = Integer.parseInt(timestampString.substring(8, 10));
        timestamp.setYear(year);
        timestamp.setMonth(month);
        timestamp.setDay(day);
    }

    private void setTime(String timestampString, Timestamp timestamp) {
        int hour = Integer.parseInt(timestampString.substring(11, 13));
        int minute = Integer.parseInt(timestampString.substring(14, 16));
        int second = Integer.parseInt(timestampString.substring(17, 19));
        timestamp.setHour(hour);
        timestamp.setMinute(minute);
        timestamp.setSecond(second);
    }
}
