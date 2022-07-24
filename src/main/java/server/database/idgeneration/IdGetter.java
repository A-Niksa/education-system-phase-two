package server.database.idgeneration;

import server.database.datamodels.abstractions.DepartmentName;
import server.database.datamodels.users.professors.AcademicLevel;
import server.database.datamodels.users.professors.AcademicRole;
import server.database.datamodels.users.students.DegreeLevel;

public class IdGetter {
    public static String getYearOfEntryId(int yearOfEntry) {
        if (yearOfEntry < 2000) {
            return (yearOfEntry + "").substring(1);
        } else {
            return String.format("%02d", yearOfEntry%2000);
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
                academicRoleId = "7";
                break;
            case DEPUTY:
                academicRoleId = "8";
                break;
            case DEAN:
                academicRoleId = "9";
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

    public static String getDepartmentId(DepartmentName departmentName) {
        String departmentId;
        switch (departmentName) {
            case MATHEMATICS:
                departmentId = "1";
                break;
            case PHYSICS:
                departmentId = "2";
                break;
            case ECONOMICS:
                departmentId = "3";
                break;
            case CHEMISTRY:
                departmentId = "4";
                break;
            case AEROSPACE_ENGINEERING:
                departmentId = "5";
                break;
            default:
                departmentId = "-";
        }
        return departmentId;
    }
}
