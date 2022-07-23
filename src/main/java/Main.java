import shareables.utils.timekeeping.Timestamp;
import shareables.utils.timekeeping.TimestampParser;

public class Main {
    public static void main(String[] args) {
        Timestamp timestamp = Timestamp.now();
        System.out.println(timestamp);
        System.out.println(TimestampParser.toTimestamp(timestamp.toString()));
    }
}
