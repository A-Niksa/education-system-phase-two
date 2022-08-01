package shareables.network.DTOs;

import shareables.models.pojos.users.professors.Professor;
import shareables.utils.timekeeping.WeekTime;

import java.util.List;

public class CourseDTO {
    private String courseName;
    private String compressedNamesOfProfessors;
    private List<WeekTime> weeklyClassTimes;

    public CourseDTO(String courseName, String compressedNamesOfProfessors, List<WeekTime> weeklyClassTimes) {
        this.courseName = courseName;
        this.compressedNamesOfProfessors = compressedNamesOfProfessors;
        this.weeklyClassTimes = weeklyClassTimes;
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
