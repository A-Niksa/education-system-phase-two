package server.network.clienthandling.logicutils.comparators.coursewarecomparators;

import shareables.network.DTOs.coursewares.SubmissionThumbnailDTO;

import java.util.Comparator;

public class SubmissionThumbnailDTOComparator implements Comparator<SubmissionThumbnailDTO> {
    @Override
    public int compare(SubmissionThumbnailDTO firstDTO, SubmissionThumbnailDTO secondDTO) {
        return firstDTO.getUploadedAt().compareTo(secondDTO.getUploadedAt());
    }
}
