package shareables.network.DTOs;

import shareables.utils.timekeeping.FormattingUtils;
import shareables.utils.timekeeping.WeekTime;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class CourseDTO {
    private String courseName;
    private String compressedNamesOfProfessors;
    private List<WeekTime> weeklyClassTimes;
    private LocalDateTime examDate;

    public CourseDTO() {
    }

    public CourseDTO(String courseName, String compressedNamesOfProfessors, List<WeekTime> weeklyClassTimes, LocalDateTime examDate) {
        this.courseName = courseName;
        this.compressedNamesOfProfessors = compressedNamesOfProfessors;
        this.weeklyClassTimes = weeklyClassTimes;
        this.examDate = examDate;
    }

    public String fetchFormattedExamDate() {
        DateTimeFormatter dateTimeFormatter = FormattingUtils.getExtensiveDateTimeFormatter();
        return dateTimeFormatter.format(examDate);
    }

    public LocalDateTime getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDateTime examDate) {
        this.examDate = examDate;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCompressedNamesOfProfessors() {
        return compressedNamesOfProfessors;
    }

    public List<WeekTime> getWeeklyClassTimes() {
        return weeklyClassTimes;
    }
}
