package client.gui.menus.standing.students;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.CourseScoreDTO;
import shareables.network.DTOs.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class TemporaryStandingView extends DynamicPanelTemplate {
    private Student student;
    private ArrayList<CourseScoreDTO> temporaryCourseScoreDTOs;
    private DefaultTableModel tableModel;
    private JTable scoresTable;
    private JScrollPane scrollPane;
    private String[] columns;
    private String[][] data;
    private ArrayList<JButton> protestButtonsList;

    public TemporaryStandingView(MainFrame mainFrame, MainMenu mainMenu, User user, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        student = (Student) user;
        configIdentifier = ConfigFileIdentifier.GUI_TEMPORARY_STANDING_VIEW;
        initializeColumns();
        updateTemporaryCourseScoreDTOs();
        setTableData();
        protestButtonsList = new ArrayList<>();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    public void updateTable() {
        updateTemporaryCourseScoreDTOs(); // TODO: removing this line here and elsewhere after adding pings
        setTableData();
        tableModel.setDataVector(data, columns);
    }

    private void updateTemporaryCourseScoreDTOs() {
        Response response = clientController.getStudentTemporaryCourseScoreDTOs(offlineModeDTO.getId());
        if (response == null) return;
        temporaryCourseScoreDTOs = (ArrayList<CourseScoreDTO>) response.get("courseScoreDTOs");
    }

    private void initializeColumns() {
        columns = new String[4];
        columns[0] = ConfigManager.getString(configIdentifier, "courseNameCol");
        columns[1] = ConfigManager.getString(configIdentifier, "currentScoreCol");
        columns[2] = ConfigManager.getString(configIdentifier, "protestToScoreCol");
        columns[3] = ConfigManager.getString(configIdentifier, "teachingProfessorResponseCol");
    }

    void setTableData() {
        data = new String[temporaryCourseScoreDTOs.size()][];
        CourseScoreDTO courseScoreDTO;
        for (int i = 0; i < temporaryCourseScoreDTOs.size(); i++) {
            courseScoreDTO = temporaryCourseScoreDTOs.get(i);
            data[i] = new String[]{courseScoreDTO.getCourseName(),
                    String.valueOf(courseScoreDTO.getScore()),
                    courseScoreDTO.getStudentProtest(),
                    courseScoreDTO.getProfessorResponse()};
        }
    }

    @Override
    protected void initializeComponents() {
        tableModel = new DefaultTableModel(data, columns);
        scoresTable = new JTable(tableModel);

        initializeButtons();
    }

    private void initializeButtons() {
        protestButtonsList.clear();
        for (int i = 0; i < temporaryCourseScoreDTOs.size(); i++) {
            JButton button = new JButton(ConfigManager.getString(configIdentifier, "protestButtonM"));
            protestButtonsList.add(button);
        }
    }

    @Override
    protected void alignComponents() {
        scoresTable.setRowHeight(ConfigManager.getInt(configIdentifier, "scoresTableRowH"));
        scrollPane = new JScrollPane(scoresTable);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);

        alignButtons();
    }

    private void alignButtons() {
        int currentX = ConfigManager.getInt(configIdentifier, "startingX");
        int currentY = ConfigManager.getInt(configIdentifier, "startingY");
        int buttonWidth = ConfigManager.getInt(configIdentifier, "buttonW");
        int buttonHeight = ConfigManager.getInt(configIdentifier, "buttonH");
        int incrementOfY = ConfigManager.getInt(configIdentifier, "incY");
        for (JButton protestButton : protestButtonsList) {
            protestButton.setBounds(currentX, currentY, buttonWidth, buttonHeight);
            add(protestButton);
            currentY += buttonHeight + incrementOfY;
        }
    }

    @Override
    protected void connectListeners() {
        JButton protestButton;
        CourseScoreDTO courseScoreDTO;
        for (int i = 0; i < protestButtonsList.size(); i++) {
            protestButton = protestButtonsList.get(i);
            courseScoreDTO = temporaryCourseScoreDTOs.get(i);
            protestButton.addActionListener(new ProtestSubmissionHandler(mainFrame, this,
                    courseScoreDTO, clientController, configIdentifier, offlineModeDTO));
        }
    }

    @Override
    protected void updatePanel() {
        updateTemporaryCourseScoreDTOs();
        setTableData();
        tableModel.setDataVector(data, columns);
        protestButtonsList.forEach(this::remove);
        initializeButtons();
        alignButtons();
        connectListeners();
        repaint();
    }
}
