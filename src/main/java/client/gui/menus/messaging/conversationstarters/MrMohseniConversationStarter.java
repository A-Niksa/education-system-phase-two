package client.gui.menus.messaging.conversationstarters;

import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.network.DTOs.messaging.ContactProfileDTO;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.util.ArrayList;

public class MrMohseniConversationStarter extends ConversationStarter {
    private JButton startConversationWithFiltersButton;

    public MrMohseniConversationStarter(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        addFilteredConversationStartingButton();
        startPinging(offlineModeDTO.getId());
    }

    private void addFilteredConversationStartingButton() {
        initializeButton();
        alignButton();
        connectButtonListener();
    }

    private void initializeButton() {
        startConversationWithFiltersButton = new JButton(ConfigManager.getString(configIdentifier,
                "startConversationWithFiltersButtonM"));
    }

    private void alignButton() {
        startConversationWithFiltersButton.setBounds(
                ConfigManager.getInt(configIdentifier, "startConversationWithFiltersButtonX"),
                ConfigManager.getInt(configIdentifier, "startConversationWithFiltersButtonY"),
                ConfigManager.getInt(configIdentifier, "startConversationWithFiltersButtonW"),
                ConfigManager.getInt(configIdentifier, "startConversationWithFiltersButtonH"));
        add(startConversationWithFiltersButton);
    }

    private void connectButtonListener() {
        startConversationWithFiltersButton.addActionListener(actionEvent -> {
            ArrayList<String> allContactIds = contactManager.getAllContactIds(contactProfileDTOs);
            if (allContactIds.isEmpty()) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,  "youHaveNoContacts");

                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners", getClass());
                return;
            }

            MasterLogger.clientInfo(clientController.getId(), "Opted to start a conversation using filters",
                    "connectListeners", getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(new FilteredConversationStartingRoom(mainFrame, mainMenu, offlineModeDTO,
                    allContactIds));
        });
    }

    @Override
    protected void updateContactProfileDTOs() {
        Response response = clientController.getMrMohseniContactProfileDTOs(offlineModeDTO.getId());
        if (response == null) return;
        contactProfileDTOs = (ArrayList<ContactProfileDTO>) response.get("contactProfileDTOs");
    }
}
