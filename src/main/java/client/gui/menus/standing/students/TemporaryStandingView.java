package client.gui.menus.standing.students;

import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.main.MainMenu;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.CourseScoreDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class TemporaryStandingView extends PanelTemplate {
    private Student student;
    private ArrayList<CourseScoreDTO> temporaryCourseScoreDTOs;
    private DefaultTableModel tableModel;
    private JTable scoresTable;
    private JScrollPane scrollPane;
    private String[] columns;
    private String[][] data;
    private ArrayList<JButton> protestButtonsList;

    public TemporaryStandingView(MainFrame mainFrame, MainMenu mainMenu, User user) {
        super(mainFrame, mainMenu);
        student = (Student) user;
        configIdentifier = ConfigFileIdentifier.GUI_TEMPORARY_STANDING_VIEW;
        initializeColumns();
        updateTemporaryCourseScoreDTOs();
        setTableData();
        protestButtonsList = new ArrayList<>();
        drawPanel();
    }

    public void updateTable() {
        updateTemporaryCourseScoreDTOs(); // TODO: removing this line here and elsewhere after adding pings
        setTableData();
        tableModel.setDataVector(data, columns);
    }

    private void updateTemporaryCourseScoreDTOs() {
        Response response = clientController.getStudentTemporaryCourseScoreDTOs(student.getId());
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
            protestButton.addActionListener(new ProtestSubmissionHandler(mainFrame, this, student,
                    courseScoreDTO, clientController, configIdentifier));
        }
    }
}
