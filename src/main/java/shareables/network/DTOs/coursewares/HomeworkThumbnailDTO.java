package shareables.network.DTOs.coursewares;

import shareables.utils.timing.formatting.FormattingUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HomeworkThumbnailDTO {
    private static DateTimeFormatter extensiveDateTimeFormatter;
    static {
        extensiveDateTimeFormatter = FormattingUtils.getExtensiveDateTimeFormatter();
    }

    private String id;
    private String title;
    private LocalDateTime startingDate;
    private LocalDateTime preliminaryDeadlineDate;
    private LocalDateTime sharpDeadlineDate;

    public HomeworkThumbnailDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDateTime startingDate) {
        this.startingDate = startingDate;
    }

    public LocalDateTime getPreliminaryDeadlineDate() {
        return preliminaryDeadlineDate;
    }

    public void setPreliminaryDeadlineDate(LocalDateTime preliminaryDeadlineDate) {
        this.preliminaryDeadlineDate = preliminaryDeadlineDate;
    }

    public LocalDateTime getSharpDeadlineDate() {
        return sharpDeadlineDate;
    }

    public void setSharpDeadlineDate(LocalDateTime sharpDeadlineDate) {
        this.sharpDeadlineDate = sharpDeadlineDate;
    }

    @Override
    public String toString() {
        return "<html>" +
                    id + " - " + title +
                    "<br/>" +
                    "Starts At: " + extensiveDateTimeFormatter.format(startingDate) +
                    "<br/>" +
                    "Normal Deadline: " + extensiveDateTimeFormatter.format(preliminaryDeadlineDate) +
                    "<br/>" +
                    "Sharp Deadline: " + extensiveDateTimeFormatter.format(sharpDeadlineDate) +
                "</html>";
    }
}
