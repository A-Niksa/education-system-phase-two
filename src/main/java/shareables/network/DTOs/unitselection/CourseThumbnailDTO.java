package shareables.network.DTOs.unitselection;

import shareables.models.pojos.users.students.DegreeLevel;
import shareables.utils.timing.formatting.FormattingUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CourseThumbnailDTO {
    private static DateTimeFormatter extensiveDateTimeFormatter;
    static {
        extensiveDateTimeFormatter = FormattingUtils.getExtensiveDateTimeFormatter();
    }

    private String courseId;
    private String courseName;
    private LocalDateTime examDate;
    private int groupNumber;
    private int currentCapacity;
    private DegreeLevel degreeLevel;
    private List<String> teachingProfessorNames;
    private boolean isAcquired;
    private boolean isPinnedToFavorites;

    public CourseThumbnailDTO() {
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public LocalDateTime getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDateTime examDate) {
        this.examDate = examDate;
    }

    public DegreeLevel getDegreeLevel() {
        return degreeLevel;
    }

    public void setDegreeLevel(DegreeLevel degreeLevel) {
        this.degreeLevel = degreeLevel;
    }

    public List<String> getTeachingProfessorNames() {
        return teachingProfessorNames;
    }

    public void setTeachingProfessorNames(List<String> teachingProfessorNames) {
        this.teachingProfessorNames = teachingProfessorNames;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public boolean isAcquired() {
        return isAcquired;
    }

    public void setAcquired(boolean acquired) {
        isAcquired = acquired;
    }

    public boolean isPinnedToFavorites() {
        return isPinnedToFavorites;
    }

    public void setPinnedToFavorites(boolean pinnedToFavorites) {
        isPinnedToFavorites = pinnedToFavorites;
    }

    @Override
    public String toString() {
        return "<html>" +
                    courseId + " - " + groupNumber + " - " + courseName +
                    "<br/>" +
                    "Teaching professor(s): " + teachingProfessorsToString() +
                    "<br/>" +
                    degreeLevel +
                    "<br/>" +
                    "Exam date: " + extensiveDateTimeFormatter.format(examDate) +
                    "<br/>" +
                    "Remaining capacity: " + currentCapacity +
                    "<br/>" +
                    "Acquired: " + acquiredToString(isAcquired) +
                "</html>";
    }

    private String acquiredToString(boolean isAcquired) {
        return isAcquired ? "Yes" : "No";
    }

    public String teachingProfessorsToString() {
        StringBuilder teachingProfessorsNameStringBuilder = new StringBuilder();
        for (String professorName : teachingProfessorNames) {
            teachingProfessorsNameStringBuilder.append(professorName).append(", ");
        }
        int stringBuilderLength = teachingProfessorsNameStringBuilder.length();
        return teachingProfessorsNameStringBuilder.delete(stringBuilderLength - 2, stringBuilderLength - 1).toString();
    }
}
