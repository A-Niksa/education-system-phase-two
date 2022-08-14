package client.gui.menus.services.requests.management;

import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import client.locallogic.menus.profile.DepartmentGetter;
import shareables.models.pojos.abstractions.DepartmentName;
import shareables.network.DTOs.academicrequests.MinorRequestDTO;
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

public class MinorManager extends RequestManager {
    public MinorManager(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        initializeColumns();
        drawInteractivePanel();
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void initializeColumns() {
        columns = new String[5];
        columns[0] = ConfigManager.getString(configIdentifier, "studentIdCol");
        columns[1] = ConfigManager.getString(configIdentifier, "studentNameCol");
        columns[2] = ConfigManager.getString(configIdentifier, "studentGPACol");
        columns[3] = ConfigManager.getString(configIdentifier, "originDepartmentCol");
        columns[4] = ConfigManager.getString(configIdentifier, "targetDepartmentCol");
    }

    @Override
    protected void setRequestsList() {
        Response response = clientController.getProfessorMinorRequestDTOs(offlineModeDTO.getId());
        if (response == null) return;
        requestDTOs = (ArrayList<RequestDTO>) response.get("requestDTOs");
    }

    @Override
    protected void setRequestsTableData() {
        data = new String[requestDTOs.size()][];
        MinorRequestDTO minorRequestDTO;
        for (int i = 0; i < requestDTOs.size(); i++) {
            minorRequestDTO = (MinorRequestDTO) requestDTOs.get(i);
            data[i] = new String[]{minorRequestDTO.getRequestingStudentId(),
                    minorRequestDTO.getRequestingStudentName(),
                    minorRequestDTO.getRequestingStudentGPAString(),
                    DepartmentGetter.getDepartmentNameById(minorRequestDTO.getOriginDepartmentId()).toString(),
                    DepartmentGetter.getDepartmentNameById(minorRequestDTO.getTargetDepartmentId()).toString()};
        }
    }

    @Override
    protected void setAcceptListener(int index) {
        JButton approveButton = acceptButtonsList.get(index);
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MinorRequestDTO minorRequestDTO = (MinorRequestDTO) requestDTOs.get(index);
                Response response = clientController.acceptMinorRequest(minorRequestDTO.getId(), offlineModeDTO.getDepartmentId());
                if (response == null) return;
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    DepartmentName departmentName = DepartmentGetter.getDepartmentNameById(offlineModeDTO.getDepartmentId());
                    String departmentSide = (String) response.get("departmentSide"); // "origin department" or "target department"

                    MasterLogger.clientInfo(clientController.getId(), "Minor request (requesting student ID: " +
                            minorRequestDTO.getRequestingStudentId() + ") has been partially accepted by the " +
                            departmentSide + " (Department: " + departmentName + ")", "setAcceptListener",
                            getClass());
                    updatePanel();
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
                MinorRequestDTO minorRequestDTO = (MinorRequestDTO) requestDTOs.get(index);
                Response response = clientController.declineMinorRequest(minorRequestDTO.getId(), offlineModeDTO.getDepartmentId());
                if (response == null) return;
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    DepartmentName departmentName = DepartmentGetter.getDepartmentNameById(offlineModeDTO.getDepartmentId());
                    String departmentSide = (String) response.get("departmentSide"); // "origin department" or "target department"

                    MasterLogger.clientInfo(clientController.getId(), "Minor request (requesting student ID: " +
                                    minorRequestDTO.getRequestingStudentId() + ") has been partially declined by the " +
                                    departmentSide + " (Department: " + departmentName + ")", "setDeclineListener",
                            getClass());
                    updatePanel();
                }
            }
        });
    }
}
