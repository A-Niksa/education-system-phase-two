package server.database.datamodels.academicrequests;

import javax.persistence.*;

@Entity
public class RecommendationRequest extends AcademicRequest {
    @Column
    private String recommendationText;

    public void saveGeneratedRecommendationText() {
        String professorName = receivingProfessor.getFirstName() + " " + receivingProfessor.getLastName();
        String studentName = requestingStudent.getFirstName() + " " + requestingStudent.getLastName();
        recommendationText = "I, " + professorName + ", hereby declare that " + studentName + " has been one of" +
                " my students and based on the level of excellence he has demonstrated in a plethora of different aspects" +
                " at the courses I have taught, I recommend him to be admitted to your academic program.";
    }


    public String getRecommendationText() {
        return AcademicRequestUtils.convertToHTMLFormat(recommendationText);
    }

    public void setRecommendationText(String recommendationText) {
        this.recommendationText = recommendationText;
    }
}
