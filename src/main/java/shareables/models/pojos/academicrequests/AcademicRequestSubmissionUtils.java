package shareables.models.pojos.academicrequests;

import shareables.models.pojos.abstractions.DepartmentName;

public class AcademicRequestSubmissionUtils {
    public static String convertToHTMLFormat(String text) {
        return "<html>" + text.replaceAll("\n", "<br>");
    }

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
                departmentName = null; // for explicitness
        }
        return departmentName;
    }
}
