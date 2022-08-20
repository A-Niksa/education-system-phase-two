package shareables.models.pojos.coursewares;

import shareables.models.idgeneration.IdentifiableWithTime;
import shareables.models.pojos.media.MediaFile;

public class HomeworkSubmission extends IdentifiableWithTime {
    private String ownerId;
    private String text;
    private MediaFile mediaFile;
    private double score;

    public HomeworkSubmission() {
        initializeId();
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MediaFile getMediaFile() {
        return mediaFile;
    }

    public void setMediaFile(MediaFile mediaFile) {
        this.mediaFile = mediaFile;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
