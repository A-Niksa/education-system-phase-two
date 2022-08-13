package client.locallogic.messaging;

import shareables.network.DTOs.messaging.ContactProfileDTO;

import java.util.ArrayList;

public class ContactManager {
    public ArrayList<String> getAllContactIds(ArrayList<ContactProfileDTO> contactProfileDTOs) {
        ArrayList<String> allContactIds = new ArrayList<>();
        contactProfileDTOs.forEach(e -> allContactIds.add(e.getContactId()));
        return allContactIds;
    }
}
