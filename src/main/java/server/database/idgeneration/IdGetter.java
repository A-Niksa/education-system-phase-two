package server.database.idgeneration;

import server.database.datamodels.users.DegreeLevel;

public class IdGetter {
    public static String getYearOfEntryId(int yearOfEntry) {
        if (yearOfEntry == 1400) {
            return "00";
        } else {
            return String.format("%02d", yearOfEntry%1300);
        }
    }

    public static String getDegreeLevelId(DegreeLevel degreeLevel) {
        String degreeLevelId;
        switch (degreeLevel) {
            case UNDERGRADUATE:
                degreeLevelId = "1";
                break;
            case GRADUATE:
                degreeLevelId = "2";
                break;
            case PHD:
                degreeLevelId = "3";
                break;
            default:
                degreeLevelId = "-";
        }
        return degreeLevelId;
    }
}
