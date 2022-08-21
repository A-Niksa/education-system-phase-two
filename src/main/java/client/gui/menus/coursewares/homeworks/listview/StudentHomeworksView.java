package client.gui.menus.coursewares.homeworks.listview;

import client.gui.MainFrame;
import client.gui.menus.coursewares.homeworks.display.HomeworkDisplay;
import client.gui.menus.coursewares.homeworks.display.HomeworkManager;
import client.gui.menus.main.MainMenu;
import client.gui.utils.ErrorUtils;
import client.locallogic.menus.messaging.ThumbnailParser;
import shareables.models.pojos.users.UserIdentifier;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;

public class StudentHomeworksView extends HomeworksView {
    private boolean isTeachingAssistant;

    public StudentHomeworksView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String courseId) {
        super(mainFrame, mainMenu, offlineModeDTO, courseId);
        initializeTeachingAssistanceStatus();
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

            if (!isTeachingAssistant) {
                Response response = clientController.checkDeadlineConstraints(courseId, selectedHomeworkId);
                if (response == null) return;

                if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                    MasterLogger.clientError(clientController.getId(), response.getErrorMessage(),
                            "connectListeners", getClass());
                    return;
                }
            }

            MasterLogger.clientInfo(clientController.getId(), "Opened homeworks display",
                    "connectListeners", getClass());

            stopPanelLoop();
            if (isTeachingAssistant) {
                mainFrame.setCurrentPanel(new HomeworkManager(mainFrame, mainMenu, offlineModeDTO, courseId,
                        selectedHomeworkId));
            } else {
                mainFrame.setCurrentPanel(new HomeworkDisplay(mainFrame, mainMenu, offlineModeDTO, courseId,
                        selectedHomeworkId));
            }
        });
    }
}
