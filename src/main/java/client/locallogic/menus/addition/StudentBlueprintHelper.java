package client.locallogic.menus.addition;

import shareables.models.pojos.users.students.StudentStatus;

public class StudentBlueprintHelper {
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
                studentStatus = null; // put here for explicitness
        }
        return studentStatus;
    }
}
