package client.locallogic.localdatabase.datasets;

import client.locallogic.localdatabase.datamodels.QueuedMessage;
import shareables.models.idgeneration.Identifiable;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

public enum LocalDatasetIdentifier {
    QUEUED_MESSAGES(QueuedMessage.class, "Queued Messages Path");

    private Class<? extends Identifiable> classLiteral;
    private String datasetName;

    LocalDatasetIdentifier(Class<? extends Identifiable> classLiteral, String datasetName) {
        this.classLiteral = classLiteral;
        this.datasetName = datasetName;
    }

    public Class<?> getClassLiteral() {
        return classLiteral;
    }

    @Override
    public String toString() {
        return datasetName;
    }
}
