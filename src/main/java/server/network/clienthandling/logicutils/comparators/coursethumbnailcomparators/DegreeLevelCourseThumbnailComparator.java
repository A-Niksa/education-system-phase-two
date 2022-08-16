package server.network.clienthandling.logicutils.comparators.coursethumbnailcomparators;

import shareables.network.DTOs.unitselection.CourseThumbnailDTO;

import java.util.Comparator;

public class DegreeLevelCourseThumbnailComparator implements Comparator<CourseThumbnailDTO> {
    @Override
    public int compare(CourseThumbnailDTO firstDTO, CourseThumbnailDTO secondDTO) {
        return firstDTO.getDegreeLevel().getDegreeLevelSortingValue() -
                secondDTO.getDegreeLevel().getDegreeLevelSortingValue();
    }
}
