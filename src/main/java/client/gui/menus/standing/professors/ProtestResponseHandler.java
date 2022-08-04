package client.gui.menus.standing.professors;

import client.controller.ClientController;
import client.gui.MainFrame;
import shareables.network.DTOs.CourseScoreDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProtestResponseHandler implements ActionListener {
    private MainFrame mainFrame;
    private TemporaryStandingManager temporaryStandingManager;
    private CourseScoreDTO courseScoreDTO;
    private ClientController clientController;
    private ConfigFileIdentifier configIdentifier;

    public ProtestResponseHandler(MainFrame mainFrame, TemporaryStandingManager temporaryStandingManager,
                                  CourseScoreDTO courseScoreDTO, ClientController clientController,
                                  ConfigFileIdentifier configIdentifier) {
        this.mainFrame = mainFrame;
        this.temporaryStandingManager = temporaryStandingManager;
        this.courseScoreDTO = courseScoreDTO;
        this.clientController = clientController;
        this.configIdentifier = configIdentifier;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        MasterLogger.clientInfo(clientController.getId(), "Opened the protest response option panel",
                "actionPerformed", getClass());

        String studentProtest = courseScoreDTO.getStudentProtest();
        if (studentProtest == null) {
            String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS, "noProtestToRespondTo");
            JOptionPane.showMessageDialog(mainFrame, errorMessage);
            MasterLogger.clientError(clientController.getId(), errorMessage, "actionPerformed", getClass());
            return;
        }

        String protestResponseMessage = JOptionPane.showInputDialog(mainFrame,
                ConfigManager.getString(configIdentifier, "responsePromptM"));
        Response response = clientController.respondToProtest(courseScoreDTO.getCourseId(), courseScoreDTO.getStudentId(),
                protestResponseMessage);
        if (response.getResponseStatus() == ResponseStatus.OK) {
            MasterLogger.clientInfo(clientController.getId(), "Responded to student protest (ID: " +
                            courseScoreDTO.getStudentId() + ")", "actionPerformed", getClass());
        }

        temporaryStandingManager.updateTable();
    }
}