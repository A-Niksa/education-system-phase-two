package client.locallogic.menus.addition;

import shareables.models.pojos.users.professors.AcademicLevel;

public class ProfessorBlueprintHelper {
    public static AcademicLevel getAcademicLevelWithString(String academicLevelString) {
        AcademicLevel academicLevel;
        switch (academicLevelString) {
            case "Assistant Professor":
                academicLevel = AcademicLevel.ASSISTANT;
                break;
            case "Associate Professor":
                academicLevel = AcademicLevel.ASSOCIATE;
                break;
            case "Full Professor":
                academicLevel = AcademicLevel.FULL;
                break;
            default:
                academicLevel = null; // put here for explicitness
        }
        return academicLevel;
    }
}
