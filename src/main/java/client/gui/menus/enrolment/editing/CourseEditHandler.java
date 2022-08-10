package client.gui.menus.enrolment.editing;

import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.CourseDTO;
import shareables.network.DTOs.OfflineModeDTO;
import shareables.utils.logging.MasterLogger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CourseEditHandler implements ActionListener {
    private MainFrame mainFrame;
    private MainMenu mainMenu;
    private Professor educationDeputy;
    private CourseDTO correspondingCourseDTO;
    private OfflineModeDTO offlineModeDTO;
    private int clientControllerId;

    public CourseEditHandler(MainFrame mainFrame, MainMenu mainMenu, Professor educationDeputy,
                             CourseDTO correspondingCourseDTO, OfflineModeDTO offlineModeDTO, int clientControllerId) {
        this.mainFrame = mainFrame;
        this.mainMenu = mainMenu;
        this.educationDeputy = educationDeputy;
        this.correspondingCourseDTO = correspondingCourseDTO;
        this.offlineModeDTO = offlineModeDTO;
        this.clientControllerId = clientControllerId;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String courseName = correspondingCourseDTO.getCourseName();
        MasterLogger.clientInfo(clientControllerId, "Opened course editor for " + courseName,
                "actionPerformed", getClass());
        mainFrame.setCurrentPanel(new CourseEditor(mainFrame, mainMenu, educationDeputy, correspondingCourseDTO,
                offlineModeDTO));
    }
}
