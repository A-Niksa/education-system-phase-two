package client.gui.menus.enrolment.editing;

import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.main.MainMenu;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.CourseDTO;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CourseEditor extends PanelTemplate {
    private Professor deputy;
    private CourseDTO courseDTO;
    private JLabel courseNameLabel;
    private JTextField newCourseName;
    private JButton changeCourseName;
    private JTextField newInstructor;
    private JButton changeInstructor;
    private JTextField newNumberOfCredits;
    private JButton changeNumberOfCredits;
    private JComboBox<String> newCourseLevel;
    private JButton changeCourseLevel;
    private JButton goBackButton;
    private JButton removeCourse;

    public CourseEditor(MainFrame mainFrame, MainMenu mainMenu, Professor deputy, CourseDTO courseDTO) {
        super(mainFrame, mainMenu);
        this.deputy = deputy;
        this.courseDTO = courseDTO;
        drawPanel();
    }

    @Override
    protected void initializeComponents() {
        goBackButton = new JButton("Back");

        courseNameLabel = new JLabel(courseDTO.getCourseName(), SwingConstants.CENTER);

        newCourseName = new JTextField("New Course Name...");
        changeCourseName = new JButton("Change");
        newInstructor = new JTextField("Name of New Instructor...");
        changeInstructor = new JButton("Change");
        newNumberOfCredits = new JTextField("Changed Number of Credits...");
        changeNumberOfCredits = new JButton("Change");
        newCourseLevel = new JComboBox<>(new String[]{"Bachelors", "Graduate", "PhD"});
        changeCourseLevel = new JButton("Change");

        removeCourse = new JButton("Remove Course");
    }

    @Override
    protected void alignComponents() {
        goBackButton.setBounds(140, 622, 80, 30);
        add(goBackButton);

        courseNameLabel.setBounds(405, 110, 200, 50);
        courseNameLabel.setFont(new Font("", Font.BOLD, 16));
        add(courseNameLabel);

        int currentX = 300, currentY = 200;
        newCourseName.setBounds(currentX, currentY, 250, 30);
        add(newCourseName);
        changeCourseName.setBounds(currentX + 265, currentY, 150, 30);
        add(changeCourseName);
        currentY += 45;

        newInstructor.setBounds(currentX, currentY, 250, 30);
        add(newInstructor);
        changeInstructor.setBounds(currentX + 265, currentY, 150, 30);
        add(changeInstructor);
        currentY += 45;

        newNumberOfCredits.setBounds(currentX, currentY, 250, 30);
        add(newNumberOfCredits);
        changeNumberOfCredits.setBounds(currentX + 265, currentY, 150, 30);
        add(changeNumberOfCredits);
        currentY += 45;

        newCourseLevel.setBounds(currentX, currentY, 250, 30);
        add(newCourseLevel);
        changeCourseLevel.setBounds(currentX + 265, currentY, 150, 30);
        add(changeCourseLevel);
        currentY += 45;

        removeCourse.setBounds(currentX, currentY, 415, 30);
        add(removeCourse);
    }

    @Override
    protected void connectListeners() {
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Went back to courses list editor",
                        "connectListeners", getClass());
                mainFrame.setCurrentPanel(new CoursesListEditor(mainFrame, mainMenu, deputy));
            }
        });

        changeCourseName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String courseName = newCourseName.getText();
                String previousCourseName = courseNameLabel.getText();
                courseDTO.setCourseName(courseName);
                courseDTO.updateInDatabase();
                courseNameLabel.setText(courseName);
                MasterLogger.info("course name changed from " + previousCourseName + " to " + courseName,
                        getClass());
            }
        });

        changeInstructor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String instructorName = newInstructor.getText();
                Professor professor = ProfessorsDB.getProfessorWithName(instructorName);
                if (professor == null) {
                    JOptionPane.showMessageDialog(mainFrame,
                            "No such professor exists in your department.");
                    MasterLogger.error("entered non-existent professor name", getClass());
                    return;
                }

                courseDTO.setTeachingProfessor(professor);
                courseDTO.updateInDatabase();
                MasterLogger.info(courseNameLabel.getText() + "'s instructor changed to " + instructorName,
                        getClass());
            }
        });

        changeNumberOfCredits.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int numberOfCredits = Integer.parseInt(newNumberOfCredits.getText());
                courseDTO.setNumberOfCredits(numberOfCredits);
                courseDTO.updateInDatabase();
                MasterLogger.info(courseNameLabel.getText() + "'s number of credits changed to " + numberOfCredits,
                        getClass());
            }
        });

        changeCourseLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String courseLevelString = (String) newCourseLevel.getSelectedItem();
                Course.CourseLevel courseLevel = getLevelEnum(courseLevelString);
                courseDTO.setCourseLevel(courseLevel);
                courseDTO.updateInDatabase();
                MasterLogger.info(courseNameLabel.getText() + "'s level changed to " + courseLevelString,
                        getClass());
            }
        });

        removeCourse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Department department = DepartmentsDB.getProfessorsDepartment(deputy);

                StudentsDB.removeCourseFromTranscripts(courseDTO);
                department.removeCourse(courseDTO);
                CoursesDB.removeFromDatabase(courseDTO);
                MasterLogger.info("removed the selected course", getClass());
            }
        });
    }
}
