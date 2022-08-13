package client.gui.menus.services.requests.management;

import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.DTOs.academicrequests.RequestDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DroppingOutManager extends RequestManager {
    public DroppingOutManager(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        initializeColumns();
        drawInteractivePanel();
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void initializeColumns() {
        columns = new String[2];
        columns[0] = ConfigManager.getString(configIdentifier, "studentIdCol");
        columns[1] = ConfigManager.getString(configIdentifier, "studentNameCol");
    }

    // TODO: unrelated but having 3 digit ids for students

    @Override
    protected void setRequestsList() {
        Response response = clientController.getDepartmentDroppingOutRequestDTOs(offlineModeDTO.getDepartmentId());
        if (response == null) return;
        requestDTOs = (ArrayList<RequestDTO>) response.get("requestDTOs");
    }

    @Override
    protected void setRequestsTableData() {
        data = new String[requestDTOs.size()][];
        RequestDTO requestDTO;
        for (int i = 0; i < requestDTOs.size(); i++) {
            requestDTO = requestDTOs.get(i);
            data[i] = new String[]{requestDTO.getRequestingStudentId(),
                    requestDTO.getRequestingStudentName()};
        }
    }

    @Override
    protected void setAcceptListener(int index) {
        JButton approveButton = acceptButtonsList.get(index);
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                RequestDTO requestDTO = requestDTOs.get(index);
                Response response = clientController.acceptDroppingOutRequest(requestDTO.getRequestingStudentId(),
                        requestDTO.getId());
                if (response == null) return;
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    MasterLogger.clientInfo(clientController.getId(), "Dropping out request (ID: " +
                            requestDTO.getRequestingStudentId() + ") has been accepted by the department deputy (ID: " +
                            offlineModeDTO.getId() + ")", "setApproveListener", getClass());
                    updatePanel();
                }
            }
        });
    }

    // TODO: unrelated but resolving the sequential id bug (100 -> 302)

    @Override
    protected void setDeclineListener(int index) {
        JButton declineButton = declineButtonsList.get(index);
        declineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                RequestDTO requestDTO = requestDTOs.get(index);
                Response response = clientController.declineDroppingOutRequest(requestDTO.getId());
                if (response == null) return;
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    MasterLogger.clientInfo(clientController.getId(), "Dropping request (ID: " +
                            requestDTO.getRequestingStudentId() + ") has been declined by the department deputy (ID: " +
                            offlineModeDTO.getId() + ")", "setDeclineListener", getClass());
                    updatePanel();
                }
            }
        });
    }
}
