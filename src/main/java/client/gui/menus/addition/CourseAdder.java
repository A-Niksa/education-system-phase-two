package client.gui.menus.addition;

import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.enrolment.editing.CoursesListEditor;
import client.gui.menus.main.MainMenu;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import shareables.models.pojos.users.professors.Professor;
import shareables.utils.logging.MasterLogger;
import shareables.utils.timing.formatting.DateLabelFormatter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class CourseAdder extends PanelTemplate {
    private Professor professor;
    private JTextField courseNameField;
    private JTextField courseIDField;
    private JTextField instructorsNameField;
    private JTextField numberOfCreditsField;
    private ArrayList<JTextField> textFieldsList;
    private String[] courseLevelNames;
    private JComboBox<String> courseLevelBox;
    private String[] weekdayNames;
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

    public CourseAdder(MainFrame mainFrame, MainMenu mainMenu, Professor professor) {
        super(mainFrame, mainMenu);
        this.professor = professor;
        drawPanel();
    }

    @Override
    protected void initializeComponents() {
        textFieldsList = new ArrayList<>();

        courseNameField = new JTextField("Course Name...");
        textFieldsList.add(courseNameField);
        courseIDField = new JTextField("Course ID...");
        textFieldsList.add(courseIDField);
        instructorsNameField = new JTextField("Instructor's Name...");
        textFieldsList.add(instructorsNameField);
        numberOfCreditsField = new JTextField("Number of Credits...");
        textFieldsList.add(numberOfCreditsField);

        courseLevelNames = new String[]{"Bachelors", "Graduate", "PhD"};
        courseLevelBox = new JComboBox<>(courseLevelNames);
        weekdayNames = new String[]{"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        firstClassPrompt = new JLabel("First Class in Week:");
        firstClassWeekdayBox = new JComboBox<>(weekdayNames);
        firstClassStartHourField = new JTextField("Start Hour...");
        firstClassStartMinuteField = new JTextField("Start Minute...");
        firstClassEndHourField = new JTextField("End Hour...");
        firstClassEndMinuteField = new JTextField("End Minute...");
        secondClassPrompt = new JLabel("Second Class in Week:");
        secondClassWeekdayBox = new JComboBox<>(weekdayNames);
        secondClassStartHourField = new JTextField("Start Hour...");
        secondClassStartMinuteField = new JTextField("Start Minute...");
        secondClassEndHourField = new JTextField("End Hour...");
        secondClassEndMinuteField = new JTextField("End Minute...");

        examDatePrompt = new JLabel("Exam Date: ");
        examDateModel = new UtilDateModel();
        properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");
        examDatePanel = new JDatePanelImpl(examDateModel, properties);
        examDatePicker = new JDatePickerImpl(examDatePanel, new DateLabelFormatter());

        examHourField = new JTextField("Hour...");
        examMinuteField = new JTextField("Minute...");

        addCourseButton = new JButton("Add Course");

        goBackButton = new JButton("Back");
    }

    @Override
    protected void alignComponents() {
        int currentX = 350, currentY = 18;
        for (JTextField textField : textFieldsList) {
            textField.setBounds(currentX, currentY, 300, 30);
            add(textField);
            currentY += 40;
        }

        courseLevelBox.setBounds(currentX, currentY, 300, 30);
        add(courseLevelBox);
        currentY += 40;

        firstClassPrompt.setBounds(currentX, currentY, 300, 30);
        add(firstClassPrompt);
        currentY += 35;
        firstClassWeekdayBox.setBounds(currentX, currentY, 300, 30);
        add(firstClassWeekdayBox);
        currentY += 40;
        firstClassStartHourField.setBounds(currentX, currentY, 142, 30);
        add(firstClassStartHourField);
        firstClassStartMinuteField.setBounds(currentX + 158, currentY, 142, 30);
        add(firstClassStartMinuteField);
        currentY += 35;
        firstClassEndHourField.setBounds(currentX, currentY, 142, 30);
        add(firstClassEndHourField);
        firstClassEndMinuteField.setBounds(currentX + 158, currentY, 142, 30);
        add(firstClassEndMinuteField);
        currentY += 40;

        secondClassPrompt.setBounds(currentX, currentY, 300, 30);
        add(secondClassPrompt);
        currentY += 35;
        secondClassWeekdayBox.setBounds(currentX, currentY, 300, 30);
        add(secondClassWeekdayBox);
        currentY += 40;
        secondClassStartHourField.setBounds(currentX, currentY, 142, 30);
        add(secondClassStartHourField);
        secondClassStartMinuteField.setBounds(currentX + 158, currentY, 142, 30);
        add(secondClassStartMinuteField);
        currentY += 35;
        secondClassEndHourField.setBounds(currentX, currentY, 142, 30);
        add(secondClassEndHourField);
        secondClassEndMinuteField.setBounds(currentX + 158, currentY, 142, 30);
        add(secondClassEndMinuteField);
        currentY += 40;

        examDatePrompt.setBounds(currentX, currentY, 300, 30);
        add(examDatePrompt);
        currentY += 35;
        examDatePicker.setBounds(currentX, currentY, 300, 30);
        add(examDatePicker);
        currentY += 40;
        examHourField.setBounds(currentX, currentY, 142, 30);
        add(examHourField);
        examMinuteField.setBounds(currentX + 158, currentY, 142, 30);
        add(examMinuteField);
        currentY += 40;

        addCourseButton.setBounds(15, 582, 205, 30);
        add(addCourseButton);

        goBackButton.setBounds(140, 622, 80, 30);
        add(goBackButton);
    }

    @Override
    protected void connectListeners() {
        addCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String courseName = courseNameField.getText();
                String courseID = courseIDField.getText();
                String instructorsName = instructorsNameField.getText();
                int numberOfCredits = Integer.parseInt(numberOfCreditsField.getText());
                String courseLevelString = (String) courseLevelBox.getSelectedItem();
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
                Department department = DepartmentsDB.getProfessorsDepartment(professor);

                CourseConstruction.constructCourse(courseName, courseID, instructorsName, numberOfCredits, courseLevelString,
                        firstClassWeekdayString, firstClassStartHour, firstClassStartMinute, firstClassEndHour,
                        firstClassEndMinute, secondClassWeekdayString, secondClassStartHour, secondClassStartMinute,
                        secondClassEndHour, secondClassEndMinute, selectedExamDate, examHour, examMinute, department);
                MasterLogger.info("added course (ID: " + courseID + ") to the system", getClass());
            }
        });

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.info("went back to courses list editor", getClass());
                mainFrame.setCurrentPanel(new CoursesListEditor(mainFrame, mainMenu, professor));
            }
        });
    }
}
