package client.gui.utils;

import shareables.models.pojos.abstractions.DepartmentName;
import shareables.models.pojos.coursewares.SubmissionType;
import shareables.models.pojos.users.professors.AcademicLevel;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.models.pojos.users.students.StudentStatus;
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
        String[] degreeLevels = new String[DegreeLevel.values().length];
        int index = 0;
        for (DegreeLevel degreeLevel : DegreeLevel.values()) {
            degreeLevels[index++] = degreeLevel.toString();
        }
        return degreeLevels;
    }

    public static String[] initializeWeekdays() {
        String[] weekdays = new String[Weekday.values().length];
        int index = 0;
        for (Weekday weekday : Weekday.values()) {
            weekdays[index++] = weekday.toString();
        }
        return weekdays;
    }

    public static String[] initializeStudentStatusStrings() {
        String[] studentStatusStrings = new String[StudentStatus.values().length];
        int index = 0;
        for (StudentStatus studentStatus : StudentStatus.values()) {
            studentStatusStrings[index++] = studentStatus.toString();
        }
        return studentStatusStrings;
    }

    public static String[] initializeStudentStatusStringsWithoutDropouts() {
        String[] studentStatusStrings = new String[StudentStatus.values().length - 1];
        int index = 0;
        for (StudentStatus studentStatus : StudentStatus.values()) {
            if (studentStatus != StudentStatus.DROPPED_OUT) studentStatusStrings[index++] = studentStatus.toString();
        }
        return studentStatusStrings;
    }

    public static String[] initializeSubmissionTypes() {
        String[] submissionTypes = new String[SubmissionType.values().length];
        int index = 0;
        for (SubmissionType submissionType : SubmissionType.values()) {
            submissionTypes[index++] = submissionType.toString();
        }
        return submissionTypes;
    }
}
