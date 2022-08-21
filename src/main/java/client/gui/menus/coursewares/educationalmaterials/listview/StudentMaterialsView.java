package client.gui.menus.coursewares.educationalmaterials.listview;

import client.gui.MainFrame;
import client.gui.menus.coursewares.educationalmaterials.addition.MaterialEditor;
import client.gui.menus.main.MainMenu;
import client.locallogic.menus.messaging.ThumbnailParser;
import shareables.models.pojos.users.UserIdentifier;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;

public class StudentMaterialsView extends MaterialsView {
    private boolean isTeachingAssistant;

    public StudentMaterialsView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String courseId) {
        super(mainFrame, mainMenu, offlineModeDTO, courseId);
        initializeTeachingAssistanceStatus();
        addEditingFeatureIfNecessary();
        startPinging(offlineModeDTO.getId());
    }

    private void initializeTeachingAssistanceStatus() {
        if (offlineModeDTO.getUserIdentifier() == UserIdentifier.PROFESSOR) {
            isTeachingAssistant = false;
        } else {
            Response response = clientController.getTeachingAssistanceStatus(courseId, offlineModeDTO.getId());
            if (response == null) return;
            isTeachingAssistant = (boolean) response.get("isTeachingAssistant");
        }
    }

    private void addEditingFeatureIfNecessary() {
        add(editButton);
        repaint();
        connectEditingListener();
    }

    private void connectEditingListener() {
        editButton.addActionListener(actionEvent -> {
            if (graphicalList.getSelectedIndex() == -1) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                        "noMaterialHasBeenSelected");
                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners", getClass());
                return;
            }

            String selectedListItem = graphicalList.getSelectedValue();
            String selectedMaterialId = ThumbnailParser.getIdFromThumbnailText(selectedListItem, " - ");

            MasterLogger.clientInfo(clientController.getId(), "Opened material editor",
                    "connectListeners", getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(new MaterialEditor(mainFrame, mainMenu, offlineModeDTO, courseId, selectedMaterialId));
        });
    }
}
