package client.gui.menus.coursewares.educationalmaterials.listview;

import client.gui.MainFrame;
import client.gui.menus.coursewares.educationalmaterials.addition.MaterialAdder;
import client.gui.menus.coursewares.educationalmaterials.addition.MaterialEditor;
import client.gui.menus.main.MainMenu;
import client.locallogic.menus.messaging.ThumbnailParser;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;

public class ProfessorMaterialsView extends MaterialsView {
    public ProfessorMaterialsView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String courseId) {
        super(mainFrame, mainMenu, offlineModeDTO, courseId);
        addMaterialManagementFeatures();
        startPinging(offlineModeDTO.getId());
    }

    private void addMaterialManagementFeatures() {
        add(removeAllButton);
        add(removeButton);
        add(addButton);
        add(editButton);
        repaint();
        connectManagementListeners();
    }

    private void connectManagementListeners() {
        addButton.addActionListener(actionEvent -> {
            String materialTitle = JOptionPane.showInputDialog(mainFrame, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                    "materialTitleAcquisitionPrompt"));
            if (materialTitle == null || materialTitle.equals("")) materialTitle = "No Title";

            MasterLogger.clientInfo(clientController.getId(), "Opened material adder",
                    "connectListeners", getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(new MaterialAdder(mainFrame, mainMenu, offlineModeDTO, courseId, materialTitle));
        });

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

        removeButton.addActionListener(actionEvent -> {
            if (graphicalList.getSelectedIndex() == -1) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                        "noMaterialHasBeenSelected");
                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners", getClass());
                return;
            }

            String selectedListItem = graphicalList.getSelectedValue();
            String selectedMaterialId = ThumbnailParser.getIdFromThumbnailText(selectedListItem, " - ");

            Response response = clientController.removeCourseEducationalMaterial(courseId, selectedMaterialId);
            if (response == null) return;

            if (response.getResponseStatus() == ResponseStatus.OK) {
                MasterLogger.clientInfo(clientController.getId(), "Removed material (ID: " + selectedMaterialId + ")",
                        "connectListeners", getClass());
                updatePanel();
            }
        });

        removeAllButton.addActionListener(actionEvent -> {
            Response response = clientController.removeAllCourseEducationalMaterials(courseId);
            if (response == null) return;

            if (response.getResponseStatus() == ResponseStatus.OK) {
                MasterLogger.clientInfo(clientController.getId(), "Removed course materials",
                        "connectListeners", getClass());
                updatePanel();
            }
        });
    }
}
