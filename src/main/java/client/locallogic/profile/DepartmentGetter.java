package client.locallogic.profile;

import shareables.models.pojos.abstractions.DepartmentName;

public class DepartmentGetter {
    public static DepartmentName getDepartmentNameById(String id) {
        DepartmentName departmentName;
        switch (id) {
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
                departmentName = null; // added for explicitness
        }
        return departmentName;
    }

    public static DepartmentName getDepartmentNameByString(String departmentNameString) {
        DepartmentName departmentName;
        switch (departmentNameString) {
            case "Mathematics":
                departmentName = DepartmentName.MATHEMATICS;
                break;
            case "Physics":
                departmentName = DepartmentName.PHYSICS;
                break;
            case "Economics":
                departmentName = DepartmentName.ECONOMICS;
                break;
            case "Chemistry":
                departmentName = DepartmentName.CHEMISTRY;
                break;
            case "Aerospace Engineering":
                departmentName = DepartmentName.AEROSPACE_ENGINEERING;
                break;
            default:
                departmentName = null; // added for explicitness
        }
        return departmentName;
    }
}
