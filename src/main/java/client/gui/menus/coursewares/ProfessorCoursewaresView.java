package client.gui.menus.coursewares;

import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;

public class ProfessorCoursewaresView extends CoursewaresView {
    public ProfessorCoursewaresView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void connectListeners() {

    }

    @Override
    protected void updateCourseThumbnailDTOs() {

    }
}
