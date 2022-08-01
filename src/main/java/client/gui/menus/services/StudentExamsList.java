package client.gui.menus.services;

import gui.MainFrame;
import gui.Template;
import gui.main.MainMenu;
import logic.menus.services.ExamsListLoader;
import logic.models.abstractions.Course;
import logic.models.roles.Student;
import logic.models.roles.User;

import javax.swing.*;
import java.util.LinkedList;

public class StudentExamsList extends Template {
    private Student student;
    private JTable examsTable;
    private String[] columns;
    private String[][] data;

    public StudentExamsList(MainFrame mainFrame, MainMenu mainMenu, User user) {
        super(mainFrame, mainMenu);
        student = (Student) user;
        columns = new String[]{"Course Name", "Exam Date and Time"};
        setTableData();
        drawPanel();
    }

    private void setTableData() {
        LinkedList<Course> sortedCoursesList = ExamsListLoader.getSortedListOfCoursesPerExam(student);
        Course course;
        data = new String[sortedCoursesList.size()][];
        for (int i = 0; i < sortedCoursesList.size(); i++) {
            course = sortedCoursesList.get(i);
            data[i] = new String[]{course.getCourseName(), course.getExamTimeString()};
        }
    }

    @Override
    protected void initializeComponents() {
        examsTable = new JTable(data, columns);
    }

    @Override
    protected void alignComponents() {
        examsTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(examsTable);
        scrollPane.setBounds(50, 50, 885, 530);
        add(scrollPane);
    }

    @Override
    protected void connectListeners() {
    }
}