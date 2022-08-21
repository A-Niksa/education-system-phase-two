package client.gui.menus.coursewares.homeworks.display;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;

public class HomeworkManager extends DynamicPanelTemplate {
    private String courseId;
    private String homeworkId;

    public HomeworkManager(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String courseId,
                           String homeworkId) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.courseId = courseId;
        this.homeworkId = homeworkId;
    }

    @Override
    protected void updatePanel() {

    }

    @Override
    protected void initializeComponents() {

    }

    @Override
    protected void alignComponents() {

    }

    @Override
    protected void connectListeners() {

    }
}
