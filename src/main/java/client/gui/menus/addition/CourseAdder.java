package client.gui.menus.addition;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.enrolment.editing.CoursesListEditor;
import client.gui.menus.main.MainMenu;
import client.gui.utils.EnumArrayUtils;
import client.gui.utils.ErrorUtils;
import client.locallogic.menus.addition.BlueprintGenerator;
import client.locallogic.menus.enrolment.NamesParser;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.blueprints.Blueprint;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;
import shareables.utils.timing.formatting.DateLabelFormatter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class CourseAdder extends DynamicPanelTemplate {
    private JTextField courseNameField;
    private JTextField courseIdField;
    private JTextField termIdentifierField;
    private JTextField prerequisiteIdsField;
    private JTextField corequisiteIdsField;
    private JTextField teachingProfessorNames;
    private JTextField teachingAssistantIds;
    private JTextField numberOfCreditsField;
    private JTextField courseCapacityField;
    private ArrayList<JTextField> textFieldsList;
    private String[] degreeLevels;
    private JComboBox<String> degreeLevelBox;
    private String[] weekdays;
    private JLabel firstClassPrompt;
    private JComboBox<String> firstClassWeekdayBox; // firstWeekday refers to the first class in a week
    private JTextField firstClassStartHourField;
    private JTextField firstClassStartMinuteField;
    private JTextField firstClassEndHourField;
    private JTextField firstClassEndMinuteField;
    private JLabel secondClassPrompt;
    private JComboBox<String> secondClassWeekdayBox; // secondWeekday refers to the second class in a week
    private JTextField secondClassStartHourField;
    private JTextField secondClassStartMinuteField;
    private JTextField secondClassEndHourField;
    private JTextField secondClassEndMinuteField;
    private JLabel examDatePrompt;
    private Properties properties;
    private UtilDateModel examDateModel;
    private JDatePanelImpl examDatePanel;
    private JDatePickerImpl examDatePicker;
    private JTextField examHourField;
    private JTextField examMinuteField;
    private JCheckBox theologyCourseCheckBox;
    private JButton addCourseButton;
    private JButton goBackButton;

    public CourseAdder(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        configIdentifier = ConfigFileIdentifier.GUI_COURSE_ADDER;
        degreeLevels = EnumArrayUtils.initializeDegreeLevels();
        weekdays = EnumArrayUtils.initializeWeekdays();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void initializeComponents() {
        textFieldsList = new ArrayList<>();

        courseNameField = new JTextField(ConfigManager.getString(configIdentifier, "courseNameFieldM"));
        textFieldsList.add(courseNameField);
        courseIdField = new JTextField(ConfigManager.getString(configIdentifier, "courseIdFieldM"));
        textFieldsList.add(courseIdField);
        termIdentifierField = new JTextField(ConfigManager.getString(configIdentifier, "termIdentifierFieldM"));
        textFieldsList.add(termIdentifierField);
        prerequisiteIdsField = new JTextField(ConfigManager.getString(configIdentifier, "prerequisiteIdsFieldM"));
        textFieldsList.add(prerequisiteIdsField);
        corequisiteIdsField = new JTextField(ConfigManager.getString(configIdentifier, "corequisiteIdsFieldM"));
        textFieldsList.add(corequisiteIdsField);
        teachingProfessorNames =
                new JTextField(ConfigManager.getString(configIdentifier, "teachingProfessorNamesM"));
        textFieldsList.add(teachingProfessorNames);
        teachingAssistantIds =
                new JTextField(ConfigManager.getString(configIdentifier, "teachingAssistantIdsM"));
        textFieldsList.add(teachingAssistantIds);
        numberOfCreditsField = new JTextField(ConfigManager.getString(configIdentifier, "numberOfCreditsM"));
        textFieldsList.add(numberOfCreditsField);
        courseCapacityField = new JTextField(ConfigManager.getString(configIdentifier, "courseCapacityFieldM"));
        textFieldsList.add(courseCapacityField);

        degreeLevelBox = new JComboBox<>(degreeLevels);
        firstClassPrompt = new JLabel(ConfigManager.getString(configIdentifier, "firstClassPromptM"));
        firstClassWeekdayBox = new JComboBox<>(weekdays);
        firstClassStartHourField = new JTextField(ConfigManager.getString(configIdentifier, "startHourFieldM"));
        firstClassStartMinuteField = new JTextField(ConfigManager.getString(configIdentifier, "startMinuteFieldM"));
        firstClassEndHourField = new JTextField(ConfigManager.getString(configIdentifier, "endHourFieldM"));
        firstClassEndMinuteField = new JTextField(ConfigManager.getString(configIdentifier, "endMinuteFieldM"));
        secondClassPrompt = new JLabel(ConfigManager.getString(configIdentifier, "secondClassPromptM"));
        secondClassWeekdayBox = new JComboBox<>(weekdays);
        secondClassStartHourField = new JTextField(ConfigManager.getString(configIdentifier, "startHourFieldM"));
        secondClassStartMinuteField = new JTextField(ConfigManager.getString(configIdentifier, "startMinuteFieldM"));
        secondClassEndHourField = new JTextField(ConfigManager.getString(configIdentifier, "endHourFieldM"));
        secondClassEndMinuteField = new JTextField(ConfigManager.getString(configIdentifier, "endMinuteFieldM"));

        examDatePrompt = new JLabel(ConfigManager.getString(configIdentifier, "examDatePromptM"));
        examDateModel = new UtilDateModel();
        properties = new Properties();
        properties.put("text.today", ConfigManager.getString(configIdentifier, "propertiesTextTodayM"));
        properties.put("text.month", ConfigManager.getString(configIdentifier, "propertiesTextMonthM"));
        properties.put("text.year", ConfigManager.getString(configIdentifier, "propertiesTextYearM"));
        examDatePanel = new JDatePanelImpl(examDateModel, properties);
        examDatePicker = new JDatePickerImpl(examDatePanel, new DateLabelFormatter());

        examHourField = new JTextField(ConfigManager.getString(configIdentifier, "examHourFieldM"));
        examMinuteField = new JTextField(ConfigManager.getString(configIdentifier, "examMinuteFieldM"));

        theologyCourseCheckBox = new JCheckBox(ConfigManager.getString(configIdentifier,
                "theologyCourseCheckBoxM"));

        addCourseButton = new JButton(ConfigManager.getString(configIdentifier, "addCourseButtonM"));

        goBackButton = new JButton(ConfigManager.getString(configIdentifier, "goBackButtonM"));
    }

    @Override
    protected void alignComponents() {
        int currentX = ConfigManager.getInt(configIdentifier, "startingX");
        int currentY = ConfigManager.getInt(configIdentifier, "startingY");
        int incrementOfX = ConfigManager.getInt(configIdentifier, "incX");
        int smallerIncrementOfX = ConfigManager.getInt(configIdentifier, "smallerIncX");
        int incrementOfY = ConfigManager.getInt(configIdentifier, "incY");
        int smallerIncrementOfY = ConfigManager.getInt(configIdentifier, "smallerIncY");
        int textFieldWidth = ConfigManager.getInt(configIdentifier, "textFieldW");
        int textFieldHeight = ConfigManager.getInt(configIdentifier, "textFieldH");
        int smallerTextFieldWidth = ConfigManager.getInt(configIdentifier, "smallerTextFieldW");
        int tinyTextFieldWidth = ConfigManager.getInt(configIdentifier, "tinyTextFieldW");

        courseNameField.setBounds(currentX, currentY, tinyTextFieldWidth, textFieldHeight);
        courseIdField.setBounds(currentX + smallerIncrementOfX, currentY, tinyTextFieldWidth, textFieldHeight);
        termIdentifierField.setBounds(currentX + 2 * smallerIncrementOfX, currentY, tinyTextFieldWidth, textFieldHeight);
        currentY += incrementOfY;
        prerequisiteIdsField.setBounds(currentX, currentY, smallerTextFieldWidth, textFieldHeight);
        corequisiteIdsField.setBounds(currentX + incrementOfX, currentY, smallerTextFieldWidth, textFieldHeight);
        currentY += incrementOfY;
        teachingProfessorNames.setBounds(currentX, currentY, smallerTextFieldWidth, textFieldHeight);
        teachingAssistantIds.setBounds(currentX + incrementOfX, currentY, smallerTextFieldWidth, textFieldHeight);
        currentY += incrementOfY;
        numberOfCreditsField.setBounds(currentX, currentY, smallerTextFieldWidth, textFieldHeight);
        courseCapacityField.setBounds(currentX + incrementOfX, currentY, smallerTextFieldWidth, textFieldHeight);
        currentY += incrementOfY;

        textFieldsList.forEach(this::add);

        degreeLevelBox.setBounds(currentX, currentY, textFieldWidth, textFieldHeight);
        add(degreeLevelBox);
        currentY += incrementOfY;

        firstClassPrompt.setBounds(currentX, currentY, textFieldWidth, textFieldHeight);
        add(firstClassPrompt);
        currentY += smallerIncrementOfY;
        firstClassWeekdayBox.setBounds(currentX, currentY, textFieldWidth, textFieldHeight);
        add(firstClassWeekdayBox);
        currentY += incrementOfY;
        firstClassStartHourField.setBounds(currentX, currentY, smallerTextFieldWidth, textFieldHeight);
        add(firstClassStartHourField);
        firstClassStartMinuteField.setBounds(currentX + incrementOfX, currentY, smallerTextFieldWidth, textFieldHeight);
        add(firstClassStartMinuteField);
        currentY += smallerIncrementOfY;
        firstClassEndHourField.setBounds(currentX, currentY, smallerTextFieldWidth, textFieldHeight);
        add(firstClassEndHourField);
        firstClassEndMinuteField.setBounds(currentX + incrementOfX, currentY, smallerTextFieldWidth, textFieldHeight);
        add(firstClassEndMinuteField);
        currentY += incrementOfY;

        secondClassPrompt.setBounds(currentX, currentY, textFieldWidth, textFieldHeight);
        add(secondClassPrompt);
        currentY += smallerIncrementOfY;
        secondClassWeekdayBox.setBounds(currentX, currentY, textFieldWidth, textFieldHeight);
        add(secondClassWeekdayBox);
        currentY += incrementOfY;
        secondClassStartHourField.setBounds(currentX, currentY, smallerTextFieldWidth, textFieldHeight);
        add(secondClassStartHourField);
        secondClassStartMinuteField.setBounds(currentX + incrementOfX, currentY, smallerTextFieldWidth, textFieldHeight);
        add(secondClassStartMinuteField);
        currentY += smallerIncrementOfY;
        secondClassEndHourField.setBounds(currentX, currentY, smallerTextFieldWidth, textFieldHeight);
        add(secondClassEndHourField);
        secondClassEndMinuteField.setBounds(currentX + incrementOfX, currentY, smallerTextFieldWidth, textFieldHeight);
        add(secondClassEndMinuteField);
        currentY += incrementOfY;

        examDatePrompt.setBounds(currentX, currentY, textFieldWidth, textFieldHeight);
        add(examDatePrompt);
        currentY += smallerIncrementOfY;
        examDatePicker.setBounds(currentX, currentY, textFieldWidth, textFieldHeight);
        add(examDatePicker);
        currentY += incrementOfY;
        examHourField.setBounds(currentX, currentY, smallerTextFieldWidth, textFieldHeight);
        add(examHourField);
        examMinuteField.setBounds(currentX + incrementOfX, currentY, smallerTextFieldWidth, textFieldHeight);
        add(examMinuteField);
//        currentY += incrementOfY;

        theologyCourseCheckBox.setBounds(ConfigManager.getInt(configIdentifier, "theologyCourseCheckBoxX"),
                ConfigManager.getInt(configIdentifier, "theologyCourseCheckBoxY"),
                ConfigManager.getInt(configIdentifier, "theologyCourseCheckBoxW"),
                ConfigManager.getInt(configIdentifier, "theologyCourseCheckBoxH"));

        String generalCentersId = ConfigManager.getString(ConfigFileIdentifier.CONSTANTS, "generalCentersId");
        if (offlineModeDTO.getDepartmentId().equals(generalCentersId)) {
            add(theologyCourseCheckBox);
        }

        addCourseButton.setBounds(ConfigManager.getInt(configIdentifier, "addCourseButtonX"),
                ConfigManager.getInt(configIdentifier, "addCourseButtonY"),
                ConfigManager.getInt(configIdentifier, "addCourseButtonW"),
                ConfigManager.getInt(configIdentifier, "addCourseButtonH"));
        add(addCourseButton);

        goBackButton.setBounds(ConfigManager.getInt(configIdentifier, "goBackButtonX"),
                ConfigManager.getInt(configIdentifier, "goBackButtonY"),
                ConfigManager.getInt(configIdentifier, "goBackButtonW"),
                ConfigManager.getInt(configIdentifier, "goBackButtonH"));
        add(goBackButton);
    }

    @Override
    protected void connectListeners() {
        addCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String courseName = courseNameField.getText();
                String courseId = courseIdField.getText();
                String termIdentifierString = termIdentifierField.getText();
                String[] prerequisiteIdsArray = NamesParser.parseDelimitedNames(prerequisiteIdsField.getText());
                String[] corequisiteIdsArray = NamesParser.parseDelimitedNames(corequisiteIdsField.getText());
                String[] teachingProfessorNamesArray = NamesParser.parseDelimitedNames(teachingProfessorNames.getText());
                String[] teachingAssistantIdsArray = NamesParser.parseDelimitedNames(teachingAssistantIds.getText());
                int numberOfCredits = Integer.parseInt(numberOfCreditsField.getText());
                int courseCapacity = Integer.parseInt(courseCapacityField.getText());
                String degreeLevelString = (String) degreeLevelBox.getSelectedItem();
                String firstClassWeekdayString = (String) firstClassWeekdayBox.getSelectedItem();
                int firstClassStartHour = Integer.parseInt(firstClassStartHourField.getText());
                int firstClassStartMinute = Integer.parseInt(firstClassStartMinuteField.getText());
                int firstClassEndHour = Integer.parseInt(firstClassEndHourField.getText());
                int firstClassEndMinute = Integer.parseInt(firstClassEndMinuteField.getText());
                String secondClassWeekdayString = (String) secondClassWeekdayBox.getSelectedItem();
                int secondClassStartHour = Integer.parseInt(secondClassStartHourField.getText());
                int secondClassStartMinute = Integer.parseInt(secondClassStartMinuteField.getText());
                int secondClassEndHour = Integer.parseInt(secondClassEndHourField.getText());
                int secondClassEndMinute = Integer.parseInt(secondClassEndMinuteField.getText());
                Date selectedExamDate = examDateModel.getValue();
                int examHour = Integer.parseInt(examHourField.getText());
                int examMinute = Integer.parseInt(examMinuteField.getText());
                boolean isTheologyCourse = theologyCourseCheckBox.isSelected();

                Blueprint courseBlueprint = BlueprintGenerator.generateCourseBlueprint(courseName, courseId, termIdentifierString,
                        prerequisiteIdsArray, corequisiteIdsArray, teachingProfessorNamesArray, teachingAssistantIdsArray,
                        numberOfCredits, courseCapacity, degreeLevelString, firstClassWeekdayString,
                        firstClassStartHour, firstClassStartMinute, firstClassEndHour, firstClassEndMinute,
                        secondClassWeekdayString, secondClassStartHour, secondClassStartMinute, secondClassEndHour,
                        secondClassEndMinute, selectedExamDate, examHour, examMinute, isTheologyCourse,
                        offlineModeDTO.getDepartmentId());
                Response response = clientController.addCourse(courseBlueprint);
                if (response == null) return;
                if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                    MasterLogger.clientError(clientController.getId(), response.getErrorMessage(),
                            "connectListeners", getClass());
                } else {
                    JOptionPane.showMessageDialog(mainFrame, response.getUnsolicitedMessage());
                    MasterLogger.clientInfo(clientController.getId(), response.getUnsolicitedMessage(),
                            "connectListeners", getClass());
                }
            }
        });

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Went back to courses list editor",
                        "connectListeners", getClass());
                stopPanelLoop();
                mainFrame.setCurrentPanel(new CoursesListEditor(mainFrame, mainMenu, offlineModeDTO));
            }
        });
    }

    @Override
    protected void updatePanel() {
    }
}
