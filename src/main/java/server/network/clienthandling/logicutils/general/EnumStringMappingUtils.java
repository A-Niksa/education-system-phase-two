package server.network.clienthandling.logicutils.general;

import shareables.models.pojos.abstractions.DepartmentName;

public class EnumStringMappingUtils {
    public static DepartmentName getDepartmentName(String departmentId) {
        DepartmentName departmentName;
        switch (departmentId) {
            case "0":
                departmentName = DepartmentName.GENERAL_CENTERS;
                break;
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

    public static String getDepartmentId(String departmentNameString) {
        String departmentId;
        switch (departmentNameString) {
            case "General Centers":
                departmentId = "0";
                break;
            case "Mathematics":
                departmentId = "1";
                break;
            case "Physics":
                departmentId = "2";
                break;
            case "Economics":
                departmentId = "3";
                break;
            case "Chemistry":
                departmentId = "4";
                break;
            case "Aerospace Engineering":
                departmentId = "5";
                break;
            default:
                departmentId = null; // added for explicitness
        }
        return departmentId;
    }

    public static String getDepartmentId(DepartmentName departmentName) {
        String departmentId;
        switch (departmentName) {
            case GENERAL_CENTERS:
                departmentId = "0";
                break;
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
                departmentId = null; // added for explicitness
        }
        return departmentId;
    }
}
