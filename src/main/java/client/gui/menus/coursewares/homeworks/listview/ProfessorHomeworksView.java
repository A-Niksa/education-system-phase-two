package client.gui.menus.coursewares.homeworks.listview;

import client.gui.MainFrame;
import client.gui.menus.coursewares.educationalmaterials.addition.MaterialAdder;
import client.gui.menus.coursewares.homeworks.addition.HomeworkAdder;
import client.gui.menus.coursewares.homeworks.display.HomeworkDisplay;
import client.gui.menus.coursewares.homeworks.display.HomeworkManager;
import client.gui.menus.main.MainMenu;
import client.locallogic.menus.messaging.ThumbnailParser;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;

public class ProfessorHomeworksView extends HomeworksView {
    public ProfessorHomeworksView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String courseId) {
        super(mainFrame, mainMenu, offlineModeDTO, courseId);
        addAdditionFeature();
        startPinging(offlineModeDTO.getId());
    }

    private void addAdditionFeature() {
        add(addButton);
        repaint();
        connectAdditionListener();
    }

    private void connectAdditionListener() {
        addButton.addActionListener(actionEvent -> {
            String homeworkTitle = JOptionPane.showInputDialog(mainFrame, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                    "homeworkTitleAcquisitionPrompt"));
            if (homeworkTitle == null || homeworkTitle.equals("")) homeworkTitle = "No Title";

            MasterLogger.clientInfo(clientController.getId(), "Opened homeworks adder",
                    "connectListeners", getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(new HomeworkAdder(mainFrame, mainMenu, offlineModeDTO, courseId, homeworkTitle));
        });
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

            MasterLogger.clientInfo(clientController.getId(), "Opened homeworks management tool",
                    "connectListeners", getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(new HomeworkManager(mainFrame, mainMenu, offlineModeDTO, courseId, selectedHomeworkId));
        });
    }
}
