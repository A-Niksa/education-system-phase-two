package shareables.network.DTOs.coursewares;

import shareables.utils.timing.formatting.FormattingUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MaterialThumbnailDTO {
    private static DateTimeFormatter extensiveDateTimeFormatter;
    static {
        extensiveDateTimeFormatter = FormattingUtils.getExtensiveDateTimeFormatter();
    }

    private String id;
    private String title;
    private LocalDateTime uploadDate;

    public MaterialThumbnailDTO() {
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

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public String toString() {
        return "<html>" +
                    id + " - " + title +
                    "<br/>" +
                    extensiveDateTimeFormatter.format(uploadDate) +
                "</html>";
    }
}
