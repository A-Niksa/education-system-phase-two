package shareables.models.pojos.academicrequests;

public class RecommendationRequest extends AcademicRequest {
    private String recommendationText;

    public RecommendationRequest() {
        super(AcademicRequestIdentifier.RECOMMENDATION);
    }

    public void saveGeneratedRecommendationText() {
        String professorName = receivingProfessor.fetchName();
        String studentName = requestingStudent.fetchName();
        recommendationText = "I, " + professorName + ", hereby declare that " + studentName + " has been one of" +
                " my students and based on the level of excellence he has demonstrated in a plethora of different aspects" +
                " at the courses I have taught, I recommend him to be admitted to your academic program."; // TODO: config
    }


    public String getRecommendationText() {
        return AcademicRequestUtils.convertToHTMLFormat(recommendationText);
    }

    public void setRecommendationText(String recommendationText) {
        this.recommendationText = recommendationText;
    }
}
