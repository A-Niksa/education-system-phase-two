package client.gui.menus.standing.deputies;

import client.controller.ClientController;
import client.gui.MainFrame;
import client.gui.utils.ErrorUtils;
import client.gui.utils.StatsViewHelper;
import client.locallogic.profile.DepartmentGetter;
import shareables.models.pojos.abstractions.DepartmentName;
import shareables.network.DTOs.standing.CourseStatsDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatsViewHandler implements ActionListener {
    private MainFrame mainFrame;
    private String courseName;
    private String departmentNameString;
    private ClientController clientController;
    private ConfigFileIdentifier configIdentifier;

    public StatsViewHandler(MainFrame mainFrame, String courseName, String departmentNameString,
                            ClientController clientController, ConfigFileIdentifier configIdentifier) {
        this.mainFrame = mainFrame;
        this.courseName = courseName;
        this.departmentNameString = departmentNameString;
        this.clientController = clientController;
        this.configIdentifier = configIdentifier;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        DepartmentName departmentName = DepartmentGetter.getDepartmentNameByString(departmentNameString);
        Response response = clientController.getCourseStatsDTO(courseName, departmentName);
        if (response == null) return;
        if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
            MasterLogger.clientInfo(clientController.getId(), response.getErrorMessage(), "actionPerformed",
                    getClass());
        } else {
            CourseStatsDTO courseStatsDTO = (CourseStatsDTO) response.get("courseStatsDTO");
            String dialogMessage = StatsViewHelper.getStatsDialogMessage(configIdentifier, courseStatsDTO);
            JOptionPane.showMessageDialog(mainFrame, dialogMessage);
            MasterLogger.clientInfo(clientController.getId(), "Opened stats of " + courseName,
                    "actionPerformed", getClass());
        }
    }
}
