package server.database.idgeneration;

import server.database.datamodels.users.DegreeLevel;

public class IdGetter {
    public static String getYearOfEntryId(int yearOfEntry) {
        if (yearOfEntry == 1400) {
            return "00";
        } else {
            return String.format("%2d", yearOfEntry%1300);
        }
    }

    public static String getDegreeLevelId(DegreeLevel degreeLevel) {
        switch (degreeLevel) {
            case UNDERGRADUATE:
                return "1";
            case GRADUATE:
                return "2";
            case PHD:
                return "3";
            default:
                return "-";
        }
    }
}
