package server.network.clienthandling.logicutils.comparators;

import shareables.network.DTOs.CourseScoreDTO;

import java.util.Comparator;

public class CourseScoreDTOComparator implements Comparator<CourseScoreDTO> {
    @Override
    public int compare(CourseScoreDTO firstCourseScoreDTO, CourseScoreDTO secondCourseScoreDTO) {
        long firstId = Long.parseLong(firstCourseScoreDTO.getCourseId());
        long secondId = Long.parseLong(secondCourseScoreDTO.getCourseId());
        return (int) (firstId - secondId);
    }
}
