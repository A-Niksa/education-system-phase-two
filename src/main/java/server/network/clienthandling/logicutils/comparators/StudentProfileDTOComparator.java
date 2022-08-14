package server.network.clienthandling.logicutils.comparators;

import shareables.network.DTOs.messaging.ContactProfileDTO;

import java.util.Comparator;

/**
 * to be used only for students
 */
public class StudentProfileDTOComparator implements Comparator<ContactProfileDTO> {
    @Override
    public int compare(ContactProfileDTO firstDTO, ContactProfileDTO secondDTO) {
        int firstYearOfEntry = firstDTO.getYearOfEntry();
        int secondYearOfEntry = secondDTO.getYearOfEntry();

        if (firstYearOfEntry < secondYearOfEntry) {
            return -1;
        } else if (firstYearOfEntry == secondYearOfEntry) {
            return 0;
        } else {
            return 1;
        }
    }
}
