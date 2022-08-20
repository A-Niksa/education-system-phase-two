package server.network.clienthandling.logicutils.comparators;

import shareables.network.DTOs.generalmodels.CourseDTO;

import java.util.Comparator;

public class CourseDTOComparator implements Comparator<CourseDTO> {
    @Override
    public int compare(CourseDTO firstCourseDTO, CourseDTO secondCourseDTO) {
        long firstId = Long.parseLong(firstCourseDTO.getId());
        long secondId = Long.parseLong(secondCourseDTO.getId());
        return (int) (firstId - secondId);
    }
}
