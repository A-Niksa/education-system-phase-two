package client.gui.menus.services;

import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.main.MainMenu;
import client.locallogic.services.ExamsListSorter;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.CourseDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import javax.swing.*;
import java.util.ArrayList;

public class ProfessorExamsList extends PanelTemplate {
    Professor professor;
    private ArrayList<CourseDTO> courseDTOs;
    private JTable examsTable;
    private String[] columns;
    private String[][] data;

    public ProfessorExamsList(MainFrame mainFrame, MainMenu mainMenu, Professor professor) {
        super(mainFrame, mainMenu);
        this.professor = professor;
        configIdentifier = ConfigFileIdentifier.GUI_EXAMS_LIST;
        initializeCourseDTOs();
        initializeColumns();
        setTableData();
        drawPanel();
    }

    private void initializeCourseDTOs() {
        Response response = clientController.getProfessorCourseDTOs(professor.getId());
        courseDTOs = (ArrayList<CourseDTO>) response.get("courseDTOs");
    }

    private void initializeColumns() {
        columns = new String[2];
        columns[0] = ConfigManager.getString(configIdentifier, "courseNameColumn");
        columns[1] = ConfigManager.getString(configIdentifier, "examDateAndTimeColumn");
    }

    private void setTableData() {
        ArrayList<CourseDTO> sortedCourseDTOs = ExamsListSorter.getSortedCourseDTOs(courseDTOs);
        CourseDTO courseDTO;
        data = new String[sortedCourseDTOs.size()][];
        for (int i = 0; i < sortedCourseDTOs.size(); i++) {
            courseDTO = sortedCourseDTOs.get(i);
            data[i] = new String[]{courseDTO.getCourseName(), courseDTO.fetchFormattedExamDate()};
        }
    }

    @Override
    protected void initializeComponents() {
        examsTable = new JTable(data, columns);
    }

    @Override
    protected void alignComponents() {
        examsTable.setRowHeight(ConfigManager.getInt(configIdentifier, "tableRowHeight"));
        JScrollPane scrollPane = new JScrollPane(examsTable);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);
    }

    @Override
    protected void connectListeners() {
    }
}
