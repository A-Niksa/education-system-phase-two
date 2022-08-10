package client.gui.menus.addition;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.enrolment.editing.CoursesListEditor;
import client.gui.menus.main.MainMenu;
import client.gui.utils.EnumArrayUtils;
import client.gui.utils.ErrorUtils;
import client.locallogic.addition.BlueprintGenerator;
import client.locallogic.enrolment.NamesParser;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.OfflineModeDTO;
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
    private Professor professor;
    private JTextField courseNameField;
    private JTextField termIdentifierField;
    private JTextField teachingProfessorNames;
    private JTextField numberOfCreditsField;
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
    private JButton addCourseButton;
    private JButton goBackButton;

    public CourseAdder(MainFrame mainFrame, MainMenu mainMenu, Professor professor, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.professor = professor;
        configIdentifier = ConfigFileIdentifier.GUI_COURSE_ADDER;
        degreeLevels = EnumArrayUtils.initializeDegreeLevels();
        weekdays = EnumArrayUtils.initializeWeekdays();
        drawPanel();
    }

    @Override
    protected void initializeComponents() {
        textFieldsList = new ArrayList<>();

        courseNameField = new JTextField(ConfigManager.getString(configIdentifier, "courseNameFieldM"));
        textFieldsList.add(courseNameField);
        termIdentifierField = new JTextField(ConfigManager.getString(configIdentifier, "termIdentifierFieldM"));
        textFieldsList.add(termIdentifierField);
        teachingProfessorNames =
                new JTextField(ConfigManager.getString(configIdentifier, "teachingProfessorNamesM"));
        textFieldsList.add(teachingProfessorNames);
        numberOfCreditsField = new JTextField(ConfigManager.getString(configIdentifier, "numberOfCreditsM"));
        textFieldsList.add(numberOfCreditsField);

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

        addCourseButton = new JButton(ConfigManager.getString(configIdentifier, "addCourseButtonM"));

        goBackButton = new JButton(ConfigManager.getString(configIdentifier, "goBackButtonM"));
    }

    @Override
    protected void alignComponents() {
        int currentX = ConfigManager.getInt(configIdentifier, "startingX");
        int currentY = ConfigManager.getInt(configIdentifier, "startingY");
        int incrementOfX = ConfigManager.getInt(configIdentifier, "incX");
        int incrementOfY = ConfigManager.getInt(configIdentifier, "incY");
        int smallerIncrementOfY = ConfigManager.getInt(configIdentifier, "smallerIncY");
        int textFieldWidth = ConfigManager.getInt(configIdentifier, "textFieldW");
        int textFieldHeight = ConfigManager.getInt(configIdentifier, "textFieldH");
        int smallerTextFieldWidth = ConfigManager.getInt(configIdentifier, "smallerTextFieldW");
        for (JTextField textField : textFieldsList) {
            textField.setBounds(currentX, currentY, textFieldWidth, textFieldHeight);
            add(textField);
            currentY += incrementOfY;
        }

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
                String termIdentifierString = termIdentifierField.getText();
                String[] teachingProfessorNamesArray = NamesParser.parseDelimitedNames(teachingProfessorNames.getText());
                int numberOfCredits = Integer.parseInt(numberOfCreditsField.getText());
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

                Blueprint courseBlueprint = BlueprintGenerator.generateCourseBlueprint(courseName, termIdentifierString,
                        teachingProfessorNamesArray, numberOfCredits, degreeLevelString, firstClassWeekdayString,
                        firstClassStartHour, firstClassStartMinute, firstClassEndHour, firstClassEndMinute,
                        secondClassWeekdayString, secondClassStartHour, secondClassStartMinute, secondClassEndHour,
                        secondClassEndMinute, selectedExamDate, examHour, examMinute, professor.getDepartmentId());
                Response response = clientController.addCourse(courseBlueprint);
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
                mainFrame.setCurrentPanel(new CoursesListEditor(mainFrame, mainMenu, professor, offlineModeDTO));
            }
        });
    }

    @Override
    protected void updatePanel() {

    }
}
