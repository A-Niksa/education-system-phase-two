package shareables.network.DTOs.coursewares;

import shareables.utils.timing.formatting.FormattingUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubmissionThumbnailDTO {
    private static DateTimeFormatter extensiveDateTimeFormatter;
    static {
        extensiveDateTimeFormatter = FormattingUtils.getExtensiveDateTimeFormatter();
    }

    private String id;
    private String studentId;
    private LocalDateTime uploadedAt;
    private Double score;

    public SubmissionThumbnailDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "<html>" +
                    id + " - " + studentId +
                    "<br/>" +
                    extensiveDateTimeFormatter.format(uploadedAt) +
                    "<br/>" +
                    "Score: " + scoreToString() +
                "</html>";
    }

    public String toStringForTA() {
        return "<html>" +
                    id + " - " + getAnonymousId(studentId) +
                    "<br/>" +
                    extensiveDateTimeFormatter.format(uploadedAt) +
                    "<br/>" +
                    "Score: " + scoreToString() +
                "</html>";
    }

    private String getAnonymousId(String studentId) {
        StringBuilder anonymousIdBuilder = new StringBuilder();
        for (int i = 0; i < studentId.length(); i++) {
            anonymousIdBuilder.append("*");
        }
        return anonymousIdBuilder.toString();
    }

    private String scoreToString() {
        return score == null ? "Not scored" : String.format("%.2f", score);
    }
}