package server.database.datasets;

import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.academicrequests.DefenseRequest;
import shareables.models.pojos.academicrequests.DroppingOutRequest;
import shareables.models.pojos.academicrequests.MinorRequest;
import shareables.models.pojos.academicrequests.RecommendationRequest;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.specialusers.SpecialUser;
import shareables.models.pojos.users.students.Student;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

public enum DatasetIdentifier {
    STUDENTS("studentsFolderPath", Student.class, "Students Dataset"),
    PROFESSORS("professorsFolderPath", Professor.class, "Professors Dataset"),
    SPECIAL_USERS("specialUsersFolderPath", SpecialUser.class, "Special Users Dataset"),
    COURSES("coursesFolderPath", Course.class, "Courses Dataset"),
    DEPARTMENTS("departmentsFolderPath", Department.class, "Departments Dataset"),
    DEFENSE_REQUESTS("defenseRequestsFolderPath", DefenseRequest.class, "Defense Requests Dataset"),
    DROPPING_OUT_REQUESTS("droppingOutRequestsFolderPath", DroppingOutRequest.class, "Drop-out Requests Dataset"),
    MINOR_REQUESTS("minorRequestsFolderPath", MinorRequest.class, "Minor Requests Dataset"),
    RECOMMENDATION_REQUESTS("recommendationRequestsFolderPath", RecommendationRequest.class,
            "Recommendation Requests Dataset");

    private String folderPath;
    private Class<? extends Identifiable> classLiteral;
    private String datasetName;

    private DatasetIdentifier(String pathConfigKeyString, Class<? extends Identifiable> classLiteral, String datasetName) {
        folderPath = ConfigManager.getString(ConfigFileIdentifier.ADDRESSES, "datasetsFolderPath") +
                ConfigManager.getString(ConfigFileIdentifier.ADDRESSES, pathConfigKeyString);
        this.classLiteral = classLiteral;
        this.datasetName = datasetName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public Class<?> getClassLiteral() {
        return classLiteral;
    }

    @Override
    public String toString() {
        return datasetName;
    }
}
