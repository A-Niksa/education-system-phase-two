package client.gui.utils;

import shareables.models.pojos.abstractions.DepartmentName;
import shareables.models.pojos.users.professors.AcademicLevel;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.utils.timing.timekeeping.Weekday;

public class EnumArrayUtils {
    public static String[] initializeAcademicLevels() {
        String[] academicLevels = new String[AcademicLevel.values().length];
        int index = 0;
        for (AcademicLevel academicLevel : AcademicLevel.values()) {
            academicLevels[index++] = academicLevel.toString();
        }
        return academicLevels;
    }

    public static String[] initializeDepartmentNameStrings() {
        String[] departmentNameStrings = new String[DepartmentName.values().length];
        int index = 0;
        for (DepartmentName departmentName : DepartmentName.values()) {
            departmentNameStrings[index++] = departmentName.toString();
        }
        return departmentNameStrings;
    }

    public static String[] initializeDegreeLevels() {
        String[] degreeLevels = new String[3];
        int index = 0;
        for (DegreeLevel degreeLevel : DegreeLevel.values()) {
            degreeLevels[index++] = degreeLevel.toString();
        }
        return degreeLevels;
    }

    public static String[] initializeWeekdays() {
        String[] weekdays = new String[7];
        int index = 0;
        for (Weekday weekday : Weekday.values()) {
            weekdays[index++] = weekday.toString();
        }
        return weekdays;
    }
}
