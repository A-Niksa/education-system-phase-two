package client.gui.menus.messaging.conversationstarters;

import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.network.DTOs.messaging.ContactProfileDTO;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;

import java.util.ArrayList;

public class ProfessorConversationStarter extends ConversationStarter {
    public ProfessorConversationStarter(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void updateContactProfileDTOs() {
        Response response = clientController.getProfessorContactProfileDTOs(offlineModeDTO.getId());
        if (response == null) return;
        contactProfileDTOs = (ArrayList<ContactProfileDTO>) response.get("contactProfileDTOs");
    }
}
