package client.gui.menus.coursewares.homeworks.listview;

import client.gui.MainFrame;
import client.gui.menus.coursewares.homeworks.display.HomeworkDisplay;
import client.gui.menus.main.MainMenu;
import client.locallogic.menus.messaging.ThumbnailParser;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;

public class StudentHomeworksView extends HomeworksView {
    public StudentHomeworksView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String courseId) {
        super(mainFrame, mainMenu, offlineModeDTO, courseId);
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void connectListeners() {
        openButton.addActionListener(actionEvent -> {
            if (graphicalList.getSelectedIndex() == -1) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                        "noHomeworkHasBeenSelected");
                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners", getClass());
                return;
            }

            String selectedListItem = graphicalList.getSelectedValue();
            String selectedHomeworkId = ThumbnailParser.getIdFromThumbnailText(selectedListItem, " - ");

            MasterLogger.clientInfo(clientController.getId(), "Opened homework display",
                    "connectListeners", getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(new HomeworkDisplay(mainFrame, mainMenu, offlineModeDTO, courseId, selectedHomeworkId));
        });
    }
}
