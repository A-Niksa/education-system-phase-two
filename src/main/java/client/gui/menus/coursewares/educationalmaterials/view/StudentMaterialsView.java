package client.gui.menus.coursewares.educationalmaterials.view;

import client.gui.MainFrame;
import client.gui.menus.coursewares.educationalmaterials.materialdisplay.StudentMaterialDisplay;
import client.gui.menus.main.MainMenu;
import client.locallogic.menus.messaging.ThumbnailIdParser;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;

public class StudentMaterialsView extends MaterialsView {
    public StudentMaterialsView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String courseId) {
        super(mainFrame, mainMenu, offlineModeDTO, courseId);
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void connectOpeningListener() {
        openButton.addActionListener(actionEvent -> {
            if (graphicalList.getSelectedIndex() == -1) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                        "noMaterialHasBeenSelected");
                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners", getClass());
                return;
            }

            String selectedListItem = graphicalList.getSelectedValue();
            String selectedMaterialId = ThumbnailIdParser.getIdFromThumbnailText(selectedListItem, " - ");

            MasterLogger.clientInfo(clientController.getId(), "Opened material display",
                    "connectListeners", getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(new StudentMaterialDisplay(mainFrame, mainMenu, offlineModeDTO, selectedMaterialId));
        });
    }
}
