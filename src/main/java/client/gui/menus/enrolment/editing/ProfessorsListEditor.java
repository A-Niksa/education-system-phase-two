package client.gui.menus.enrolment.editing;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.addition.ProfessorAdderOfDean;
import client.gui.menus.enrolment.management.ProfessorsListManager;
import client.gui.menus.main.MainMenu;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.DTOs.generalmodels.ProfessorDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ProfessorsListEditor extends DynamicPanelTemplate {
    private JButton goBackButton;
    private JButton addProfessorButton;
    private DefaultTableModel tableModel;
    private JTable professorsTable;
    private String[] columns;
    private String[][] data;
    private ArrayList<ProfessorDTO> departmentProfessorDTOs;
    private ArrayList<JButton> editButtonsList;

    public ProfessorsListEditor(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        configIdentifier = ConfigFileIdentifier.GUI_LIST_EDITOR;
        updateDepartmentProfessorDTOs();
        initializeColumns();
        setTableData();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    private void updateDepartmentProfessorDTOs() {
        Response response = clientController.getDepartmentProfessorDTOs(offlineModeDTO.getDepartmentId());
        if (response == null) return;
        departmentProfessorDTOs = (ArrayList<ProfessorDTO>) response.get("professorDTOs");
    }

    private void initializeColumns() {
        columns = new String[6];
        columns[0] = ConfigManager.getString(configIdentifier, "professorIdCol");
        columns[1] = ConfigManager.getString(configIdentifier, "nameAndSurnameCol");
        columns[2] = ConfigManager.getString(configIdentifier, "academicLevelCol");
        columns[3] = ConfigManager.getString(configIdentifier, "academicRoleCol");
        columns[4] = ConfigManager.getString(configIdentifier, "officeNumberCol");
        columns[5] = ConfigManager.getString(configIdentifier, "emailAddressCol");
    }

    private void setTableData() {
        data = new String[departmentProfessorDTOs.size()][];
        ProfessorDTO departmentProfessorDTO;
        for (int i = 0; i < departmentProfessorDTOs.size(); i++) {
            departmentProfessorDTO = departmentProfessorDTOs.get(i);
            data[i] = new String[]{departmentProfessorDTO.getId(),
                    departmentProfessorDTO.getName(),
                    departmentProfessorDTO.getAcademicLevel().toString(),
                    departmentProfessorDTO.getAcademicRole().toString(),
                    departmentProfessorDTO.getOfficeNumber(),
                    departmentProfessorDTO.getEmailAddress()};
        }
    }

    @Override
    protected void initializeComponents() {
        editButtonsList = new ArrayList<>();

        goBackButton = new JButton(ConfigManager.getString(configIdentifier, "goBackButtonM"));
        addProfessorButton = new JButton(ConfigManager.getString(configIdentifier, "addProfessorButtonM"));

        tableModel = new DefaultTableModel(data, columns);
        professorsTable = new JTable(tableModel);

        initializeEditButtons();
    }

    private void initializeEditButtons() {
        editButtonsList.clear();
        for (int i = 0; i < departmentProfessorDTOs.size(); i++) {
            JButton button = new JButton(ConfigManager.getString(configIdentifier, "editButtonM"));
            editButtonsList.add(button);
        }
    }

    @Override
    protected void alignComponents() {
        goBackButton.setBounds(ConfigManager.getInt(configIdentifier, "goBackButtonX"),
                ConfigManager.getInt(configIdentifier, "goBackButtonY"),
                ConfigManager.getInt(configIdentifier, "goBackButtonW"),
                ConfigManager.getInt(configIdentifier, "goBackButtonH"));
        add(goBackButton);
        addProfessorButton.setBounds(ConfigManager.getInt(configIdentifier, "addProfessorButtonX"),
                ConfigManager.getInt(configIdentifier, "addProfessorButtonY"),
                ConfigManager.getInt(configIdentifier, "addProfessorButtonW"),
                ConfigManager.getInt(configIdentifier, "addProfessorButtonH"));
        add(addProfessorButton);

        professorsTable.setRowHeight(ConfigManager.getInt(configIdentifier, "tableRowHeight"));
        JScrollPane scrollPane = new JScrollPane(professorsTable);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);

        alignEditButtons();
    }

    private void alignEditButtons() {
        int currentX = ConfigManager.getInt(configIdentifier, "editButtonStartingX");;
        int currentY = ConfigManager.getInt(configIdentifier, "editButtonStartingY");;
        int buttonWidth = ConfigManager.getInt(configIdentifier, "editButtonW");
        int buttonHeight = ConfigManager.getInt(configIdentifier, "editButtonH");
        int incrementOfY = ConfigManager.getInt(configIdentifier, "incY");
        for (JButton editButton : editButtonsList) {
            editButton.setBounds(currentX, currentY, buttonWidth, buttonHeight);
            add(editButton);
            currentY += buttonHeight + incrementOfY;
        }
    }

    @Override
    protected void connectListeners() {
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Went back to professors list listview",
                        "connectListeners", getClass());
                stopPanelLoop();
                mainFrame.setCurrentPanel(new ProfessorsListManager(mainFrame, mainMenu, offlineModeDTO));
            }
        });

        addProfessorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the professor sessionaddition section",
                        "connectListeners", getClass());
                stopPanelLoop();
                mainFrame.setCurrentPanel(new ProfessorAdderOfDean(mainFrame, mainMenu, offlineModeDTO,
                        clientController.getId()));
            }
        });

        connectEditButtonListeners();
    }

    private void connectEditButtonListeners() {
        JButton editButton;
        ProfessorDTO editableProfessorDTO;
        for (int i = 0; i < departmentProfessorDTOs.size(); i++) {
            editButton = editButtonsList.get(i);
            editableProfessorDTO = departmentProfessorDTOs.get(i);
            editButton.addActionListener(new ProfessorEditHandler(mainFrame, mainMenu, this,
                    editableProfessorDTO, offlineModeDTO));
        }
    }

    @Override
    protected void updatePanel() {
        updateDepartmentProfessorDTOs();
        setTableData();
        tableModel.setDataVector(data, columns);
        editButtonsList.forEach(this::remove);
        initializeEditButtons();
        alignEditButtons();
        connectEditButtonListeners();
        this.repaint();
    }


}