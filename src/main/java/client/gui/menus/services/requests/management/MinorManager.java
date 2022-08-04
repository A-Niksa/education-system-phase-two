package client.gui.menus.services.requests.management;

import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import client.locallogic.profile.DepartmentGetter;
import shareables.models.pojos.abstractions.DepartmentName;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.MinorRequestDTO;
import shareables.network.DTOs.RequestDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MinorManager extends RequestManager {
    public MinorManager(MainFrame mainFrame, MainMenu mainMenu, Professor professor) {
        super(mainFrame, mainMenu, professor);
        initializeColumns();
        drawInteractivePanel();
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
        Response response = clientController.getProfessorMinorRequestDTOs(professor.getId());
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
                    DepartmentGetter.getDepartmentName(minorRequestDTO.getOriginDepartmentId()).toString(),
                    DepartmentGetter.getDepartmentName(minorRequestDTO.getTargetDepartmentId()).toString()};
        }
    }

    @Override
    protected void setAcceptListener(int index) {
        JButton approveButton = acceptButtonsList.get(index);
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MinorRequestDTO minorRequestDTO = (MinorRequestDTO) requestDTOs.get(index);
                Response response = clientController.acceptMinorRequest(minorRequestDTO.getId(), professor.getDepartmentId());
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    DepartmentName departmentName = DepartmentGetter.getDepartmentName(professor.getDepartmentId());
                    String departmentSide = (String) response.get("departmentSide"); // "origin department" or "target department"

                    MasterLogger.clientInfo(clientController.getId(), "Minor request (requesting student ID: " +
                            minorRequestDTO.getRequestingStudentId() + ") has been partially accepted by the " +
                            departmentSide + " (Department: " + departmentName + ")", "setAcceptListener",
                            getClass());
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
                Response response = clientController.declineMinorRequest(minorRequestDTO.getId(), professor.getDepartmentId());
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    DepartmentName departmentName = DepartmentGetter.getDepartmentName(professor.getDepartmentId());
                    String departmentSide = (String) response.get("departmentSide"); // "origin department" or "target department"

                    MasterLogger.clientInfo(clientController.getId(), "Minor request (requesting student ID: " +
                                    minorRequestDTO.getRequestingStudentId() + ") has been partially declined by the " +
                                    departmentSide + " (Department: " + departmentName + ")", "setDeclineListener",
                            getClass());
                }
            }
        });
    }
}