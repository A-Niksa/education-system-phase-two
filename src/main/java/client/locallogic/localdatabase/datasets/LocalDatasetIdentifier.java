package client.locallogic.localdatabase.datasets;

import client.locallogic.localdatabase.datamodels.QueuedMessage;
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

public enum LocalDatasetIdentifier {
    QUEUED_MESSAGES("queuedMessagesPath", QueuedMessage.class, "Queued Messaged Path");

    private String folderPath;
    private Class<? extends Identifiable> classLiteral;
    private String datasetName;

    private LocalDatasetIdentifier(String pathConfigKeyString, Class<? extends Identifiable> classLiteral, String datasetName) {
        folderPath = ConfigManager.getString(ConfigFileIdentifier.ADDRESSES, "localDatasetsFolderPath") +
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
