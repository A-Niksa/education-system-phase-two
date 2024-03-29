package client.locallogic.menus.services;

import shareables.network.DTOs.generalmodels.CourseDTO;

import java.time.LocalDateTime;
import java.util.Comparator;

public class ExamDateComparator implements Comparator<CourseDTO> {
    @Override
    public int compare(CourseDTO firstCourseDTO, CourseDTO secondCourseDTO) {
        LocalDateTime firstExamDate = firstCourseDTO.getExamDate();
        LocalDateTime secondExamDate = secondCourseDTO.getExamDate();
        return firstExamDate.compareTo(secondExamDate);
    }
}
