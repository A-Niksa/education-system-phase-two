package server.network.clienthandling.logicutils.messaging.contactfetching;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.searching.StudentSearchingUtils;
import shareables.network.DTOs.messaging.ContactProfileDTO;

import java.util.ArrayList;
import java.util.List;

import static server.network.clienthandling.logicutils.messaging.contactfetching.ContactFetchingUtils.getNotifiedContactProfileDTOs;

public class MrMohseniContactFetchingUtils {
    public static List<ContactProfileDTO> getMrMohseniContactProfileDTOs(DatabaseManager databaseManager, String mrMohseniId) {
        List<ContactProfileDTO> contactProfileDTOs = new ArrayList<>();
        contactProfileDTOs.addAll(StudentSearchingUtils.getAllStudentContactProfileDTOs(databaseManager));
        contactProfileDTOs.addAll(getNotifiedContactProfileDTOs(databaseManager, mrMohseniId));
        return contactProfileDTOs;
    }
}
