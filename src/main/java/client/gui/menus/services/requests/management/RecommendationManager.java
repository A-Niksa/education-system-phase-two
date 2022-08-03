package client.gui.menus.services.requests.management;

import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.RequestDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RecommendationManager extends RequestManager {
    public RecommendationManager(MainFrame mainFrame, MainMenu mainMenu, Professor professor) {
        super(mainFrame, mainMenu, professor);
        initializeColumns();
        drawInteractivePanel();
    }

    @Override
    protected void initializeColumns() {
        columns = new String[3];
        columns[0] = ConfigManager.getString(configIdentifier, "studentIdCol");
        columns[1] = ConfigManager.getString(configIdentifier, "studentNameCol");
        columns[2] = ConfigManager.getString(configIdentifier, "studentGPACol");
    }

    @Override
    protected void setRequestsList() {
        Response response = clientController.getProfessorRecommendationRequestDTOs(professor.getId());
        requestDTOs = (ArrayList<RequestDTO>) response.get("requestDTOs");
    }

    @Override
    protected void setRequestsTableData() {
        data = new String[requestDTOs.size()][];
        RequestDTO requestDTO;
        for (int i = 0; i < requestDTOs.size(); i++) {
            requestDTO = requestDTOs.get(i);
            data[i] = new String[]{requestDTO.getRequestingStudentId(),
                    requestDTO.getRequestingStudentName(),
                    requestDTO.getRequestingStudentGPAString()};
        }
    }

    @Override
    protected void setApproveListener(int index) {
        JButton approveButton = approveButtonsList.get(index);
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                RequestDTO requestDTO = requestDTOs.get(index);
                Response response = clientController.acceptRecommendationRequest(requestDTO.getId());
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    MasterLogger.clientInfo(clientController.getId(), "Recommendation request (ID: " +
                            requestDTO.getRequestingStudentId() + ") has been accepted by the receiving professor (ID: " +
                            professor.getId() + ")", "setApproveListener", getClass());
                }
            }
        });
    }

    @Override
    protected void setDeclineListener(int index) {
        JButton declineButton = declineButtonsList.get(index);
        declineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                RequestDTO requestDTO = requestDTOs.get(index);
                Response response = clientController.declineRecommendationRequest(requestDTO.getId());
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    MasterLogger.clientInfo(clientController.getId(), "Recommendation request (ID: " +
                            requestDTO.getRequestingStudentId() + ") has been declined by the receiving professor (ID: " +
                            professor.getId() + ")", "setApproveListener", getClass());
                }
            }
        });
    }
}
