package client.gui.menus.coursewares.coursewaresview;

import client.gui.MainFrame;
import client.gui.menus.coursewares.coursemenu.CourseMenu;
import client.gui.menus.main.MainMenu;
import client.locallogic.menus.messaging.ThumbnailParser;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.DTOs.unitselection.CourseThumbnailDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.util.ArrayList;

public class ProfessorCoursewaresView extends CoursewaresView {
    public ProfessorCoursewaresView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void connectListeners() {
        openButton.addActionListener(actionEvent -> {
            if (graphicalList.getSelectedIndex() == -1) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                        "noCourseHasBeenSelected");
                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners", getClass());
                return;
            }

            String selectedListItem = graphicalList.getSelectedValue();
            String selectedCourseId = ThumbnailParser.getIdFromThumbnailText(selectedListItem, " - ");

            MasterLogger.clientInfo(clientController.getId(), "Opened course menu", "connectListeners",
                    getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(new CourseMenu(mainFrame, mainMenu, offlineModeDTO, selectedCourseId));
        });
    }

    @Override
    protected void updateCourseThumbnailDTOs() {
        Response response = clientController.getProfessorCoursewareThumbnailDTOs(offlineModeDTO.getId());
        if (response == null) return;
        courseThumbnailDTOs = (ArrayList<CourseThumbnailDTO>) response.get("courseThumbnailDTOs");
    }
}
