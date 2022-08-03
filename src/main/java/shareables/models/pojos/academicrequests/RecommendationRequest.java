package shareables.models.pojos.academicrequests;

import server.database.datasets.DatasetIdentifier;
import shareables.models.idgeneration.SequentialIdGenerator;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

public class RecommendationRequest extends AcademicRequest {
    private static SequentialIdGenerator sequentialIdGenerator;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator(DatasetIdentifier.RECOMMENDATION_REQUESTS);
    }

    private String recommendationText;
    private ConfigFileIdentifier configIdentifier;

    public RecommendationRequest() {
        super(AcademicRequestIdentifier.RECOMMENDATION);
        initializeId(sequentialIdGenerator);
        configIdentifier = ConfigFileIdentifier.ACADEMIC_REQUEST_TEXTS;
    }

    public void saveGeneratedRecommendationText(String receivingProfessorName, String requestingStudentName) {
        recommendationText = ConfigManager.getString(configIdentifier, "recommendationText1") + receivingProfessorName
                + ConfigManager.getString(configIdentifier, "recommendationText2") + requestingStudentName
                + ConfigManager.getString(configIdentifier, "recommendationText3");
    }

    public String fetchFormattedRecommendationText() {
        return AcademicRequestSubmissionUtils.convertToHTMLFormat(recommendationText);
    }

    public String getRecommendationText() {
        return recommendationText;
    }

    public void setRecommendationText(String recommendationText) {
        this.recommendationText = recommendationText;
    }
}
