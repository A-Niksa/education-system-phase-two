package server.database.testdata.utils;

import shareables.models.pojos.abstractions.DepartmentName;
import shareables.models.pojos.users.professors.AcademicLevel;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.utils.timing.timekeeping.Weekday;

public class EnumExtractionUtils {
    public static AcademicLevel getAcademicLevel(String academicLevelString) {
        AcademicLevel academicLevel;
        switch (academicLevelString) {
            case "Full":
                academicLevel = AcademicLevel.FULL;
                break;
            case "Associate":
                academicLevel = AcademicLevel.ASSOCIATE;
                break;
            case "Assistant":
                academicLevel = AcademicLevel.ASSISTANT;
                break;
            default:
                academicLevel = null;
        }
        return academicLevel;
    }

    public static DepartmentName getDepartmentName(String departmentNameString) {
        DepartmentName departmentName;
        switch (departmentNameString) {
            case "Mathematics":
                departmentName = DepartmentName.MATHEMATICS;
                break;
            case "Physics":
                departmentName = DepartmentName.PHYSICS;
                break;
            case "General Centers":
                departmentName = DepartmentName.GENERAL_CENTERS;
                break;
            default:
                departmentName = null;
        }
        return departmentName;
    }

    public static DegreeLevel getDegreeLevel(String degreeLevelString) {
        DegreeLevel degreeLevel;
        switch (degreeLevelString) {
            case "Undergraduate":
                degreeLevel = DegreeLevel.UNDERGRADUATE;
                break;
            case "Graduate":
                degreeLevel = DegreeLevel.GRADUATE;
                break;
            case "PhD":
                degreeLevel = DegreeLevel.PHD;
                break;
            default:
                degreeLevel = null;
        }
        return degreeLevel;
    }

    public static Weekday getWeekday(String weekdayString) {
        Weekday weekday;
        switch (weekdayString) {
            case "Saturday":
                weekday = Weekday.SATURDAY;
                break;
            case "Sunday":
                weekday = Weekday.SUNDAY;
                break;
            case "Monday":
                weekday = Weekday.MONDAY;
                break;
            case "Tuesday":
                weekday = Weekday.TUESDAY;
                break;
            case "Wednesday":
                weekday = Weekday.WEDNESDAY;
                break;
            case "Thursday":
                weekday = Weekday.THURSDAY;
                break;
            case "Friday":
                weekday = Weekday.FRIDAY;
                break;
            default:
                weekday = null;
        }
        return weekday;
    }

    public static String getIdentifiableNumeral(int identifiableIndex) {
        String studentNumeral;
        switch (identifiableIndex) {
            case 0:
                studentNumeral = "first";
                break;
            case 1:
                studentNumeral = "second";
                break;
            case 2:
                studentNumeral = "third";
                break;
            case 3:
                studentNumeral = "fourth";
                break;
            default:
                studentNumeral = null;
        }
        return studentNumeral;
    }
}
