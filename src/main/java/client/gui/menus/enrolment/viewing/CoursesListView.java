package client.gui.menus.enrolment.viewing;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import client.gui.utils.EnumArrayUtils;
import client.locallogic.menus.enrolment.CourseFilteringTool;
import client.locallogic.menus.enrolment.FilterKey;
import client.locallogic.menus.profile.DepartmentGetter;
import shareables.models.pojos.abstractions.DepartmentName;
import shareables.network.DTOs.generalmodels.CourseDTO;
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

public class CoursesListView extends DynamicPanelTemplate {
    private ArrayList<CourseDTO> activeCourseDTOs;
    private DefaultTableModel tableModel;
    private JTable coursesTable;
    private JScrollPane scrollPane;
    private String[] columns;
    private String[] degreeLevels;
    private String[][] data;
    private JTextField courseIdFilterField;
    private JButton filterOnCourseId;
    private JTextField numberOfCreditsFilterField;
    private JButton filterOnNumberOfCredits;
    private JComboBox<String> degreeLevelFilterCombo;
    private JButton filterOnDegreeLevel;
    private JButton resetButton;

    public CoursesListView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        configIdentifier = ConfigFileIdentifier.GUI_LIST_VIEW;
        updateActiveCourseDTOs();
        initializeColumns();
        degreeLevels = EnumArrayUtils.initializeDegreeLevels();
        setTableData(activeCourseDTOs);
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void updatePanel() {
        updateActiveCourseDTOs();
        setTableData(activeCourseDTOs);
        tableModel.setDataVector(data, columns);
    }

    private void updateActiveCourseDTOs() {
        Response response = clientController.getActiveCourseDTOs();
        if (response == null) return;
        activeCourseDTOs = (ArrayList<CourseDTO>) response.get("courseDTOs");
    }

    private void initializeColumns() {
        columns = new String[7];
        columns[0] = ConfigManager.getString(configIdentifier, "courseIdCol");
        columns[1] = ConfigManager.getString(configIdentifier, "nameCol");
        columns[2] = ConfigManager.getString(configIdentifier, "departmentCol");
        columns[3] = ConfigManager.getString(configIdentifier, "examDateAndTimeCol");
        columns[4] = ConfigManager.getString(configIdentifier, "nameOfProfessorsCol");
        columns[5] = ConfigManager.getString(configIdentifier, "numberOfCreditsCol");
        columns[6] = ConfigManager.getString(configIdentifier, "degreeLevelCol");
    }

    private void setTableData(ArrayList<CourseDTO> courseDTOs) {
        data = new String[courseDTOs.size()][];
        CourseDTO courseDTO;
        DepartmentName departmentName;
        for (int i = 0; i < courseDTOs.size(); i++) {
            courseDTO = courseDTOs.get(i);
            departmentName = DepartmentGetter.getDepartmentNameById(courseDTO.getDepartmentId());
            data[i] = new String[]{courseDTO.getId(),
                    courseDTO.getCourseName(),
                    departmentName.toString(),
                    courseDTO.fetchFormattedExamDate(),
                    courseDTO.getCompressedNamesOfProfessors(),
                    courseDTO.getNumberOfCredits() + "",
                    courseDTO.getDegreeLevel().toString()};
        }
    }

    @Override
    protected void initializeComponents() {
        tableModel = new DefaultTableModel(data, columns);
        coursesTable = new JTable(tableModel);
        courseIdFilterField = new JTextField(ConfigManager.getString(configIdentifier, "courseIdFilterFieldM"));
        filterOnCourseId = new JButton(ConfigManager.getString(configIdentifier, "filterButtonM"));
        numberOfCreditsFilterField = new JTextField(ConfigManager.getString(configIdentifier,
                "numberOfCreditsFilterFieldM"));
        filterOnNumberOfCredits = new JButton(ConfigManager.getString(configIdentifier, "filterButtonM"));
        degreeLevelFilterCombo = new JComboBox<>(degreeLevels);
        filterOnDegreeLevel = new JButton(ConfigManager.getString(configIdentifier, "filterButtonM"));
        resetButton = new JButton(ConfigManager.getString(configIdentifier, "resetButtonM"));
    }

    @Override
    protected void alignComponents() {
        coursesTable.setRowHeight(ConfigManager.getInt(configIdentifier, "tableRowH"));
        scrollPane = new JScrollPane(coursesTable);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);

