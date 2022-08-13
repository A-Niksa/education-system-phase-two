package shareables.network.DTOs.standing;

public class CourseStatsDTO {
    private int numberOfPassingStudents;
    private int numberOfFailingStudents;
    private double courseAverageScore;
    private double courseAverageScoreWithoutFailingStudents;

    public CourseStatsDTO() {

    }

    public int getNumberOfPassingStudents() {
        return numberOfPassingStudents;
    }

    public void setNumberOfPassingStudents(int numberOfPassingStudents) {
        this.numberOfPassingStudents = numberOfPassingStudents;
    }

    public int getNumberOfFailingStudents() {
        return numberOfFailingStudents;
    }

    public void setNumberOfFailingStudents(int numberOfFailingStudents) {
        this.numberOfFailingStudents = numberOfFailingStudents;
    }

    public double getCourseAverageScore() {
        return courseAverageScore;
    }

    public void setCourseAverageScore(double courseAverageScore) {
        this.courseAverageScore = courseAverageScore;
    }

    public double getCourseAverageScoreWithoutFailingStudents() {
        return courseAverageScoreWithoutFailingStudents;
    }

    public void setCourseAverageScoreWithoutFailingStudents(double courseAverageScoreWithoutFailingStudents) {
        this.courseAverageScoreWithoutFailingStudents = courseAverageScoreWithoutFailingStudents;
    }
}