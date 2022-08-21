package server.network.clienthandling.logicutils.comparators.coursewarecomparators;

import shareables.network.DTOs.coursewares.HomeworkThumbnailDTO;

import java.time.ZoneOffset;
import java.util.Comparator;

public class HomeworkThumbnailDTOComparator implements Comparator<HomeworkThumbnailDTO> {
    @Override
    public int compare(HomeworkThumbnailDTO firstDTO, HomeworkThumbnailDTO secondDTO) {
        long firstStartingDateSeconds = firstDTO.getStartingDate().toEpochSecond(ZoneOffset.UTC);
        long firstSharpDeadlineSeconds = firstDTO.getStartingDate().toEpochSecond(ZoneOffset.UTC);
        long secondStartingDateSeconds = secondDTO.getStartingDate().toEpochSecond(ZoneOffset.UTC);
        long secondSharpDeadlineSeconds = secondDTO.getStartingDate().toEpochSecond(ZoneOffset.UTC);

        return (int) ((firstStartingDateSeconds + firstSharpDeadlineSeconds) -
                        (secondStartingDateSeconds + secondSharpDeadlineSeconds));
    }
}
