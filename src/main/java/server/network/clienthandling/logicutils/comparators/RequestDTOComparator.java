package server.network.clienthandling.logicutils.comparators;

import shareables.network.DTOs.academicrequests.RequestDTO;

import java.util.Comparator;

public class RequestDTOComparator implements Comparator<RequestDTO> {
    @Override
    public int compare(RequestDTO firstRequestDTO, RequestDTO secondRequestDTO) {
        long firstId = Long.parseLong(firstRequestDTO.getId());
        long secondId = Long.parseLong(secondRequestDTO.getId());
        return (int) (firstId - secondId);
    }
}
