package client.gui.menus.standing.deputies;

import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.main.MainMenu;
import client.gui.utils.BooleanDisplayParser;
import client.locallogic.profile.DepartmentGetter;
import client.locallogic.standing.ScoreFormatUtils;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.CourseScoreDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TemporaryStandingMaster extends PanelTemplate {
    private enum CurrentMode {
        COURSE_VIEW,
        PROFESSOR_VIEW,
        STUDENT_VIEW
    }

    private Professor professor;
    private String departmentNameString;
    private String[] courseNames;
    private JComboBox<String> coursesBox;
    private String selectedCourseName;
    private JButton viewCourse;
    private String[] professorNames;
    private JComboBox<String> professorsBox;
    private String selectedProfessorName;
    private JButton viewProfessor;
    private String[] studentIds;
    private JComboBox<String> studentsBox;
    private String selectedStudentId;
    private JButton viewStudent;
    private DefaultTableModel tableModel;
    private JTable scoresTable;
    private JScrollPane scrollPane;
    private String[] columns;
    private String[][] data;
    private ArrayList<CourseScoreDTO> courseScoreDTOs;
    private JButton statsButton;
    private CurrentMode currentMode;

    public TemporaryStandingMaster(MainFrame mainFrame, MainMenu mainMenu, Professor professor) {
        super(mainFrame, mainMenu);
        this.professor = professor;
        departmentNameString = DepartmentGetter.getDepartmentNameById(professor.getDepartmentId()).toString();
        configIdentifier = ConfigFileIdentifier.GUI_TEMPORARY_STANDING_MASTER;
        drawPanel();
    }

    private void setInteractiveTableForFirstTime() {
        updateCourseScoreDTOs();
        setColumns();
        setTableData();
        tableModel = new DefaultTableModel(data, columns);
        scoresTable = new JTable(tableModel);
        alignTable();
        setStatsButton();
        repaint();
        validate();
    }

    private void setInteractiveTable() {
        updateCourseScoreDTOs();
        setColumns();
        updateTable();
        setStatsButton();
        repaint();
        validate();
    }

    private void setColumns() {
        if (currentMode == CurrentMode.COURSE_VIEW) {
            columns = new String[5];
            columns[0] = ConfigManager.getString(configIdentifier, "nameCol");
            columns[1] = ConfigManager.getString(configIdentifier, "currentScoreCol");
            columns[2] = ConfigManager.getString(configIdentifier, "studentProtestCol");
            columns[3] = ConfigManager.getString(configIdentifier, "teachingProfessorResponseCol");
            columns[4] = ConfigManager.getString(configIdentifier, "finalizedCol");
        } else { // PROFESSOR_VIEW or STUDENT_VIEW by design
            columns = new String[6];
            columns[0] = ConfigManager.getString(configIdentifier, "subjectCol");
            columns[1] = ConfigManager.getString(configIdentifier, "nameCol");
            columns[2] = ConfigManager.getString(configIdentifier, "currentScoreCol");
            columns[3] = ConfigManager.getString(configIdentifier, "studentProtestCol");
            columns[4] = ConfigManager.getString(configIdentifier, "teachingProfessorResponseCol");
            columns[5] = ConfigManager.getString(configIdentifier, "finalizedCol");
        }
    }

    private void updateTable() {
        setTableData();
        tableModel.setDataVector(data, columns);
    }

    private void updateCourseScoreDTOs() {
        Response response;
        switch (currentMode) {
            case COURSE_VIEW:
                response = clientController.getCourseScoreDTOsForCourse(professor.getDepartmentId(),
                        selectedCourseName);
                courseScoreDTOs = (ArrayList<CourseScoreDTO>) response.get("courseScoreDTOs");
                break;
            case PROFESSOR_VIEW:
                response = clientController.getCourseScoreDTOsForProfessor(professor.getDepartmentId(),
                        selectedProfessorName);
                courseScoreDTOs = (ArrayList<CourseScoreDTO>) response.get("courseScoreDTOs");
                break;
            case STUDENT_VIEW:
                response = clientController.getCourseScoreDTOsForStudent(professor.getDepartmentId(), selectedStudentId);
                courseScoreDTOs = (ArrayList<CourseScoreDTO>) response.get("courseScoreDTOs");
                break;
        }
    }

    void setTableData() {
        data = new String[courseScoreDTOs.size()][];
        CourseScoreDTO courseScoreDTO;
        Double studentScore;
        for (int i = 0; i < courseScoreDTOs.size(); i++) {
            courseScoreDTO = courseScoreDTOs.get(i);
            studentScore = courseScoreDTO.getScore();
            if (currentMode == CurrentMode.COURSE_VIEW) {
                data[i] = new String[]{courseScoreDTO.getStudentName(),
                        ScoreFormatUtils.getScoreString(studentScore),
                        courseScoreDTO.getStudentProtest(),
                        courseScoreDTO.getProfessorResponse(),
                        BooleanDisplayParser.getBooleanDisplayString(courseScoreDTO.isFinalized())};
            } else { // PROFESSOR_VIEW or STUDENT_VIEW by design
                data[i] = new String[]{courseScoreDTO.getCourseName(),
                        courseScoreDTO.getStudentName(),
                        ScoreFormatUtils.getScoreString(studentScore),
                        courseScoreDTO.getStudentProtest(),
                        courseScoreDTO.getProfessorResponse(),
                        BooleanDisplayParser.getBooleanDisplayString(courseScoreDTO.isFinalized())};
            }
        }
    }

    private void alignTable() {
        scoresTable.setRowHeight(ConfigManager.getInt(configIdentifier, "scoresTableRowH"));
        scrollPane = new JScrollPane(scoresTable);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);
    }

    private void setStatsButton() {
        if (currentMode == CurrentMode.COURSE_VIEW) {
            if (statsButton != null) { /* resetting the statsButton if there was a previous statsButton set up (which
                won't be null) */
                remove(statsButton);
            }

            statsButton = new JButton(ConfigManager.getString(configIdentifier, "statsButtonM"));
            statsButton.setBounds(ConfigManager.getInt(configIdentifier, "statsButtonX"),
                    ConfigManager.getInt(configIdentifier, "statsButtonY"),
                    ConfigManager.getInt(configIdentifier, "statsButtonW"),
                    ConfigManager.getInt(configIdentifier, "statsButtonH"));
            statsButton.addActionListener(new StatsViewHandler(mainFrame, this, selectedCourseName,
                    departmentNameString, clientController, configIdentifier));
            add(statsButton);
        } else if (statsButton != null) {
            remove(statsButton);
        }
    }

    @Override
    protected void initializeComponents() {
        updateDepartmentCourseNames();
        coursesBox = new JComboBox<>(courseNames);
        viewCourse = new JButton(ConfigManager.getString(configIdentifier, "viewCourseM"));

        updateDepartmentProfessorNames();
        professorsBox = new JComboBox<>(professorNames);
        viewProfessor = new JButton(ConfigManager.getString(configIdentifier, "viewProfessorM"));

        updateDepartmentStudentIds();
        studentsBox = new JComboBox<>(studentIds);
        viewStudent = new JButton(ConfigManager.getString(configIdentifier, "viewStudentM"));
    }

    private void updateDepartmentStudentIds() {
        Response response = clientController.getDepartmentStudentIds(professor.getDepartmentId());
        studentIds = (String[]) response.get("stringArray");
    }

    private void updateDepartmentProfessorNames() {
        Response response = clientController.getDepartmentProfessorNames(professor.getDepartmentId());
        professorNames = (String[]) response.get("stringArray");
    }

    private void updateDepartmentCourseNames() {
        Response response = clientController.getDepartmentCourseNames(professor.getDepartmentId());
        courseNames = (String[]) response.get("stringArray");
    }

    @Override
    protected void alignComponents() {
        int currentX = ConfigManager.getInt(configIdentifier, "startingX");
        int currentY = ConfigManager.getInt(configIdentifier, "startingY");
        int incrementOfX = ConfigManager.getInt(configIdentifier, "incX");
        int smallerIncrementOfX = ConfigManager.getInt(configIdentifier, "smallerIncX");
        int smallerComponentWidth = ConfigManager.getInt(configIdentifier, "smallerComponentW");
        int componentWidth = ConfigManager.getInt(configIdentifier, "componentW");
        int componentHeight = ConfigManager.getInt(configIdentifier, "componentH");
        coursesBox.setBounds(currentX, currentY, componentWidth, componentHeight);
        add(coursesBox);
        viewCourse.setBounds(currentX + smallerIncrementOfX, currentY, smallerComponentWidth, componentHeight);
        add(viewCourse);
        currentX += incrementOfX;

        professorsBox.setBounds(currentX, currentY, componentWidth, componentHeight);
        add(professorsBox);
        viewProfessor.setBounds(currentX + smallerIncrementOfX, currentY, smallerComponentWidth, componentHeight);
        add(viewProfessor);
        currentX += incrementOfX;

        studentsBox.setBounds(currentX, currentY, componentWidth, componentHeight);
        add(studentsBox);
        viewStudent.setBounds(currentX + smallerIncrementOfX, currentY, smallerComponentWidth, componentHeight);
        add(viewStudent);
    }

    @Override
    protected void connectListeners() {
        viewCourse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedCourseName = (String) coursesBox.getSelectedItem();
                currentMode = CurrentMode.COURSE_VIEW;

                checkIfFirstTimeAndSetScoresTable();

                MasterLogger.clientInfo(clientController.getId(), "Deputy opened the scores tables of " +
                        selectedCourseName, "connectListeners", getClass());
            }
        });

        viewProfessor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedProfessorName = (String) professorsBox.getSelectedItem();
                currentMode = CurrentMode.PROFESSOR_VIEW;

                checkIfFirstTimeAndSetScoresTable();

                MasterLogger.clientInfo(clientController.getId(), "deputy opened the scores table of professor "
                                + selectedProfessorName, "connectListeners", getClass());
            }
        });

        viewStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedStudentId = (String) studentsBox.getSelectedItem();
                currentMode = CurrentMode.STUDENT_VIEW;

                checkIfFirstTimeAndSetScoresTable();

                MasterLogger.clientInfo(clientController.getId(), "Deputy opened the scores table of student with ID: "
                                + selectedStudentId, "connectListeners", getClass());
            }
        });
    }

    private void checkIfFirstTimeAndSetScoresTable() {
        if (scoresTable == null) {
            setInteractiveTableForFirstTime();
        } else {
            setInteractiveTable();
        }
    }
}