package server.network.clienthandling.logicutils.services;

import shareables.models.idgeneration.Identifiable;
import shareables.network.DTOs.RequestDTO;

import java.util.Comparator;

public class RequestDTOComparator implements Comparator<RequestDTO> {
    @Override
    public int compare(RequestDTO firstRequestDTO, RequestDTO secondRequestDTO) {
        long firstId = Long.parseLong(firstRequestDTO.getId());
        long secondId = Long.parseLong(secondRequestDTO.getId());
        return (int) (firstId - secondId);
    }
}
