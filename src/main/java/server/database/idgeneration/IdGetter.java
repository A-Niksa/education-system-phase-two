package server.database.idgeneration;

import server.database.datamodels.users.AcademicLevel;
import server.database.datamodels.users.AcademicRole;
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

    public static String getAcademicRoleId(AcademicRole academicRole) {
        String academicRoleId;
        switch (academicRole) {
            case NORMAL:
                academicRoleId = "1";
                break;
            case DEPUTY:
                academicRoleId = "2";
                break;
            case DEAN:
                academicRoleId = "3";
                break;
            default:
                academicRoleId = "-";
        }
        return academicRoleId;
    }

    public static String getAcademicLevelId(AcademicLevel academicLevel) {
        String academicLevelId;
        switch (academicLevel) {
            case ASSISTANT:
                academicLevelId = "1";
                break;
            case ASSOCIATE:
                academicLevelId = "2";
                break;
            case FULL:
                academicLevelId = "3";
                break;
            default:
                academicLevelId = "-";
        }
        return academicLevelId;
    }
}
