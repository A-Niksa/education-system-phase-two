package client.gui.menus.standing.deputies;

import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.main.MainMenu;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.CourseScoreDTO;
import shareables.network.DTOs.TranscriptDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CurrentStandingMaster extends PanelTemplate {
    private enum CurrentMode {
        ID_LOOKUP,
        NAME_LOOKUP
    }

    private Professor professor;
    private String[] studentIds;
    private JComboBox<String> studentIdsBox;
    private JButton searchWithStudentID;
    private String selectedStudentId;
    private String[] studentNames;
    private JComboBox<String> studentNamesBox;
    private JButton searchWithStudentName;
    private String selectedStudentName;
    private ArrayList<CourseScoreDTO> courseScoreDTOs;
    private TranscriptDTO transcriptDTO;
    private DefaultTableModel tableModel;
    private JTable transcriptTable;
    private JScrollPane scrollPane;
    private String[] columns;
    private String[][] data;
    private JButton seeGPAButton;
    private JLabel numberOfPassesCreditsLabel;
    private CurrentMode currentMode;

    public CurrentStandingMaster(MainFrame mainFrame, MainMenu mainMenu, Professor professor) {
        super(mainFrame, mainMenu);
        this.professor = professor;
        configIdentifier = ConfigFileIdentifier.GUI_CURRENT_STANDING_MASTER;
        drawPanel();
    }

    private void setInteractiveTableForFirstTime() {
        updateTranscriptDTO();
        updateCourseScoreDTOs();
        setColumns();
        setTableData();
        tableModel = new DefaultTableModel(data, columns);
        transcriptTable = new JTable(tableModel);
        alignTable();
        setSeeGPAButton();
        setNumberOfPassesCreditsLabel();
        repaint();
        validate();
    }

    private void setInteractiveTable() {
        updateTranscriptDTO();
        updateCourseScoreDTOs();
        setColumns();
        updateTable();
        setSeeGPAButton();
        setNumberOfPassesCreditsLabel();
        repaint();
        validate();
    }

    private void updateTranscriptDTO() {
        Response response;
        if (currentMode == CurrentMode.ID_LOOKUP) {
            response = clientController.getStudentTranscriptDTOWithId(selectedStudentId);
        } else { // in this case, will be NAME_LOOKUP by design
            response = clientController.getStudentTranscriptDTOWithName(professor.getDepartmentId(),
                    selectedStudentName);
        }
        transcriptDTO = (TranscriptDTO) response.get("transcriptDTO");
    }

    private void updateCourseScoreDTOs() {
        Response response;
        if (currentMode == CurrentMode.ID_LOOKUP) {
            response = clientController.getStudentCourseScoreDTOsWithId(selectedStudentId);
        } else { // in this case, will be NAME_LOOKUP by design
            response = clientController.getStudentCourseScoreDTOsWithName(professor.getDepartmentId(), selectedStudentName);
        }
        courseScoreDTOs = (ArrayList<CourseScoreDTO>) response.get("courseScoreDTOs");
    }

    private void setColumns() {
        if (currentMode == CurrentMode.ID_LOOKUP) {
            columns = new String[3];
            columns[0] = ConfigManager.getString(configIdentifier, "nameCol");
            columns[1] = ConfigManager.getString(configIdentifier, "courseNameCol");
            columns[2] = ConfigManager.getString(configIdentifier, "scoreCol");
        } else { // PROFESSOR_VIEW or STUDENT_VIEW by design
            columns = new String[3];
            columns[0] = ConfigManager.getString(configIdentifier, "studentIdCol");
            columns[1] = ConfigManager.getString(configIdentifier, "courseNameCol");
            columns[2] = ConfigManager.getString(configIdentifier, "scoreCol");
        }
    }

    void setTableData() {
//        LinkedList<String> courseScoreDTOs = transcript.getPassedCoursesIDs();
        data = new String[courseScoreDTOs.size()][];
        CourseScoreDTO courseScoreDTO;
        for (int i = 0; i < courseScoreDTOs.size(); i++) {
            courseScoreDTO = courseScoreDTOs.get(i);
            if (currentMode == CurrentMode.ID_LOOKUP) {
                data[i] = new String[]{courseScoreDTO.getStudentName(),
                        courseScoreDTO.getCourseName(),
                        courseScoreDTO.getScoreString()};
            } else { // NAME_LOOKUP by design
                data[i] = new String[]{courseScoreDTO.getStudentId(),
                        courseScoreDTO.getCourseName(),
                        courseScoreDTO.getScoreString()};
            }
        }
    }

    private void alignTable() {
        transcriptTable.setRowHeight(ConfigManager.getInt(configIdentifier, "tableRowH"));
        scrollPane = new JScrollPane(transcriptTable);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);
    }

    private void updateTable() {
        setTableData();
        tableModel.setDataVector(data, columns);
    }

    private void setSeeGPAButton() {
        if (seeGPAButton != null) { // resetting the seeGPAButton if there was a previous seeGPAButton set up (which
            // won't be null)
            remove(seeGPAButton);
        }

        seeGPAButton = new JButton(ConfigManager.getString(configIdentifier, "seeGPAButtonM"));
        seeGPAButton.setBounds(ConfigManager.getInt(configIdentifier, "seeGPAButtonX"),
                ConfigManager.getInt(configIdentifier, "seeGPAButtonY"),
                ConfigManager.getInt(configIdentifier, "seeGPAButtonW"),
                ConfigManager.getInt(configIdentifier, "seeGPAButtonH"));
        add(seeGPAButton);

        seeGPAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String studentsTotalGPA = transcriptDTO.getGPAString();

                JOptionPane.showMessageDialog(mainFrame, ConfigManager.getString(configIdentifier, "totalGPA")
                        + studentsTotalGPA);
                MasterLogger.clientInfo(clientController.getId(), "Viewed the total GPA of the selected student",
                        "setSeeGPAButton", getClass());
            }
        });
    }


    private void setNumberOfPassesCreditsLabel() {
        if (numberOfPassesCreditsLabel != null) { // this condition helps us with finding labels for credits that we
            // need to clear because they belong to other students
            remove(numberOfPassesCreditsLabel);
        }

        numberOfPassesCreditsLabel = new JLabel(ConfigManager.getString(configIdentifier,
                "numberOfPassesCreditsLabelM") + transcriptDTO.getNumberOfPassedCredits());
        numberOfPassesCreditsLabel.setBounds(ConfigManager.getInt(configIdentifier, "numberOfPassesCreditsLabelX"),
                ConfigManager.getInt(configIdentifier, "numberOfPassesCreditsLabelY"),
                ConfigManager.getInt(configIdentifier, "numberOfPassesCreditsLabelW"),
                ConfigManager.getInt(configIdentifier, "numberOfPassesCreditsLabelH"));
        add(numberOfPassesCreditsLabel);
    }

    private void updateDepartmentStudentIds() {
        Response response = clientController.getDepartmentStudentIds(professor.getDepartmentId());
        studentIds = (String[]) response.get("stringArray");
    }

    private void updateDepartmentStudentNames() {
        Response response = clientController.getDepartmentStudentNames(professor.getDepartmentId());
        studentNames = (String[]) response.get("stringArray");
    }

    @Override
    protected void initializeComponents() {
        updateDepartmentStudentIds();
        studentIdsBox = new JComboBox<>(studentIds);
        searchWithStudentID = new JButton(ConfigManager.getString(configIdentifier, "searchButtonM"));

        updateDepartmentStudentNames();
        studentNamesBox = new JComboBox<>(studentNames);
        searchWithStudentName = new JButton(ConfigManager.getString(configIdentifier, "searchButtonM"));
    }

    @Override
    protected void alignComponents() {
        int currentX = ConfigManager.getInt(configIdentifier, "startingX");
        int currentY = ConfigManager.getInt(configIdentifier, "startingY");
        int incrementOfX = ConfigManager.getInt(configIdentifier, "incX");
        int smallerIncrementOfX = ConfigManager.getInt(configIdentifier, "smallerIncX");
        int componentWidth = ConfigManager.getInt(configIdentifier, "componentW");
        int smallerComponentWidth = ConfigManager.getInt(configIdentifier, "smallerComponentW");
        int componentHeight = ConfigManager.getInt(configIdentifier, "componentH");
        studentIdsBox.setBounds(currentX, currentY, componentWidth, componentHeight);
        add(studentIdsBox);
        searchWithStudentID.setBounds(currentX + smallerIncrementOfX, currentY, smallerComponentWidth, componentHeight);
        add(searchWithStudentID);
        currentX += incrementOfX;

        studentNamesBox.setBounds(currentX, currentY, componentWidth, componentHeight);
        add(studentNamesBox);
        searchWithStudentName.setBounds(currentX + smallerIncrementOfX, currentY, smallerComponentWidth, componentHeight);
        add(searchWithStudentName);
    }

    @Override
    protected void connectListeners() {
        searchWithStudentID.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedStudentId = (String) studentIdsBox.getSelectedItem();
                currentMode = CurrentMode.ID_LOOKUP;

                checkIfFirstTimeAndSetScoresTable();

                MasterLogger.clientInfo(clientController.getId(), "Opened the transcript of student (ID: " +
                        selectedStudentId + ")", "connectListeners", getClass());
            }
        });

        searchWithStudentName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedStudentName = (String) studentNamesBox.getSelectedItem();
                currentMode = CurrentMode.NAME_LOOKUP;

                checkIfFirstTimeAndSetScoresTable();

                MasterLogger.clientInfo(clientController.getId(), "Opened the transcript of student (name: " +
                        selectedStudentName + ")", "connectListeners", getClass());
            }
        });
    }

    private void checkIfFirstTimeAndSetScoresTable() {
        if (transcriptTable == null) {
            setInteractiveTableForFirstTime();
        } else {
            setInteractiveTable();
        }
    }
}
