package client.gui.menus.coursewares.coursemenu;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.coursewares.coursewaresview.ProfessorCoursewaresView;
import client.gui.menus.coursewares.coursewaresview.StudentCoursewaresView;
import client.gui.menus.coursewares.educationalmaterials.listview.ProfessorMaterialsView;
import client.gui.menus.coursewares.educationalmaterials.listview.StudentMaterialsView;
import client.gui.menus.coursewares.homeworks.listview.ProfessorHomeworksView;
import client.gui.menus.coursewares.homeworks.listview.StudentHomeworksView;
import client.gui.menus.main.MainMenu;
import client.gui.utils.ErrorUtils;
import client.locallogic.general.DatePickerConfigurationTool;
import client.locallogic.menus.coursewares.CalendarUtils;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import shareables.models.pojos.users.UserIdentifier;
import shareables.network.DTOs.coursewares.CalendarEventDTO;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;
import shareables.utils.timing.formatting.DateLabelFormatter;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

public class CourseMenu extends DynamicPanelTemplate {
    private String courseId;
    private UtilDateModel dateModel;
    private Properties properties;
    private JDatePanelImpl datePanel;
    private JDatePickerImpl datePicker;
    private LocalDateTime selectedLocalDateTime;
    private JButton seeButton;
    private ArrayList<CalendarEventDTO> calendarEventDTOs;
    private String[] calendarEventTexts;
    private DefaultListModel<String> listModel;
    private JList<String> graphicalList;
    private JScrollPane scrollPane;
    private JButton homeworksButton;
    private JButton educationalMaterialsButton;
    private JButton addStudentButton;
    private JButton addTAButton;
    private JButton goBackButton;

    public CourseMenu(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String courseId) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.courseId = courseId;
        configIdentifier = ConfigFileIdentifier.GUI_COURSE_MENU;
        updateCalendarEventDTOs();
        updateCalendarEventTexts();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    private void updateCalendarEventTexts() {
        calendarEventTexts = new String[calendarEventDTOs.size()];
        for (int i = 0; i < calendarEventDTOs.size(); i++) {
            calendarEventTexts[i] = calendarEventDTOs.get(i).toShortenedString();
        }
    }

    private void updateCalendarEventDTOs() {
        Response response = clientController.getCalendarEventDTOs(courseId, selectedLocalDateTime);
        if (response == null) return;
        calendarEventDTOs = (ArrayList<CalendarEventDTO>) response.get("calendarEventDTOs");
    }

    @Override
    protected void updatePanel() {
        if (selectedLocalDateTime == null) return;

        updateCalendarEventDTOs();
        String[] previousCalendarThumbnailTexts = Arrays.copyOf(calendarEventTexts, calendarEventTexts.length);
        updateCalendarEventTexts();
        Arrays.stream(previousCalendarThumbnailTexts)
                .filter(e -> !arrayContains(calendarEventTexts, e))
                .forEach(e -> listModel.removeElement(e));
        Arrays.stream(calendarEventTexts)
                .filter(e -> !arrayContains(previousCalendarThumbnailTexts, e))
                .forEach(e -> listModel.add(0, e));
    }

    private boolean arrayContains(String[] array, String targetElement) {
        return Arrays.stream(array).anyMatch(e -> e.equals(targetElement));
    }

    @Override
    protected void initializeComponents() {
        seeButton = new JButton(ConfigManager.getString(configIdentifier, "seeButtonM"));

        dateModel = new UtilDateModel();
        properties = new Properties();
        properties.put("text.today", ConfigManager.getString(configIdentifier, "propertiesTextTodayM"));
        properties.put("text.month", ConfigManager.getString(configIdentifier, "propertiesTextMonthM"));
        properties.put("text.year", ConfigManager.getString(configIdentifier, "propertiesTextYearM"));
        datePanel = new JDatePanelImpl(dateModel, properties);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        listModel = new DefaultListModel<>();
        Arrays.stream(calendarEventTexts).forEach(e -> listModel.addElement(e));
        graphicalList = new JList<>(listModel);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(graphicalList);

        homeworksButton = new JButton(ConfigManager.getString(configIdentifier, "homeworksButtonM"));
        educationalMaterialsButton = new JButton(ConfigManager.getString(configIdentifier,
                "educationalMaterialsButtonM"));

        addStudentButton = new JButton(ConfigManager.getString(configIdentifier, "addStudentButtonM"));
        addTAButton = new JButton(ConfigManager.getString(configIdentifier, "addTAButtonM"));

        goBackButton = new JButton(ConfigManager.getString(configIdentifier, "goBackButtonM"));
    }

    @Override
    protected void alignComponents() {
        seeButton.setBounds(ConfigManager.getInt(configIdentifier, "seeButtonX"),
                ConfigManager.getInt(configIdentifier, "seeButtonY"),
                ConfigManager.getInt(configIdentifier, "seeButtonW"),
                ConfigManager.getInt(configIdentifier, "seeButtonH"));
        add(seeButton);

        DatePickerConfigurationTool.configureInitialDateOfDatePicker(dateModel);
        datePicker.setBounds(ConfigManager.getInt(configIdentifier, "datePickerX"),
                ConfigManager.getInt(configIdentifier, "datePickerY"),
                ConfigManager.getInt(configIdentifier, "datePickerW"),
                ConfigManager.getInt(configIdentifier, "datePickerH"));
        add(datePicker);

        graphicalList.setLayoutOrientation(JList.VERTICAL);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);

