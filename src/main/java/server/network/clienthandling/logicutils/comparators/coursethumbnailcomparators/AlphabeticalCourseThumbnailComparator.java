package server.network.clienthandling.logicutils.comparators.coursethumbnailcomparators;

import shareables.network.DTOs.unitselection.CourseThumbnailDTO;

import java.util.Comparator;

public class AlphabeticalCourseThumbnailComparator implements Comparator<CourseThumbnailDTO> {
    @Override
    public int compare(CourseThumbnailDTO firstDTO, CourseThumbnailDTO secondDTO) {
        return firstDTO.getCourseName().compareTo(secondDTO.getCourseName());
    }
}
