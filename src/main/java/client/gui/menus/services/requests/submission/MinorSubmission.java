package client.gui.menus.services.requests.submission;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import client.gui.utils.EnumArrayUtils;
import client.gui.utils.ErrorUtils;
import client.locallogic.menus.profile.DepartmentGetter;
import shareables.network.DTOs.academicrequests.MinorRequestDTO;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MinorSubmission extends DynamicPanelTemplate {
    private ArrayList<MinorRequestDTO> studentMinorRequestDTOs;
    private DefaultTableModel tableModel;
    private JTable requestsTable;
    private JScrollPane scrollPane;
    private String[] columns;
    private String[][] data;
    private String[] departmentNameStrings;
    private JComboBox<String> departmentSelector;
    private JButton submitRequest;

    public MinorSubmission(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        configIdentifier = ConfigFileIdentifier.GUI_MINOR_SUBMISSION;
        departmentNameStrings = EnumArrayUtils.initializeDepartmentNameStrings();
        updateMinorRequestDTOs();
        initializeColumns();
        setTableData();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    private void initializeColumns() {
        columns = new String[3];
        columns[0] = ConfigManager.getString(configIdentifier, "originDepartmentM");
        columns[1] = ConfigManager.getString(configIdentifier, "targetDepartmentM");
        columns[2] = ConfigManager.getString(configIdentifier, "statusM");
    }

    private void updateMinorRequestDTOs() {
        Response response = clientController.getStudentMinorRequestDTOs(offlineModeDTO.getId());
        if (response == null) return;
        studentMinorRequestDTOs = (ArrayList<MinorRequestDTO>) response.get("requestDTOs");
    }

    private void setTableData() {
        data = new String[studentMinorRequestDTOs.size()][];
        MinorRequestDTO minorRequestDTO;
        for (int i = 0; i < studentMinorRequestDTOs.size(); i++) {
            minorRequestDTO = studentMinorRequestDTOs.get(i);

            data[i] = new String[]{DepartmentGetter.getDepartmentNameById(minorRequestDTO.getOriginDepartmentId()).toString(),
                    DepartmentGetter.getDepartmentNameById(minorRequestDTO.getTargetDepartmentId()).toString(),
                    minorRequestDTO.getAcademicRequestStatus().toString()};
        }
    }

    private void updateTable() {
        updateMinorRequestDTOs();
        setTableData();
        tableModel.setDataVector(data, columns);
    }

    @Override
    protected void initializeComponents() {
        tableModel = new DefaultTableModel(data, columns);
        requestsTable = new JTable(tableModel);
        departmentSelector = new JComboBox<>(departmentNameStrings);
        submitRequest = new JButton(ConfigManager.getString(configIdentifier, "submitRequestM"));
    }

    @Override
    protected void alignComponents() {
        requestsTable.setRowHeight(25);
        scrollPane = new JScrollPane(requestsTable);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);

        departmentSelector.setBounds(ConfigManager.getInt(configIdentifier, "departmentSelectorX"),
                ConfigManager.getInt(configIdentifier, "departmentSelectorY"),
                ConfigManager.getInt(configIdentifier, "departmentSelectorW"),
                ConfigManager.getInt(configIdentifier, "departmentSelectorH"));
        add(departmentSelector);

        submitRequest.setBounds(ConfigManager.getInt(configIdentifier, "submitRequestX"),
                ConfigManager.getInt(configIdentifier, "submitRequestY"),
                ConfigManager.getInt(configIdentifier, "submitRequestW"),
                ConfigManager.getInt(configIdentifier, "submitRequestH"));
        add(submitRequest);
    }

    @Override
    protected void connectListeners() {
        submitRequest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String targetDepartmentNameString = (String) departmentSelector.getSelectedItem();
                Response response = clientController.askForMinor(offlineModeDTO.getId(), offlineModeDTO.getDepartmentId(),
                        targetDepartmentNameString);
                if (response == null) return;
                if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                    MasterLogger.clientError(clientController.getId(), response.getErrorMessage(), "connectListeners",
                            getClass());
                } else {
                    MasterLogger.clientInfo(clientController.getId(), "Submitted a request to minor at " +
                            targetDepartmentNameString, "connectListeners", getClass());
                    updateTable();
                }
            }
        });
    }

    @Override
    protected void updatePanel() {
        updateTable();
    }
}
