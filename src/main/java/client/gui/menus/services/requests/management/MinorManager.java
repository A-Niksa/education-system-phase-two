package client.gui.menus.services.requests.management;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MinorManager extends RequestManager {
    public MinorManager(MainFrame mainFrame, MainMenu mainMenu, Professor operatingProfessor) {
        super(mainFrame, mainMenu, operatingProfessor);
        columns = new String[]{"Student ID", "Name and Surname", "GPA", "Origin Department",
                "Target Department"};
        drawInteractivePanel();
    }

    @Override
    protected void initializeColumns() {

    }

    @Override
    protected void setRequestsList() {
        MinorsDB.ensureStatusesAreUpToDate();
        requestsList = MinorsDB.getMinorRequestsOfDeputy(operatingProfessor);
    }

    @Override
    protected void setRequestsTableData() {
        data = new String[requestsList.size()][];
        MinorRequest request;
        Student requestingStudent;
        for (int i = 0; i < requestsList.size(); i++) {
            request = (MinorRequest) requestsList.get(i);
            requestingStudent = request.getRequestingStudent();
            data[i] = new String[]{requestingStudent.getStudentID(),
                    requestingStudent.getFirstName() + " " + requestingStudent.getLastName(),
                    requestingStudent.getTotalGPAString(),
                    request.getOriginDepartmentName(),
                    request.getTargetDepartmentName()};
        }
    }

    @Override
    protected void setApproveListener(int index) {
        JButton approveButton = approveButtonsList.get(index);
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MinorRequest request = (MinorRequest) requestsList.get(index);
                Student requestingStudent = request.getRequestingStudent();

                boolean deputyIsFromOriginDepartment = request.deputyIsFromOriginDepartment(operatingProfessor);

                if (deputyIsFromOriginDepartment) {
                    request.setOriginDepartmentResponded(true);
                    request.setOriginDepartmentAccepted(true);
                    MasterLogger.info("minor request (requesting student ID: " +
                            requestingStudent.getStudentID() + ") has been partially accepted by the origin department "
                            + "(Department: " + operatingProfessor.getDepartmentName() + ")", getClass());
                } else { // deputy is from the target department by default
                    request.setTargetDepartmentResponded(true);
                    request.setTargetDepartmentAccepted(true);
                    MasterLogger.info("minor request (requesting student ID: " +
                            requestingStudent.getStudentID() + ") has been partially accepted by the target department "
                            + "(Department: " + operatingProfessor.getDepartmentName() + ")", getClass());
                }

                request.updateInDatabase();
            }
        });
    }

    @Override
    protected void setDeclineListener(int index) {
        JButton declineButton = declineButtonsList.get(index);
        declineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MinorRequest request = (MinorRequest) requestsList.get(index);
                Student requestingStudent = request.getRequestingStudent();

                boolean deputyIsFromOriginDepartment = request.deputyIsFromOriginDepartment(operatingProfessor);

                if (deputyIsFromOriginDepartment) {
                    request.setOriginDepartmentResponded(true);
                    request.setOriginDepartmentAccepted(false);
                    MasterLogger.info("minor request (requesting student ID: " +
                            requestingStudent.getStudentID() + ") has been partially declined by the origin department "
                            + "(Department: " + operatingProfessor.getDepartmentName() + ")", getClass());
                } else { // deputy is from the target department by default
                    request.setTargetDepartmentResponded(true);
                    request.setTargetDepartmentAccepted(false);
                    MasterLogger.info("minor request (requesting student ID: " +
                            requestingStudent.getStudentID() + ") has been partially declined by the target department "
                            + "(Department: " + operatingProfessor.getDepartmentName() + ")", getClass());
                }

                request.updateInDatabase();
            }
        });
    }
}
