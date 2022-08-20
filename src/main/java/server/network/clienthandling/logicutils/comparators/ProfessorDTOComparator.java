package server.network.clienthandling.logicutils.comparators;

import shareables.network.DTOs.generalmodels.ProfessorDTO;

import java.util.Comparator;

public class ProfessorDTOComparator implements Comparator<ProfessorDTO> {
    @Override
    public int compare(ProfessorDTO firstProfessorDTO, ProfessorDTO secondProfessorDTO) {
        long firstId = Long.parseLong(firstProfessorDTO.getId());
        long secondId = Long.parseLong(secondProfessorDTO.getId());
        return (int) (firstId - secondId);
    }
}