        int currentX = ConfigManager.getInt(configIdentifier, "startingCurrentX");
        int spacingOfX = ConfigManager.getInt(configIdentifier, "spacingX");
        int incrementOfX = ConfigManager.getInt(configIdentifier, "incX");
        courseIdFilterField.setBounds(currentX, ConfigManager.getInt(configIdentifier, "courseIdFilterFieldY"),
                ConfigManager.getInt(configIdentifier, "courseIdFilterFieldW"),
                ConfigManager.getInt(configIdentifier, "courseIdFilterFieldH"));
        add(courseIdFilterField);
        filterOnCourseId.setBounds(currentX + spacingOfX, ConfigManager.getInt(configIdentifier, "filterOnCourseIdY"),
                ConfigManager.getInt(configIdentifier, "filterOnCourseIdW"),
                ConfigManager.getInt(configIdentifier, "filterOnCourseIdH"));
        add(filterOnCourseId);
        currentX += incrementOfX;

        numberOfCreditsFilterField.setBounds(currentX,
                ConfigManager.getInt(configIdentifier, "numberOfCreditsFilterFieldY"),
                ConfigManager.getInt(configIdentifier, "numberOfCreditsFilterFieldW"),
                ConfigManager.getInt(configIdentifier, "numberOfCreditsFilterFieldH"));
        add(numberOfCreditsFilterField);
        filterOnNumberOfCredits.setBounds(currentX + spacingOfX,
                ConfigManager.getInt(configIdentifier, "filterOnNumberOfCreditsY"),
                ConfigManager.getInt(configIdentifier, "filterOnNumberOfCreditsW"),
                ConfigManager.getInt(configIdentifier, "filterOnNumberOfCreditsH"));
        add(filterOnNumberOfCredits);
        currentX += incrementOfX;

        degreeLevelFilterCombo.setBounds(currentX,
                ConfigManager.getInt(configIdentifier, "degreeLevelFilterComboY"),
                ConfigManager.getInt(configIdentifier, "degreeLevelFilterComboW"),
                ConfigManager.getInt(configIdentifier, "degreeLevelFilterComboH"));
        add(degreeLevelFilterCombo);
        filterOnDegreeLevel.setBounds(currentX + spacingOfX,
                ConfigManager.getInt(configIdentifier, "filterOnDegreeLevelY"),
                ConfigManager.getInt(configIdentifier, "filterOnDegreeLevelW"),
                ConfigManager.getInt(configIdentifier, "filterOnDegreeLevelH"));
        add(filterOnDegreeLevel);
        currentX += ConfigManager.getInt(configIdentifier, "largeIncX");

        resetButton.setBounds(currentX, ConfigManager.getInt(configIdentifier, "resetButtonY"),
                ConfigManager.getInt(configIdentifier, "resetButtonW"),
                ConfigManager.getInt(configIdentifier, "resetButtonH"));
        add(resetButton);
    }

    @Override
    protected void connectListeners() {
        filterOnCourseId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String desiredCourseId = courseIdFilterField.getText();
                MasterLogger.clientInfo(clientController.getId(), "Filtered courses based on course id: " +
                        desiredCourseId, "connectListeners", getClass());
                setTableData(CourseFilteringTool.getFilteredCourseDTOs(FilterKey.COURSE_ID, desiredCourseId,
                        activeCourseDTOs));
                DefaultTableModel tableModel = new DefaultTableModel(data, columns);
                coursesTable.setModel(tableModel);
            }
        });

        filterOnNumberOfCredits.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String desiredNumberOfCredits = numberOfCreditsFilterField.getText();
                MasterLogger.clientInfo(clientController.getId(), "Filtered courses based on number of credits: " +
                        desiredNumberOfCredits, "connectListeners", getClass());
                setTableData(CourseFilteringTool.getFilteredCourseDTOs(FilterKey.NUMBER_OF_CREDITS, desiredNumberOfCredits,
                        activeCourseDTOs));
                DefaultTableModel tableModel = new DefaultTableModel(data, columns);
                coursesTable.setModel(tableModel);
            }
        });

        filterOnDegreeLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String desiredDegreeLevel = (String) degreeLevelFilterCombo.getSelectedItem();
                MasterLogger.clientInfo(clientController.getId(), "Filtered courses based on course level: "
                        + desiredDegreeLevel, "connectListeners", getClass());
                setTableData(CourseFilteringTool.getFilteredCourseDTOs(FilterKey.COURSE_LEVEL, desiredDegreeLevel,
                        activeCourseDTOs));
                DefaultTableModel tableModel = new DefaultTableModel(data, columns);
                coursesTable.setModel(tableModel);
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Filters were reset",
                        "connectListeners", getClass());
                setTableData(activeCourseDTOs);
                DefaultTableModel tableModel = new DefaultTableModel(data, columns);
                coursesTable.setModel(tableModel);
            }
        });
    }
}
