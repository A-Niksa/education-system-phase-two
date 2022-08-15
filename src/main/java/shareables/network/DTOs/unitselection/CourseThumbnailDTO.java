package shareables.network.DTOs.unitselection;

import shareables.models.pojos.users.students.DegreeLevel;

import java.util.List;

public class CourseThumbnailDTO {
    private String courseId;
    private String courseName;
    private String examDateString;
    private int groupNumber;
    private int currentCapacity;
    private DegreeLevel degreeLevel;
    private List<String> teachingProfessorNames;

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

    public String getExamDateString() {
        return examDateString;
    }

    public void setExamDateString(String examDateString) {
        this.examDateString = examDateString;
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

    @Override
    public String toString() {
        return "<html>" +
                    courseId + " - " + groupNumber + " - " + courseName +
                    "<br/>" +
                    "Teaching professor(s): " + teachingProfessorsToString() +
                    "<br/>" +
                    degreeLevel +
                    "<br/>" +
                    "Exam date: " + examDateString +
                    "<br/>" +
                    "Remaining capacity: " + currentCapacity +
                "</html>";
    }

    private String teachingProfessorsToString() {
        StringBuilder teachingProfessorsNameStringBuilder = new StringBuilder();
        for (String professorName : teachingProfessorNames) {
            teachingProfessorsNameStringBuilder.append(professorName).append(", ");
        }
        int stringBuilderLength = teachingProfessorsNameStringBuilder.length();
        return teachingProfessorsNameStringBuilder.delete(stringBuilderLength - 2, stringBuilderLength - 1).toString();
    }
}
