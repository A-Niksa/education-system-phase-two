package client.locallogic.enrolment;

import shareables.models.pojos.users.students.DegreeLevel;

public class CourseLevelGetter {
    public static DegreeLevel getCourseLevel(String courseLevelString) {
        DegreeLevel courseLevel;
        switch (courseLevelString) {
            case "Undergraduate":
                courseLevel = DegreeLevel.UNDERGRADUATE;
                break;
            case "Graduate":
                courseLevel = DegreeLevel.GRADUATE;
                break;
            case "PhD":
                courseLevel = DegreeLevel.PHD;
                break;
            default:
                courseLevel = null; // for explicitness
        }
        return courseLevel;
    }
}
