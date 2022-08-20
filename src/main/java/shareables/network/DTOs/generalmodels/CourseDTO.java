package shareables.network.DTOs.generalmodels;

import shareables.models.pojos.users.students.DegreeLevel;
import shareables.utils.timing.formatting.FormattingUtils;
import shareables.utils.timing.timekeeping.WeekTime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CourseDTO {
    private String id;
    private String courseName;
    private String departmentId;
    private String compressedNamesOfProfessors;
    private int numberOfCredits;
    private List<WeekTime> weeklyClassTimes;
    private LocalDateTime examDate;
    private DegreeLevel degreeLevel;

    public CourseDTO() {
    }

    public String fetchFormattedExamDate() {
        DateTimeFormatter dateTimeFormatter = FormattingUtils.getExtensiveDateTimeFormatter();
        return dateTimeFormatter.format(examDate);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getCompressedNamesOfProfessors() {
        return compressedNamesOfProfessors;
    }

    public void setCompressedNamesOfProfessors(String compressedNamesOfProfessors) {
        this.compressedNamesOfProfessors = compressedNamesOfProfessors;
    }

    public int getNumberOfCredits() {
        return numberOfCredits;
    }

    public void setNumberOfCredits(int numberOfCredits) {
        this.numberOfCredits = numberOfCredits;
    }

    public List<WeekTime> getWeeklyClassTimes() {
        return weeklyClassTimes;
    }

    public void setWeeklyClassTimes(List<WeekTime> weeklyClassTimes) {
        this.weeklyClassTimes = weeklyClassTimes;
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
}
