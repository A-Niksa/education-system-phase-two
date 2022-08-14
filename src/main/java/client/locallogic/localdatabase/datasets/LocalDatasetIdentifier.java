package client.locallogic.localdatabase.datasets;

import client.locallogic.localdatabase.datamodels.QueuedMessage;
import shareables.models.idgeneration.Identifiable;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

public enum LocalDatasetIdentifier {
    QUEUED_MESSAGES("queuedMessagesFolderPath", QueuedMessage.class, "Queued Messaged Path");

    private String path;
    private Class<? extends Identifiable> classLiteral;
    private String datasetName;

    LocalDatasetIdentifier(String pathConfigKeyString, Class<? extends Identifiable> classLiteral, String datasetName) {
        path = ConfigManager.getString(ConfigFileIdentifier.ADDRESSES, pathConfigKeyString);
        this.classLiteral = classLiteral;
        this.datasetName = datasetName;
    }

    public String getPath() {
        return path;
    }

    public Class<?> getClassLiteral() {
        return classLiteral;
    }

    @Override
    public String toString() {
        return datasetName;
    }
}
