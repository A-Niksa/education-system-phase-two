package client.gui.menus.standing.students;


import client.controller.ClientController;
import client.gui.MainFrame;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.CourseScoreDTO;
import shareables.network.DTOs.OfflineMessengerDTO;
import shareables.network.DTOs.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProtestSubmissionHandler implements ActionListener {
    private MainFrame mainFrame;
    private TemporaryStandingView temporaryStandingView;
    private CourseScoreDTO courseScoreDTO;
    private ClientController clientController;
    private ConfigFileIdentifier configIdentifier;
    private OfflineModeDTO offlineModeDTO;

    public ProtestSubmissionHandler(MainFrame mainFrame, TemporaryStandingView temporaryStandingView,
                                    CourseScoreDTO courseScoreDTO, ClientController clientController,
                                    ConfigFileIdentifier configIdentifier, OfflineModeDTO offlineModeDTO) {
        this.mainFrame = mainFrame;
        this.temporaryStandingView = temporaryStandingView;
        this.courseScoreDTO = courseScoreDTO;
        this.clientController = clientController;
        this.configIdentifier = configIdentifier;
        this.offlineModeDTO = offlineModeDTO;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        MasterLogger.clientInfo(clientController.getId(), "Opened the protest submission option panel",
                "actionPerformed", getClass());
        String protest = JOptionPane.showInputDialog(mainFrame,
                ConfigManager.getString(configIdentifier, "enterProtestM"));

        Response response = clientController.submitProtest(courseScoreDTO.getCourseId(), offlineModeDTO.getId(), protest);
        if (response == null) return;
        if (response.getResponseStatus() == ResponseStatus.OK) {
            MasterLogger.clientInfo(clientController.getId(), "Student protested to their score of " +
                    courseScoreDTO.getCourseName(), "actionPerformed", getClass());
        }

        temporaryStandingView.updateTable();
    }
}
