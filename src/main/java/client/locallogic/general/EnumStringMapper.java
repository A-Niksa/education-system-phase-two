package client.locallogic.general;

import shareables.models.pojos.coursewares.SubmissionType;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.utils.timing.timekeeping.Weekday;

public class EnumStringMapper {
    public static Weekday getWeekday(String weekdayString) {
        if (weekdayString.equals("Saturday")) {
            return Weekday.SATURDAY;
        } else if (weekdayString.equals("Sunday")) {
            return Weekday.SUNDAY;
        } else if (weekdayString.equals("Monday")) {
            return Weekday.MONDAY;
        } else if (weekdayString.equals("Tuesday")) {
            return Weekday.TUESDAY;
        } else if (weekdayString.equals("Wednesday")) {
            return Weekday.WEDNESDAY;
        } else if (weekdayString.equals("Thursday")) {
            return Weekday.THURSDAY;
        } else if (weekdayString.equals("Friday")) {
            return Weekday.FRIDAY;
        }
        return null;
    }

    public static DegreeLevel getDegreeLevel(String degreeLevelString) {
        if (degreeLevelString.equals("Undergraduate")) {
            return DegreeLevel.UNDERGRADUATE;
        } else if (degreeLevelString.equals("Graduate")) {
            return DegreeLevel.GRADUATE;
        } else if (degreeLevelString.equals("PhD")) {
            return DegreeLevel.PHD;
        }
        return null;
    }

    public static SubmissionType getSubmissionType(String submissionTypeString) {
        if (submissionTypeString.equals("Text")) {
            return SubmissionType.TEXT;
        } else if (submissionTypeString.equals("Media File")) {
            return SubmissionType.MEDIA_FILE;
        }
        return null;
    }
}
