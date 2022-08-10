package client.gui.menus.messaging;

import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.network.DTOs.OfflineModeDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;

public class StudentMessengerView extends MessengerView {
    public StudentMessengerView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void connectListeners() {
        newConversationButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Opened new conversation creator",
                    "connectListeners", getClass());
            stopPanelLoop();
            // TODO
        });

        openConversationButton.addActionListener(actionEvent -> {
            if (graphicalList.getSelectedIndex() == -1) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                        "noConversationHasBeenSelected");
                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners", getClass());
                return;
            }

            String selectedListItem = graphicalList.getSelectedValue();
            String selectedContactId = selectedListItem.split(" - ")[0];
            System.out.println(selectedContactId); // TODO: to be removed
            // TODO
            stopPanelLoop();
            // TODO
            MasterLogger.clientInfo(clientController.getId(), "Opened conversation with user (ID: " +
                            selectedContactId + ")", "connectListeners", getClass());
        });
    }
}
