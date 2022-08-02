package server.database.datasets;

import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.academicrequests.DefenseRequest;
import shareables.models.pojos.academicrequests.DroppingOutRequest;
import shareables.models.pojos.academicrequests.MinorRequest;
import shareables.models.pojos.academicrequests.RecommendationRequest;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.Student;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

public enum DatasetIdentifier {
    STUDENTS("studentsFolderPath", Student.class),
    PROFESSORS("professorsFolderPath", Professor.class),
    COURSES("coursesFolderPath", Course.class),
    DEPARTMENTS("departmentsFolderPath", Department.class),
    DEFENSE_REQUESTS("defenseRequestsFolderPath", DefenseRequest.class),
    DROPPING_OUT_REQUESTS("droppingOutRequestsFolderPath", DroppingOutRequest.class),
    MINOR_REQUESTS("minorRequestsFolderPath", MinorRequest.class),
    RECOMMENDATION_REQUESTS("recommendationRequestsFolderPath", RecommendationRequest.class);

    private String folderPath;
    private Class<? extends Identifiable> classLiteral;

    private DatasetIdentifier(String pathConfigKeyString, Class<? extends Identifiable> classLiteral) {
        folderPath = ConfigManager.getString(ConfigFileIdentifier.ADDRESSES, "datasetsFolderPath") +
                ConfigManager.getString(ConfigFileIdentifier.ADDRESSES, pathConfigKeyString);
        this.classLiteral = classLiteral;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public Class<?> getClassLiteral() {
        return classLiteral;
    }
}
