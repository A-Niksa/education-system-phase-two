package shareables.network.DTOs;

public class CourseScoreDTO {
    private String courseId;
    private String courseName;
    private String scoreString;
    private double score;
    private String studentProtest;
    private String professorResponse;

    public CourseScoreDTO() {
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

    public String getScoreString() {
        return scoreString;
    }

    public void setScoreString(String scoreString) {
        this.scoreString = scoreString;
    }

    public String getStudentProtest() {
        return studentProtest;
    }

    public void setStudentProtest(String studentProtest) {
        this.studentProtest = studentProtest;
    }

    public String getProfessorResponse() {
        return professorResponse;
    }

    public void setProfessorResponse(String professorResponse) {
        this.professorResponse = professorResponse;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
