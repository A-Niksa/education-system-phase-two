package server.network.clienthandling.logicutils.comparators.coursewarecomparators;

import shareables.network.DTOs.coursewares.CalendarEventDTO;

import java.util.Comparator;

public class CalendarEventDTOComparator implements Comparator<CalendarEventDTO> {
    @Override
    public int compare(CalendarEventDTO firstDTO, CalendarEventDTO secondDTO) {
        return firstDTO.getEventDate().compareTo(secondDTO.getEventDate());
    }
}
