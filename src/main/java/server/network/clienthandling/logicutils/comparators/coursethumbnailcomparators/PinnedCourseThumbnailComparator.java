package server.network.clienthandling.logicutils.comparators.coursethumbnailcomparators;

import shareables.network.DTOs.unitselection.CourseThumbnailDTO;

import java.util.Comparator;

/**
 * sorts pinned course thumbnail DTOs in a way that recommended courses come before favorite courses
 */
public class PinnedCourseThumbnailComparator implements Comparator<CourseThumbnailDTO> {
    @Override
    public int compare(CourseThumbnailDTO firstDTO, CourseThumbnailDTO secondDTO) {
        boolean isFirstDTORecommended = firstDTO.isRecommended();
        boolean isFirstDTOPinnedToFavorites = firstDTO.isPinnedToFavorites();
        boolean isSecondDTORecommended = secondDTO.isRecommended();
        boolean isSecondDTOPinnedToFavorites = secondDTO.isPinnedToFavorites();

        int numericalValueOfFirstDTO = 0;
        int numericalValueOfSecondDTO = 0;

        if (isFirstDTORecommended) {
            numericalValueOfFirstDTO += 1;
        }
        if (isFirstDTOPinnedToFavorites) {
            numericalValueOfFirstDTO += 2;
        }

        if (isSecondDTORecommended) {
            numericalValueOfSecondDTO += 1;
        }
        if (isSecondDTOPinnedToFavorites) {
            numericalValueOfSecondDTO += 2;
        }

        return numericalValueOfFirstDTO - numericalValueOfSecondDTO;
    }
}