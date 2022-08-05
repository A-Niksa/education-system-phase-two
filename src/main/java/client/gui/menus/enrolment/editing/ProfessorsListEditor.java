package client.gui.menus.enrolment.editing;

import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.addition.ProfessorAdderOfDean;
import client.gui.menus.enrolment.management.ProfessorsListManager;
import client.gui.menus.main.MainMenu;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.ProfessorDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ProfessorsListEditor extends PanelTemplate {
    private Professor professor;
    private JButton goBackButton;
    private JButton addProfessorButton;
    private JTable professorsTable;
    private String[] columns;
    private String[][] data;
    private ArrayList<ProfessorDTO> departmentProfessorDTOs;
    private ArrayList<JButton> editButtonsList;

    public ProfessorsListEditor(MainFrame mainFrame, MainMenu mainMenu, Professor professor) {
        super(mainFrame, mainMenu);
        this.professor = professor;
        updateDepartmentProfessorDTOs();
        initializeColumns();
        setTableData();
        drawPanel();
    }

    private void updateDepartmentProfessorDTOs() {
        // duplicating request to avoid receiving lossy data:
        clientController.getDepartmentProfessorDTOs(professor.getDepartmentId());
        Response response = clientController.getDepartmentProfessorDTOs(professor.getDepartmentId());
        departmentProfessorDTOs = (ArrayList<ProfessorDTO>) response.get("professorDTOs");
    }

    private void initializeColumns() {
        columns = new String[5];
        columns[0] = ConfigManager.getString(configIdentifier, "nameAndSurnameCol");
        columns[1] = ConfigManager.getString(configIdentifier, "academicLevelCol");
        columns[2] = ConfigManager.getString(configIdentifier, "academicRoleCol");
        columns[3] = ConfigManager.getString(configIdentifier, "officeNumberCol");
        columns[4] = ConfigManager.getString(configIdentifier, "emailAddressCol");
    }

    private void setTableData() {
        data = new String[departmentProfessorDTOs.size()][];
        ProfessorDTO departmentProfessorDTO;
        for (int i = 0; i < departmentProfessorDTOs.size(); i++) {
            departmentProfessorDTO = departmentProfessorDTOs.get(i);
            data[i] = new String[]{departmentProfessorDTO.getName(),
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

        professorsTable = new JTable(data, columns);

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

        professorsTable.setRowHeight(ConfigManager.getInt(configIdentifier, "tableRowH"));
        JScrollPane scrollPane = new JScrollPane(professorsTable);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);

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
                MasterLogger.clientInfo(clientController.getId(), "Went back to professors list view",
                        "connectListeners", getClass());
                mainFrame.setCurrentPanel(new ProfessorsListManager(mainFrame, mainMenu, professor));
            }
        });

        addProfessorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the professor addition section",
                        "connectListeners", getClass());
                mainFrame.setCurrentPanel(new ProfessorAdderOfDean(mainFrame, mainMenu, professor,
                        clientController.getId()));
            }
        });

        JButton editButton;
        ProfessorDTO editableProfessorDTO;
        for (int i = 0; i < editButtonsList.size(); i++) {
            editButton = editButtonsList.get(i);
            editableProfessorDTO = departmentProfessorDTOs.get(i);
            editButton.addActionListener(new ProfessorEditHandler(mainFrame, mainMenu, professor, editableProfessorDTO));
        }
    }
}