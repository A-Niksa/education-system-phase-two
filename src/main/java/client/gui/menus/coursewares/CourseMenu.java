package client.gui.menus.coursewares;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import client.locallogic.general.DatePickerConfigurationTool;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import shareables.network.DTOs.coursewares.CalendarEventDTO;
import shareables.network.DTOs.coursewares.CoursewareDTO;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.utils.config.ConfigManager;
import shareables.utils.timing.formatting.DateLabelFormatter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class CourseMenu extends DynamicPanelTemplate {
    private CoursewareDTO coursewareDTO;
    private UtilDateModel dateModel;
    private Properties properties;
    private JDatePanelImpl datePanel;
    private JDatePickerImpl datePicker;
    private JButton seeButton;
    private ArrayList<CalendarEventDTO> calendarEventDTOs;
    private String[] calendarEventTexts;
    private DefaultListModel<String> listModel;
    private JList<String> graphicalList;
    private JScrollPane scrollPane;
    private JButton homeworksButton;
    private JButton educationalMaterialsButton;

    public CourseMenu(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        updateCoursewareDTO();
        updateCalendarEventDTOs();
        updateCalendarEventTexts();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    private void updateCalendarEventTexts() {

    }

    private void updateCalendarEventDTOs() {
    }

    private void updateCoursewareDTO() {
    }

    @Override
    protected void updatePanel() {
        updateCoursewareDTO();
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
    }

    @Override
    protected void connectListeners() {

    }
}
