package client.gui.menus.standing.students;

import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.main.MainMenu;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.CourseScoreDTO;
import shareables.network.DTOs.TranscriptDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import javax.swing.*;
import java.util.ArrayList;

public class CurrentStandingView extends PanelTemplate {
    private Student student;
    private ArrayList<CourseScoreDTO> courseScoreDTOs;
    private TranscriptDTO transcriptDTO;
    private JLabel totalGPA;
    private JLabel numberOfPassedCredits;
    private JTable transcriptTable;
    private JScrollPane scrollPane;
    private String[] columns;
    private String[][] data;

    public CurrentStandingView(MainFrame mainFrame, MainMenu mainMenu, User user) {
        super(mainFrame, mainMenu);
        student = (Student) user;
        configIdentifier = ConfigFileIdentifier.GUI_CURRENT_STANDING;
        initializeColumns();
        updateCourseScoreDTOs();
        updateTranscriptDTO();
        setTableData();
        drawPanel();
    }

    private void updateTranscriptDTO() {
        Response response = clientController.getStudentTranscriptDTOWithId(student.getId());
        transcriptDTO = (TranscriptDTO) response.get("transcriptDTO");
    }

    private void updateCourseScoreDTOs() {
        Response response = clientController.getStudentCourseScoreDTOsWithId(student.getId());
        courseScoreDTOs = (ArrayList<CourseScoreDTO>) response.get("courseScoreDTOs");
    }

    private void initializeColumns() {
        columns = new String[2];
        columns[0] = ConfigManager.getString(configIdentifier, "courseNameCol");
        columns[1] = ConfigManager.getString(configIdentifier, "scoreCol");
    }

    private void setTableData() {
        data = new String[courseScoreDTOs.size()][];
        CourseScoreDTO courseScoreDTO;
        for (int i = 0; i < courseScoreDTOs.size(); i++) {
            courseScoreDTO = courseScoreDTOs.get(i);
            data[i] = new String[]{courseScoreDTO.getCourseName(),
                    courseScoreDTO.getScoreString()};
        }
    }

    @Override
    protected void initializeComponents() {
        totalGPA = new JLabel(ConfigManager.getString(configIdentifier, "totalGPA")
                + transcriptDTO.getGPAString());
        numberOfPassedCredits = new JLabel(ConfigManager.getString(configIdentifier, "numberOfPassedCredits")
                + transcriptDTO.getNumberOfPassedCredits());
        transcriptTable = new JTable(data, columns);
    }

    @Override
    protected void alignComponents() {
        transcriptTable.setRowHeight(ConfigManager.getInt(configIdentifier, "transcriptTableRowH"));
        scrollPane = new JScrollPane(transcriptTable);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);

        numberOfPassedCredits.setBounds(ConfigManager.getInt(configIdentifier, "numberOfPassedCreditsX"),
                ConfigManager.getInt(configIdentifier, "numberOfPassedCreditsY"),
                ConfigManager.getInt(configIdentifier, "numberOfPassedCreditsW"),
                ConfigManager.getInt(configIdentifier, "numberOfPassedCreditsH"));
        add(numberOfPassedCredits);
        totalGPA.setBounds(ConfigManager.getInt(configIdentifier, "totalGPAX"),
                ConfigManager.getInt(configIdentifier, "totalGPAY"),
                ConfigManager.getInt(configIdentifier, "totalGPAW"),
                ConfigManager.getInt(configIdentifier, "totalGPAH"));
        add(totalGPA);
    }

    @Override
    protected void connectListeners() {
    }
}
