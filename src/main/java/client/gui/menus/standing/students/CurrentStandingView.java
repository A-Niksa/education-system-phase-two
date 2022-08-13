package client.gui.menus.standing.students;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.OfflinePanel;
import client.gui.menus.main.MainMenu;
import shareables.network.DTOs.standing.CourseScoreDTO;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.DTOs.standing.TranscriptDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class CurrentStandingView extends DynamicPanelTemplate implements OfflinePanel {
    private ArrayList<CourseScoreDTO> courseScoreDTOs;
    private TranscriptDTO transcriptDTO;
    private JLabel totalGPA;
    private JLabel numberOfPassedCredits;
    private DefaultTableModel tableModel;
    private JTable transcriptTable;
    private JScrollPane scrollPane;
    private String[] columns;
    private String[][] data;
    private String totalGPAMessage;
    private String numberOfPassedCreditsMessage;

    public CurrentStandingView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO,
                               boolean isOnline) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.isOnline = isOnline;
        configIdentifier = ConfigFileIdentifier.GUI_CURRENT_STANDING;
        initializeColumns();
        courseScoreDTOs = (ArrayList<CourseScoreDTO>) offlineModeDTO.getCourseScoreDTOs();
        transcriptDTO = offlineModeDTO.getTranscriptDTO();
        setTableData();
        drawPanel();
        startPingingIfOnline(offlineModeDTO.getId(), this);
    }

    private void updateTranscriptDTO() {
        Response response = clientController.getStudentTranscriptDTOWithId(offlineModeDTO.getId());
        if (response == null) return;
        transcriptDTO = (TranscriptDTO) response.get("transcriptDTO");
    }

    private void updateCourseScoreDTOs() {
        Response response = clientController.getStudentCourseScoreDTOsWithId(offlineModeDTO.getId());
        if (response == null) return;
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
        totalGPAMessage = ConfigManager.getString(configIdentifier, "totalGPA");
        totalGPA = new JLabel(totalGPAMessage + transcriptDTO.getGPAString());
        numberOfPassedCreditsMessage = ConfigManager.getString(configIdentifier, "numberOfPassedCredits");
        numberOfPassedCredits = new JLabel(numberOfPassedCreditsMessage + transcriptDTO.getNumberOfPassedCredits());
        tableModel = new DefaultTableModel(data, columns);
        transcriptTable = new JTable(tableModel);
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

    @Override
    protected void updatePanel() {
        courseScoreDTOs = (ArrayList<CourseScoreDTO>) offlineModeDTO.getCourseScoreDTOs();
        transcriptDTO = offlineModeDTO.getTranscriptDTO();
        // TODO: removing updates
        setTableData();
        tableModel.setDataVector(data, columns);
        totalGPA.setText(totalGPAMessage + transcriptDTO.getGPAString());
        numberOfPassedCredits.setText(numberOfPassedCreditsMessage + transcriptDTO.getNumberOfPassedCredits());
    }

    @Override
    public void disableOnlineComponents() {
        stopPanelLoop();
    }

    @Override
    public void enableOnlineComponents() {
        restartPanelLoop();
    }
}
