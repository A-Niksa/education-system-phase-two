package client.gui.menus.standing.professors;
import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import client.gui.utils.BooleanDisplayParser;
import client.gui.utils.ErrorUtils;
import client.locallogic.menus.standing.ScoreFormatUtils;
import client.locallogic.menus.standing.TemporaryScoringChecker;
import shareables.network.DTOs.standing.CourseScoreDTO;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class TemporaryStandingManager extends DynamicPanelTemplate {
    private String[] activeCourseNames;
    private JComboBox<String> coursesBox; // only includes courses active in the current semester
    private String selectedCourseName;
    private JButton viewCourse;
    private DefaultTableModel tableModel;
    private JTable scoresTable;
    private JScrollPane scrollPane;
    private String[] columns;
    private String[][] data;
    private JButton saveTemporaryScores;
    private JButton finalizeAllScores;
    private ArrayList<CourseScoreDTO> courseScoreDTOsForSelectedCourse;
    private HashMap<String, Double> temporaryScoresMap;
    private ArrayList<JButton> addScoreButtonsList;
    private ArrayList<JButton> respondToProtestButtonsList;
    private String noCourseNameSelectedErrorMessage;

    public TemporaryStandingManager(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        configIdentifier = ConfigFileIdentifier.GUI_TEMPORARY_STANDING_MANAGER;
        noCourseNameSelectedErrorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "noCourseHasBeenSelected");
        temporaryScoresMap = new HashMap<>();
        initializeButtonLists();
        initializeColumns();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    private void initializeTable() {
        tableModel = new DefaultTableModel(data, columns);
        scoresTable = new JTable(tableModel);
    }

    private void initializeButtonLists() {
        addScoreButtonsList = new ArrayList<>();
        respondToProtestButtonsList = new ArrayList<>();
    }

    private void initializeColumns() {
        columns = new String[5];
        columns[0] = ConfigManager.getString(configIdentifier, "nameCol");
        columns[1] = ConfigManager.getString(configIdentifier, "currentScoreCol");
        columns[2] = ConfigManager.getString(configIdentifier, "studentProtestCol");
        columns[3] = ConfigManager.getString(configIdentifier, "yourResponseCol");
        columns[4] = ConfigManager.getString(configIdentifier, "finalizedCol");
    }

    private void setInteractiveTableForFirstTime() {
        updateCourseScoreDTOsForSelectedCourse();
        updateTemporaryScoresMap();
        setTableData();
        initializeTable();
        alignTable();
        initializeInteractiveButtons();
        alignInteractiveButtons();
        connectInteractiveListeners();
        repaint();
        validate();
    }

    private void setInteractiveTable() {
        removePreviousButtons();
        updateCourseScoreDTOsForSelectedCourse();
        updateTemporaryScoresMap();
        updateTable();
        initializeInteractiveButtons();
        alignInteractiveButtons();
        connectInteractiveListeners();
        repaint();
        validate();
    }

    private void updateTemporaryScoresMap() {
        temporaryScoresMap.clear();
        courseScoreDTOsForSelectedCourse.forEach(e -> {
            temporaryScoresMap.put(e.getStudentId(), e.getScore());
        });
    }

    private void removePreviousButtons() {
        addScoreButtonsList.forEach(this::remove);
        respondToProtestButtonsList.forEach(this::remove);
    }

    public void updateTable() {
        updateCourseScoreDTOsForSelectedCourse();
        setTableData();
        if (tableModel == null) return;
        tableModel.setDataVector(data, columns);
    }

    private void updateCourseScoreDTOsForSelectedCourse() {
        Response response = clientController.getCourseScoreDTOsForCourse(offlineModeDTO.getDepartmentId(),
                selectedCourseName);
        if (response == null) return;
        courseScoreDTOsForSelectedCourse = (ArrayList<CourseScoreDTO>) response.get("courseScoreDTOs");
    }

    private void updateCourseNames() {
        Response response = clientController.getProfessorActiveCourseNames(offlineModeDTO.getId());
        if (response == null) return;
        activeCourseNames = (String[]) response.get("stringArray");
    }

    void setTableData() {
        if (courseScoreDTOsForSelectedCourse == null) return;

        data = new String[courseScoreDTOsForSelectedCourse.size()][];
        CourseScoreDTO courseScoreDTO;
        Double studentScore;
        for (int i = 0; i < courseScoreDTOsForSelectedCourse.size(); i++) {
            courseScoreDTO = courseScoreDTOsForSelectedCourse.get(i);
            studentScore = temporaryScoresMap.get(courseScoreDTO.getStudentId());
            data[i] = new String[]{courseScoreDTO.getStudentName(),
                    ScoreFormatUtils.getScoreString(studentScore),
                    courseScoreDTO.getStudentProtest(),
                    courseScoreDTO.getProfessorResponse(),
                    BooleanDisplayParser.getBooleanDisplayString(courseScoreDTO.isFinalized())};
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

    private void initializeInteractiveButtons() {
        addScoreButtonsList.clear();
        respondToProtestButtonsList.clear();

        for (int i = 0; i < courseScoreDTOsForSelectedCourse.size(); i++) {
            JButton addScoreButton = new JButton(ConfigManager.getString(configIdentifier, "addScoreButtonM"));
            addScoreButtonsList.add(addScoreButton);
            JButton respondButton = new JButton(ConfigManager.getString(configIdentifier, "respondButtonM"));
            respondToProtestButtonsList.add(respondButton);
        }
    }

    private void alignInteractiveButtons() {
        int currentX = ConfigManager.getInt(configIdentifier, "startingX");
        int currentY = ConfigManager.getInt(configIdentifier, "startingY");
        int buttonWidth = ConfigManager.getInt(configIdentifier, "buttonW");
        int buttonHeight = ConfigManager.getInt(configIdentifier, "buttonH");
        int incrementOfX = ConfigManager.getInt(configIdentifier, "incX");
        int incrementOfY = ConfigManager.getInt(configIdentifier, "incY");
        for (int i = 0; i < courseScoreDTOsForSelectedCourse.size(); i++) {
            JButton addScoreButton = addScoreButtonsList.get(i);
            addScoreButton.setBounds(currentX, currentY, buttonWidth, buttonHeight);
            add(addScoreButton);

            JButton respondButton = respondToProtestButtonsList.get(i);
            respondButton.setBounds(currentX + incrementOfX, currentY, buttonWidth, buttonHeight);
            add(respondButton);

            currentY += buttonHeight + incrementOfY;
        }
    }

    private void connectInteractiveListeners() {
        JButton addScoreButton;
        JButton respondButton;
        CourseScoreDTO courseScoreDTO;
        for (int i = 0; i < courseScoreDTOsForSelectedCourse.size(); i++) {
            addScoreButton = addScoreButtonsList.get(i);
            respondButton = respondToProtestButtonsList.get(i);
            courseScoreDTO = courseScoreDTOsForSelectedCourse.get(i);

            addScoreButton.addActionListener(new ScoreAdditionHandler(mainFrame, this,
                    courseScoreDTO, clientController.getId(), configIdentifier, temporaryScoresMap));
            respondButton.addActionListener(new ProtestResponseHandler(mainFrame, this,
                    courseScoreDTO, clientController, configIdentifier));
        }
    }

    private void initializeCoursesBox() {
        updateCourseNames();
        coursesBox = new JComboBox<>(activeCourseNames);
    }

    @Override
    protected void initializeComponents() {
        initializeCoursesBox();
        viewCourse = new JButton(ConfigManager.getString(configIdentifier, "viewCourseM"));

        saveTemporaryScores = new JButton(ConfigManager.getString(configIdentifier, "saveTemporaryScoresM"));
        finalizeAllScores = new JButton(ConfigManager.getString(configIdentifier, "finalizeAllScoresM"));
    }

    private void alignCoursesBox() {
        coursesBox.setBounds(ConfigManager.getInt(configIdentifier, "coursesBoxX"),
                ConfigManager.getInt(configIdentifier, "coursesBoxY"),
                ConfigManager.getInt(configIdentifier, "coursesBoxW"),
                ConfigManager.getInt(configIdentifier, "coursesBoxH"));
        add(coursesBox);
    }

    @Override
    protected void alignComponents() {
        alignCoursesBox();
        viewCourse.setBounds(ConfigManager.getInt(configIdentifier, "viewCourseX"),
                ConfigManager.getInt(configIdentifier, "viewCourseY"),
                ConfigManager.getInt(configIdentifier, "viewCourseW"),
                ConfigManager.getInt(configIdentifier, "viewCourseH"));
        add(viewCourse);

        saveTemporaryScores.setBounds(ConfigManager.getInt(configIdentifier, "saveTemporaryScoresX"),
                ConfigManager.getInt(configIdentifier, "saveTemporaryScoresY"),
                ConfigManager.getInt(configIdentifier, "saveTemporaryScoresW"),
                ConfigManager.getInt(configIdentifier, "saveTemporaryScoresH"));
        add(saveTemporaryScores);
        finalizeAllScores.setBounds(ConfigManager.getInt(configIdentifier, "finalizeAllScoresX"),
                ConfigManager.getInt(configIdentifier, "finalizeAllScoresY"),
                ConfigManager.getInt(configIdentifier, "finalizeAllScoresW"),
                ConfigManager.getInt(configIdentifier, "finalizeAllScoresH"));
        add(finalizeAllScores);
    }

    @Override
    protected void connectListeners() {
        viewCourse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedCourseName = (String) coursesBox.getSelectedItem();
                if (ErrorUtils.showNoSelectedCourseErrorDialogIfNecessary(mainFrame, selectedCourseName,
                        noCourseNameSelectedErrorMessage)) {
                    MasterLogger.clientError(clientController.getId(), noCourseNameSelectedErrorMessage,
                            "connectListeners", getClass());
                    return;
                }

                if (scoresTable == null) {
                    setInteractiveTableForFirstTime();
                } else {
                    setInteractiveTable();
                }

                MasterLogger.clientInfo(clientController.getId(), "Opened the scoring table of " + selectedCourseName,
                        "connectListeners", getClass());
            }
        });

        saveTemporaryScores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (ErrorUtils.showNoSelectedCourseErrorDialogIfNecessary(mainFrame, selectedCourseName,
                        noCourseNameSelectedErrorMessage)) {
                    MasterLogger.clientError(clientController.getId(), noCourseNameSelectedErrorMessage,
                            "connectListeners", getClass());
                    return;
                }

                if (!TemporaryScoringChecker.allStudentsHaveBeenTemporaryScores(temporaryScoresMap,
                        courseScoreDTOsForSelectedCourse.size())) {
                    String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                            "notAllStudentsHaveTemporaryScores");
                    MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners",
                            getClass());
                    JOptionPane.showMessageDialog(mainFrame, errorMessage);
                    return;
                }

                Response response = clientController.saveTemporaryScores(offlineModeDTO.getDepartmentId(), selectedCourseName,
                        temporaryScoresMap);
                if (response == null) return;
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    MasterLogger.clientInfo(clientController.getId(), "Saved temporary scores of " +
                            selectedCourseName, "connectListeners", getClass());
                }
            }
        });

        finalizeAllScores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (ErrorUtils.showNoSelectedCourseErrorDialogIfNecessary(mainFrame, selectedCourseName,
                        noCourseNameSelectedErrorMessage)) {
                    MasterLogger.clientError(clientController.getId(), noCourseNameSelectedErrorMessage,
                            "connectListeners", getClass());
                    return;
                }

                Response response = clientController.finalizeScores(offlineModeDTO.getDepartmentId(), selectedCourseName);
                if (response == null) return;

                if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                    MasterLogger.clientError(clientController.getId(), response.getErrorMessage(), "connectListeners",
                            getClass());
                } else {
                    MasterLogger.clientInfo(clientController.getId(), "Finalized scores of " + selectedCourseName,
                            "connectListeners", getClass());
                    updateTable();
                }
            }
        });
    }

    private void updateCoursesBox() {
        updateCourseNames();
        coursesBox.setModel(new DefaultComboBoxModel<>(activeCourseNames));
    }

    @Override
    protected void updatePanel() {
        removePreviousButtons();
        updateCourseScoreDTOsForSelectedCourse();
//        updateCoursesBox();
        // TODO: updating courses box here and in master (don't remove the methods though, just in case)
        updateTable();
        initializeInteractiveButtons();
        alignInteractiveButtons();
        connectInteractiveListeners();
        repaint();
        validate();
    }

    // TODO: removing JOptionPane remaining? : if we go offline while the scoring pane is open, it'll remain open
}