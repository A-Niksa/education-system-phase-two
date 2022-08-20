package server.network.clienthandling.logicutils.comparators;

import shareables.network.DTOs.coursewares.MaterialThumbnailDTO;

import java.util.Comparator;

public class MaterialThumbnailDTOComparator implements Comparator<MaterialThumbnailDTO> {
    @Override
    public int compare(MaterialThumbnailDTO firstDTO, MaterialThumbnailDTO secondDTO) {
        return firstDTO.getUploadDate().compareTo(secondDTO.getUploadDate());
    }
}
