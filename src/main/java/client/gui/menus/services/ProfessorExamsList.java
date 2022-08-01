package client.gui.menus.services;

import gui.MainFrame;
import gui.Template;
import gui.main.MainMenu;
import logic.menus.services.ExamsListLoader;
import logic.models.abstractions.Course;
import logic.models.roles.Professor;

import javax.swing.*;
import java.util.LinkedList;

public class ProfessorExamsList extends Template {
    Professor operatingProfessor;
    private JTable examsTable;
    private String[] columns;
    private String[][] data;

    public ProfessorExamsList(MainFrame mainFrame, MainMenu mainMenu, Professor operatingProfessor) {
        super(mainFrame, mainMenu);
        this.operatingProfessor = operatingProfessor;
        columns = new String[]{"Course Name", "Exam Date and Time"};
        setTableData();
        drawPanel();
    }

    private void setTableData() {
        LinkedList<Course> sortedCoursesList = ExamsListLoader.getSortedListOfCoursesPerExam(operatingProfessor);
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
