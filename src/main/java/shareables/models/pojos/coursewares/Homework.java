package shareables.models.pojos.coursewares;

import shareables.models.idgeneration.IdentifiableWithTime;
import shareables.models.pojos.media.MediaFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Homework extends IdentifiableWithTime {
    private String title;
    private LocalDateTime startingTime;
    private LocalDateTime endingTime;
    private LocalDateTime permissibleSubmittingTime;
    private String text;
    private MediaFile mediaFile;
    private SubmissionType submissionType;
    private List<HomeworkSubmission> homeworkSubmissions;

    public Homework() {
        homeworkSubmissions = new ArrayList<>();
        initializeId();
    }

    public void addToHomeworkSubmissions(HomeworkSubmission homeworkSubmission) {
        homeworkSubmissions.add(homeworkSubmission);
    }

    public void removeFromHomeworkSubmissions(String homeworkSubmissionId) {
        homeworkSubmissions.removeIf(e -> e.getId().equals(homeworkSubmissionId));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalDateTime startingTime) {
        this.startingTime = startingTime;
    }

    public LocalDateTime getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(LocalDateTime endingTime) {
        this.endingTime = endingTime;
    }

    public LocalDateTime getPermissibleSubmittingTime() {
        return permissibleSubmittingTime;
    }

    public void setPermissibleSubmittingTime(LocalDateTime permissibleSubmittingTime) {
        this.permissibleSubmittingTime = permissibleSubmittingTime;
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

    public SubmissionType getSubmissionType() {
        return submissionType;
    }

    public void setSubmissionType(SubmissionType submissionType) {
        this.submissionType = submissionType;
    }
}