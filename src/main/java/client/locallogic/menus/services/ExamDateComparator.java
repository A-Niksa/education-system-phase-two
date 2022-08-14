package client.locallogic.menus.services;

import shareables.network.DTOs.CourseDTO;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;

public class ExamDateComparator implements Comparator<CourseDTO> {
    @Override
    public int compare(CourseDTO firstCourseDTO, CourseDTO secondCourseDTO) {
        LocalDateTime firstExamDate = firstCourseDTO.getExamDate();
        LocalDateTime secondExamDate = secondCourseDTO.getExamDate();
        return firstExamDate.compareTo(secondExamDate);
    }
}