        homeworksButton.setBounds(ConfigManager.getInt(configIdentifier, "homeworksButtonX"),
                ConfigManager.getInt(configIdentifier, "homeworksButtonY"),
                ConfigManager.getInt(configIdentifier, "homeworksButtonW"),
                ConfigManager.getInt(configIdentifier, "homeworksButtonH"));
        add(homeworksButton);
        educationalMaterialsButton.setBounds(ConfigManager.getInt(configIdentifier, "educationalMaterialsButtonX"),
                ConfigManager.getInt(configIdentifier, "educationalMaterialsButtonY"),
                ConfigManager.getInt(configIdentifier, "educationalMaterialsButtonW"),
                ConfigManager.getInt(configIdentifier, "educationalMaterialsButtonH"));
        add(educationalMaterialsButton);

        goBackButton.setBounds(ConfigManager.getInt(configIdentifier, "goBackButtonX"),
                ConfigManager.getInt(configIdentifier, "goBackButtonY"),
                ConfigManager.getInt(configIdentifier, "goBackButtonW"),
                ConfigManager.getInt(configIdentifier, "goBackButtonH"));
        add(goBackButton);

        addStudentButton.setBounds(ConfigManager.getInt(configIdentifier, "addStudentButtonX"),
                ConfigManager.getInt(configIdentifier, "addStudentButtonY"),
                ConfigManager.getInt(configIdentifier, "addStudentButtonW"),
                ConfigManager.getInt(configIdentifier, "addStudentButtonH"));
        addTAButton.setBounds(ConfigManager.getInt(configIdentifier, "addTAButtonX"),
                ConfigManager.getInt(configIdentifier, "addTAButtonY"),
                ConfigManager.getInt(configIdentifier, "addTAButtonW"),
                ConfigManager.getInt(configIdentifier, "addTAButtonH"));

        if (offlineModeDTO.getUserIdentifier() == UserIdentifier.PROFESSOR) {
            add(addStudentButton);
            add(addTAButton);
        }
    }

    @Override
    protected void connectListeners() {
        seeButton.addActionListener(actionEvent -> {
            Date selectedDate = dateModel.getValue();
            if (selectedDate == null) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                        "noDateHasBeenSelected");
                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners", getClass());
            }

            selectedLocalDateTime = CalendarUtils.convertToLocalDateTime(selectedDate);

            updatePanel();
        });

        homeworksButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Opened courseware homeworks section",
                    "connectListeners", getClass());
            stopPanelLoop();
            if (offlineModeDTO.getUserIdentifier() == UserIdentifier.STUDENT) {
                mainFrame.setCurrentPanel(new StudentHomeworksView(mainFrame, mainMenu, offlineModeDTO, courseId));
            } else if (offlineModeDTO.getUserIdentifier() == UserIdentifier.PROFESSOR) {
                mainFrame.setCurrentPanel(new ProfessorHomeworksView(mainFrame, mainMenu, offlineModeDTO, courseId));
            }
        });

        educationalMaterialsButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Opened courseware educational materials section",
                    "connectListeners", getClass());
            stopPanelLoop();
            if (offlineModeDTO.getUserIdentifier() == UserIdentifier.STUDENT) {
                mainFrame.setCurrentPanel(new StudentMaterialsView(mainFrame, mainMenu, offlineModeDTO, courseId));
            } else if (offlineModeDTO.getUserIdentifier() == UserIdentifier.PROFESSOR) {
                mainFrame.setCurrentPanel(new ProfessorMaterialsView(mainFrame, mainMenu, offlineModeDTO, courseId));
            }
        });

        addStudentButton.addActionListener(actionEvent -> {
            String studentId = JOptionPane.showInputDialog(mainFrame, ConfigManager.getString(configIdentifier,
                    "addStudentPrompt"));
            if (studentId == null) return;

            Response response = clientController.addStudentToCourse(studentId, courseId);
            if (response == null) return;

            if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                MasterLogger.clientError(clientController.getId(), response.getErrorMessage(), "connectListeners",
                        getClass());
            } else {
                MasterLogger.clientInfo(clientController.getId(), "Added student (ID: " + studentId + ") to own course" +
                                " (ID: " + courseId + ")", "connectListeners", getClass());
            }
        });

        addTAButton.addActionListener(actionEvent -> {
            String teachingAssistantId = JOptionPane.showInputDialog(mainFrame, ConfigManager.getString(configIdentifier,
                    "addTAPrompt"));
            if (teachingAssistantId == null) return;

            Response response = clientController.addTeachingAssistantToCourse(teachingAssistantId, courseId);
            if (response == null) return;

            if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                MasterLogger.clientError(clientController.getId(), response.getErrorMessage(), "connectListeners",
                        getClass());
            } else {
                MasterLogger.clientInfo(clientController.getId(), "Added TA (ID: " + teachingAssistantId +
                        ") to own course (ID: " + courseId + ")", "connectListeners", getClass());
            }
        });

        goBackButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Went back to coursewares listview",
                    "connectListeners", getClass());
            stopPanelLoop();
            if (offlineModeDTO.getUserIdentifier() == UserIdentifier.STUDENT) {
                mainFrame.setCurrentPanel(new StudentCoursewaresView(mainFrame, mainMenu, offlineModeDTO));
            } else if (offlineModeDTO.getUserIdentifier() == UserIdentifier.PROFESSOR) {
                mainFrame.setCurrentPanel(new ProfessorCoursewaresView(mainFrame, mainMenu, offlineModeDTO));
            }
        });
    }
}