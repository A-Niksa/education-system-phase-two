package client.gui.utils;

import shareables.models.pojos.abstractions.DepartmentName;

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
}
