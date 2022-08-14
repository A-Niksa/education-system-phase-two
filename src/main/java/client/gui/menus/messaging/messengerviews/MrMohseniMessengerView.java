package client.gui.menus.messaging.messengerviews;

import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import client.gui.menus.messaging.conversationroom.ConversationRoom;
import client.gui.menus.messaging.conversationstarters.MrMohseniConversationStarter;
import client.locallogic.menus.messaging.ThumbnailIdParser;
import shareables.network.DTOs.messaging.ConversationDTO;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;

public class MrMohseniMessengerView extends MessengerView {
    public MrMohseniMessengerView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void connectListeners() {
        newConversationButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Opened new conversation creator",
                    "connectListeners", getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(new MrMohseniConversationStarter(mainFrame, mainMenu, offlineModeDTO));
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
            String selectedContactId = ThumbnailIdParser.getIdFromThumbnailText(selectedListItem, " - ");
            Response response = clientController.getContactConversationDTO(offlineModeDTO.getId(), selectedContactId);
            if (response == null) return;
            if (response.getResponseStatus() == ResponseStatus.OK) {
                ConversationDTO conversationDTO = (ConversationDTO) response.get("conversationDTO");

                stopPanelLoop();
                mainFrame.setCurrentPanel(new ConversationRoom(mainFrame, mainMenu, offlineModeDTO,
                        conversationDTO, selectedContactId));
                MasterLogger.clientInfo(clientController.getId(), "Opened conversation room with user (ID: " +
                        selectedContactId + ")", "connectListeners", getClass());
            }
        });
    }
}