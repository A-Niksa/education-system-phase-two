package client.locallogic.menus.enrolment;

import shareables.models.pojos.users.students.DegreeLevel;

public class DegreeLevelGetter {
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
                degreeLevel = null; // for explicitness
        }
        return degreeLevel;
    }
}
