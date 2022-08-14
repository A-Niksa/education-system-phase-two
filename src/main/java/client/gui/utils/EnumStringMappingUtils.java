package client.gui.utils;

import shareables.models.pojos.abstractions.DepartmentName;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.models.pojos.users.students.StudentStatus;

public class EnumStringMappingUtils {
    public static DepartmentName getDepartmentName(String departmentId) {
        DepartmentName departmentName;
        switch (departmentId) {
            case "1":
                departmentName = DepartmentName.MATHEMATICS;
                break;
            case "2":
                departmentName = DepartmentName.PHYSICS;
                break;
            case "3":
                departmentName = DepartmentName.ECONOMICS;
                break;
            case "4":
                departmentName = DepartmentName.CHEMISTRY;
                break;
            case "5":
                departmentName = DepartmentName.AEROSPACE_ENGINEERING;
                break;
            default:
                departmentName = null; // put here for explicitness
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

    public static StudentStatus getStudentStatus(String studentStatusString) {
        StudentStatus studentStatus;
        switch (studentStatusString) {
            case "Currently studying":
                studentStatus = StudentStatus.CURRENTLY_STUDYING;
                break;
            case "Graduated":
                studentStatus = StudentStatus.GRADUATED;
                break;
            case "Dropped out":
                studentStatus = StudentStatus.DROPPED_OUT;
                break;
            default:
                studentStatus = null;
        }
        return studentStatus;
    }
}
