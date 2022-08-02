package client.gui.menus.enrolment.editing;

import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.addition.CourseAdder;
import client.gui.menus.enrolment.management.CoursesListManager;
import client.gui.menus.main.MainMenu;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.CourseDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CoursesListEditor extends PanelTemplate {
    private Professor professor;
    private ArrayList<CourseDTO> departmentCourseDTOs;
    private JButton goBackButton;
    private JButton addCourseButton;
    private JTable coursesTable;
    private String[] columns;
    private String[][] data;
    private ArrayList<JButton> editButtonsList;

    public CoursesListEditor(MainFrame mainFrame, MainMenu mainMenu, Professor professor) {
        super(mainFrame, mainMenu);
        this.professor = professor;
        configIdentifier = ConfigFileIdentifier.GUI_LIST_EDITOR;
        initializeDepartmentCourseDTOs();
        initializeColumns();
        setTableData();
        drawPanel();
    }

    private void initializeDepartmentCourseDTOs() {
        Response response = clientController.getDepartmentCourseDTOs(professor.getDepartmentId());
        departmentCourseDTOs = (ArrayList<CourseDTO>) response.get("courseDTOs");
    }

    private void initializeColumns() {
        columns = new String[6];
        columns[0] = ConfigManager.getString(configIdentifier, "courseIdCol");
        columns[1] = ConfigManager.getString(configIdentifier, "nameCol");
        columns[2] = ConfigManager.getString(configIdentifier, "examDateAndTimeCol");
        columns[3] = ConfigManager.getString(configIdentifier, "nameOfProfessorsCol");
        columns[4] = ConfigManager.getString(configIdentifier, "numberOfCreditsCol");
        columns[5] = ConfigManager.getString(configIdentifier, "degreeLevelCol");
    }

    private void setTableData() {
        data = new String[departmentCourseDTOs.size()][];
        CourseDTO courseDTO;
        Professor teachingProfessor;
        for (int i = 0; i < departmentCourseDTOs.size(); i++) {
            courseDTO = departmentCourseDTOs.get(i);
            data[i] = new String[]{courseDTO.getId(),
                    courseDTO.getCourseName(),
                    courseDTO.fetchFormattedExamDate(),
                    courseDTO.getCompressedNamesOfProfessors(),
                    courseDTO.getNumberOfCredits() + "",
                    courseDTO.getDegreeLevel().toString()};
        }
    }

    @Override
    protected void initializeComponents() {
        editButtonsList = new ArrayList<>();

        goBackButton = new JButton(ConfigManager.getString(configIdentifier, "goBackButtonM"));
        addCourseButton = new JButton(ConfigManager.getString(configIdentifier, "addCourseButtonM"));

        coursesTable = new JTable(data, columns);

        for (int i = 0; i < departmentCourseDTOs.size(); i++) {
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
        addCourseButton.setBounds(ConfigManager.getInt(configIdentifier, "addCourseButtonX"),
                ConfigManager.getInt(configIdentifier, "addCourseButtonY"),
                ConfigManager.getInt(configIdentifier, "addCourseButtonW"),
                ConfigManager.getInt(configIdentifier, "addCourseButtonH"));
        add(addCourseButton);

        coursesTable.setRowHeight(ConfigManager.getInt(configIdentifier, "tableRowHeight"));
        JScrollPane scrollPane = new JScrollPane(coursesTable);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);

        int currentX = ConfigManager.getInt(configIdentifier, "editButtonStartingX");
        int currentY = ConfigManager.getInt(configIdentifier, "editButtonStartingY");
        int incrementOfY = ConfigManager.getInt(configIdentifier, "incY");
        int buttonWidth = ConfigManager.getInt(configIdentifier, "editButtonW");
        int buttonHeight = ConfigManager.getInt(configIdentifier, "editButtonH");
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
                MasterLogger.clientInfo(clientController.getId(), "Went back to courses list view",
                        "connectListeners", getClass());
                mainFrame.setCurrentPanel(new CoursesListManager(mainFrame, mainMenu, professor));
            }
        });

        addCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the course addition section",
                        "connectListeners", getClass());
                mainFrame.setCurrentPanel(new CourseAdder(mainFrame, mainMenu, professor));
            }
        });

        JButton editButton;
        CourseDTO editableCourseDTO;
        for (int i = 0; i < editButtonsList.size(); i++) {
            editButton = editButtonsList.get(i);
            editableCourseDTO = departmentCourseDTOs.get(i);
            editButton.addActionListener(new CourseEditHandler(mainFrame, mainMenu, professor, editableCourseDTO,
                    clientController.getId()));
        }
    }
}
