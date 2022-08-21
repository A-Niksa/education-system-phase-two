package client.gui.menus.messaging.messengerviews;

import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import client.gui.menus.messaging.conversationroom.ConversationRoom;
import client.gui.menus.messaging.conversationstarters.ProfessorConversationStarter;
import client.locallogic.menus.messaging.ThumbnailParser;
import shareables.network.DTOs.messaging.ConversationDTO;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;

public class ProfessorMessengerView extends MessengerView {
    public ProfessorMessengerView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, boolean isOnline) {
        super(mainFrame, mainMenu, offlineModeDTO, isOnline);
        startPingingIfOnline(offlineModeDTO.getId(), this);
    }

    @Override
    protected void connectListeners() {
        newConversationButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Opened new conversation creator",
                    "connectListeners", getClass());
            facilitateChangingPanel(this);
            mainFrame.setCurrentPanel(new ProfessorConversationStarter(mainFrame, mainMenu, offlineModeDTO));
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
            String selectedContactId = ThumbnailParser.getIdFromThumbnailText(selectedListItem, " - ");

            if (!isOnline) {
                getConversationDTOInOfflineMode(selectedContactId);
                return;
            }

            Response response = clientController.getContactConversationDTO(offlineModeDTO.getId(), selectedContactId);
            if (response == null) return;
            if (response.getResponseStatus() == ResponseStatus.OK) {
                ConversationDTO conversationDTO = (ConversationDTO) response.get("conversationDTO");

                facilitateChangingPanel(this);
                mainFrame.setCurrentPanel(new ConversationRoom(mainFrame, mainMenu, offlineModeDTO,
                        conversationDTO, selectedContactId, isOnline));
                MasterLogger.clientInfo(clientController.getId(), "Opened conversation room with user (ID: " +
                        selectedContactId + ")", "connectListeners", getClass());
            }
        });
    }
}
