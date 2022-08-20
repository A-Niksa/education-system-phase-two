package client.gui.menus.enrolment.editing;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import client.gui.utils.EnumArrayUtils;
import client.gui.utils.ErrorUtils;
import client.locallogic.menus.enrolment.DegreeLevelGetter;
import client.locallogic.menus.enrolment.NamesParser;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.network.DTOs.generalmodels.CourseDTO;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CourseEditor extends DynamicPanelTemplate {
    private CourseDTO courseDTO;
    private JLabel courseNameLabel;
    private JTextField newCourseName;
    private JButton changeCourseName;
    private JTextField newTeachingProfessors;
    private JButton changeTeachingProfessors;
    private JTextField newNumberOfCredits;
    private JButton changeNumberOfCredits;
    private JComboBox<String> newDegreeLevel;
    private String[] degreeLevels;
    private JButton changeDegreeLevel;
    private JButton goBackButton;
    private JButton removeCourse;

    public CourseEditor(MainFrame mainFrame, MainMenu mainMenu, CourseDTO courseDTO,
                        OfflineModeDTO offlineModeDTO) {
        // TODO: perhaps updating the courseDTO
        super(mainFrame, mainMenu, offlineModeDTO);
        this.courseDTO = courseDTO;
        configIdentifier = ConfigFileIdentifier.GUI_COURSE_EDITOR;
        degreeLevels = EnumArrayUtils.initializeDegreeLevels();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void initializeComponents() {
        goBackButton = new JButton(ConfigManager.getString(configIdentifier, "goBackButtonM"));

        courseNameLabel = new JLabel(courseDTO.getCourseName(), SwingConstants.CENTER);

        newCourseName = new JTextField(ConfigManager.getString(configIdentifier, "newCourseNameM"));
        changeCourseName = new JButton(ConfigManager.getString(configIdentifier, "changeButtonM"));
        newTeachingProfessors = new JTextField(ConfigManager.getString(configIdentifier, "newTeachingProfessorsM"));
        changeTeachingProfessors = new JButton(ConfigManager.getString(configIdentifier, "changeButtonM"));
        newNumberOfCredits = new JTextField(ConfigManager.getString(configIdentifier, "newNumberOfCreditsM"));
        changeNumberOfCredits = new JButton(ConfigManager.getString(configIdentifier, "changeButtonM"));
        newDegreeLevel = new JComboBox<>(degreeLevels);
        changeDegreeLevel = new JButton(ConfigManager.getString(configIdentifier, "changeButtonM"));

        removeCourse = new JButton(ConfigManager.getString(configIdentifier, "removeCourseM"));
    }

    @Override
    protected void alignComponents() {
        goBackButton.setBounds(ConfigManager.getInt(configIdentifier, "goBackButtonX"),
                ConfigManager.getInt(configIdentifier, "goBackButtonY"),
                ConfigManager.getInt(configIdentifier, "goBackButtonW"),
                ConfigManager.getInt(configIdentifier, "goBackButtonH"));
        add(goBackButton);

        courseNameLabel.setBounds(ConfigManager.getInt(configIdentifier, "courseNameLabelX"),
                ConfigManager.getInt(configIdentifier, "courseNameLabelY"),
                ConfigManager.getInt(configIdentifier, "courseNameLabelW"),
                ConfigManager.getInt(configIdentifier, "courseNameLabelH"));
        courseNameLabel.setFont(new Font("", Font.BOLD,
                ConfigManager.getInt(configIdentifier, "courseNameLabelFontSize")));
        add(courseNameLabel);

        int currentX = ConfigManager.getInt(configIdentifier, "startingX");
        int currentY = ConfigManager.getInt(configIdentifier, "startingY");
        int incrementOfX = ConfigManager.getInt(configIdentifier, "incX");
        int incrementOfY = ConfigManager.getInt(configIdentifier, "incY");
        newCourseName.setBounds(currentX, currentY, ConfigManager.getInt(configIdentifier, "newCourseNameW"),
                ConfigManager.getInt(configIdentifier, "newCourseNameH"));
        add(newCourseName);
        changeCourseName.setBounds(currentX + incrementOfX, currentY,
                ConfigManager.getInt(configIdentifier, "changeCourseNameW"),
                ConfigManager.getInt(configIdentifier, "changeCourseNameH"));
        add(changeCourseName);
        currentY += incrementOfY;

        newTeachingProfessors.setBounds(currentX, currentY,
                ConfigManager.getInt(configIdentifier, "newTeachingProfessorsW"),
                ConfigManager.getInt(configIdentifier, "newTeachingProfessorsH"));
        add(newTeachingProfessors);
        changeTeachingProfessors.setBounds(currentX + incrementOfX, currentY,
                ConfigManager.getInt(configIdentifier, "changeTeachingProfessorsW"),
                ConfigManager.getInt(configIdentifier, "changeTeachingProfessorsH"));
        add(changeTeachingProfessors);
        currentY += incrementOfY;

        newNumberOfCredits.setBounds(currentX, currentY,
                ConfigManager.getInt(configIdentifier, "newNumberOfCreditsW"),
                ConfigManager.getInt(configIdentifier, "newNumberOfCreditsH"));
        add(newNumberOfCredits);
        changeNumberOfCredits.setBounds(currentX + incrementOfX, currentY,
                ConfigManager.getInt(configIdentifier, "changeNumberOfCreditsW"),
                ConfigManager.getInt(configIdentifier, "changeNumberOfCreditsH"));
        add(changeNumberOfCredits);
        currentY += incrementOfY;

        newDegreeLevel.setBounds(currentX, currentY, ConfigManager.getInt(configIdentifier, "newDegreeLevelW"),
                ConfigManager.getInt(configIdentifier, "newDegreeLevelH"));
        add(newDegreeLevel);
        changeDegreeLevel.setBounds(currentX + incrementOfX, currentY,
                ConfigManager.getInt(configIdentifier, "changeDegreeLevelW"),
                ConfigManager.getInt(configIdentifier, "changeDegreeLevelH"));
        add(changeDegreeLevel);
        currentY += incrementOfY;

        removeCourse.setBounds(currentX, currentY, ConfigManager.getInt(configIdentifier, "removeCourseW"),
                ConfigManager.getInt(configIdentifier, "removeCourseH"));
        add(removeCourse);
    }

    @Override
    protected void connectListeners() {
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Went back to courses list editor",
                        "connectListeners", getClass());
                stopPanelLoop();
                mainFrame.setCurrentPanel(new CoursesListEditor(mainFrame, mainMenu, offlineModeDTO));
            }
        });

        changeCourseName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String courseName = newCourseName.getText();
                String previousCourseName = courseNameLabel.getText();
                Response response = clientController.changeCourseName(courseDTO.getId(), courseName);
                if (response == null) return;
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    courseNameLabel.setText(courseName);
                    MasterLogger.clientInfo(clientController.getId(), "Course name changed from " +
                            previousCourseName + " to " + courseName, "connectListeners", getClass());
                }
            }
        });

        changeTeachingProfessors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String newTeachingProfessorNames = newTeachingProfessors.getText();
                String[] newTeachingProfessorNamesArray = NamesParser.parseDelimitedNames(newTeachingProfessorNames);
                Response response = clientController.changeTeachingProfessors(courseDTO.getId(),
                        newTeachingProfessorNamesArray);
                if (response == null) return;
                if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                    MasterLogger.clientError(clientController.getId(), response.getErrorMessage(),
                            "connectListeners", getClass());
                    return;
                }

                if (response.getResponseStatus() == ResponseStatus.OK) {
                    MasterLogger.clientInfo(clientController.getId(), courseDTO.getCourseName() + "'s professor(s)" +
                            " changed to " + newTeachingProfessorNames, "connectListeners", getClass());
                }
            }
        });

        changeNumberOfCredits.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int numberOfCredits = Integer.parseInt(newNumberOfCredits.getText());
                Response response = clientController.changeCourseNumberOfCredits(courseDTO.getId(), numberOfCredits);
                if (response == null) return;
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    MasterLogger.clientInfo(clientController.getId(), courseDTO.getCourseName() + "'s number of " +
                            "credits changed to " + numberOfCredits, "connectListeners", getClass());
                }
            }
        });

        changeDegreeLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String degreeLevelString = (String) newDegreeLevel.getSelectedItem();
                DegreeLevel degreeLevel = DegreeLevelGetter.getDegreeLevel(degreeLevelString);
                Response response = clientController.changeCourseDegreeLevel(courseDTO.getId(), degreeLevel);
                if (response == null) return;
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    MasterLogger.clientInfo(clientController.getId(), courseDTO.getCourseName() + "'s level changed to "
                            + degreeLevelString, "connectListeners", getClass());
                }
            }
        });

        removeCourse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Response response = clientController.removeCourse(courseDTO.getId());
                if (response == null) return;
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    MasterLogger.clientInfo(clientController.getId(), "Removed the selected course",
                            "connectListeners", getClass());
                }
            }
        });
    }

    @Override
    protected void updatePanel() {

    }
}
