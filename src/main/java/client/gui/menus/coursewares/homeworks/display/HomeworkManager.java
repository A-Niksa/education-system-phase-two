package client.gui.menus.coursewares.homeworks.display;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.coursewares.homeworks.listview.ProfessorHomeworksView;
import client.gui.menus.coursewares.homeworks.listview.StudentHomeworksView;
import client.gui.menus.main.MainMenu;
import client.locallogic.menus.messaging.DownloadManager;
import client.locallogic.menus.messaging.ThumbnailParser;
import client.locallogic.menus.standing.ScoreFormatUtils;
import shareables.models.pojos.coursewares.SubmissionType;
import shareables.models.pojos.media.MediaFile;
import shareables.models.pojos.users.UserIdentifier;
import shareables.network.DTOs.coursewares.SubmissionThumbnailDTO;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class HomeworkManager extends DynamicPanelTemplate {
    private String courseId;
    private String homeworkId;
    private SubmissionType submissionType;
    private boolean isTeachingAssistant;
    private DownloadManager downloadManager;
    private String[] submissionThumbnailTexts;
    private ArrayList<SubmissionThumbnailDTO> submissionThumbnailDTOs;
    protected DefaultListModel<String> listModel;
    protected JList<String> graphicalList;
    protected JScrollPane scrollPane;
    private JButton viewButton;
    private JButton scoreButton;
    private JButton goBackButton;

    public HomeworkManager(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String courseId,
                           String homeworkId) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.courseId = courseId;
        this.homeworkId = homeworkId;
        configIdentifier = ConfigFileIdentifier.GUI_HOMEWORK_MANAGER;
        initializeSubmissionType();
        initializeTeachingAssistanceStatus();
        downloadManager = new DownloadManager();
        updateSubmissionThumbnailDTOs();
        updateSubmissionThumbnailTexts();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    private void updateSubmissionThumbnailDTOs() {
        Response response = clientController.getSubmissionThumbnailDTOs(courseId, homeworkId);
        if (response == null) return;
        submissionThumbnailDTOs = (ArrayList<SubmissionThumbnailDTO>) response.get("submissionThumbnailDTOs");
    }

    private void updateSubmissionThumbnailTexts() {
        if (submissionThumbnailDTOs == null) return;

        submissionThumbnailTexts = new String[submissionThumbnailDTOs.size()];
        for (int i = 0; i < submissionThumbnailDTOs.size(); i++) {
            if (isTeachingAssistant) {
                submissionThumbnailTexts[i] = submissionThumbnailDTOs.get(i).toStringForTA();
            } else {
                submissionThumbnailTexts[i] = submissionThumbnailDTOs.get(i).toString();
            }
        }
    }

    private void initializeSubmissionType() {
        Response response = clientController.getHomeworkSubmissionType(courseId, homeworkId);
        if (response == null) return;
        submissionType = (SubmissionType) response.get("submissionType");
    }


    private void initializeTeachingAssistanceStatus() {
        if (offlineModeDTO.getUserIdentifier() == UserIdentifier.PROFESSOR) {
            isTeachingAssistant = false;
        } else {
            Response response = clientController.getTeachingAssistanceStatus(courseId, offlineModeDTO.getId());
            if (response == null) return;
            isTeachingAssistant = (boolean) response.get("isTeachingAssistant");
        }
    }

    @Override
    protected void updatePanel() {
        updateSubmissionThumbnailDTOs();
        String[] previousSubmissionThumbnailTexts = Arrays.copyOf(submissionThumbnailTexts,
                submissionThumbnailTexts.length);
        updateSubmissionThumbnailTexts();
        Arrays.stream(previousSubmissionThumbnailTexts)
                .filter(e -> !arrayContains(submissionThumbnailTexts, e))
                .forEach(e -> listModel.removeElement(e));
        Arrays.stream(submissionThumbnailTexts)
                .filter(e -> !arrayContains(previousSubmissionThumbnailTexts, e))
                .forEach(e -> listModel.add(0, e));
    }

    private boolean arrayContains(String[] array, String targetElement) {
        return Arrays.stream(array).anyMatch(e -> e.equals(targetElement));
    }

    @Override
    protected void initializeComponents() {
        listModel = new DefaultListModel<>();
        Arrays.stream(submissionThumbnailTexts).forEach(e -> listModel.addElement(e));
        graphicalList = new JList<>(listModel);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(graphicalList);

        viewButton = new JButton(ConfigManager.getString(configIdentifier, "viewButtonM"));
        scoreButton = new JButton(ConfigManager.getString(configIdentifier, "scoreButtonM"));
        goBackButton = new JButton(ConfigManager.getString(configIdentifier, "goBackButtonM"));
    }

    @Override
    protected void alignComponents() {
        graphicalList.setLayoutOrientation(JList.VERTICAL);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);

        viewButton.setBounds(ConfigManager.getInt(configIdentifier, "viewButtonX"),
                ConfigManager.getInt(configIdentifier, "viewButtonY"),
                ConfigManager.getInt(configIdentifier, "viewButtonW"),
                ConfigManager.getInt(configIdentifier, "viewButtonH"));
        add(viewButton);
        scoreButton.setBounds(ConfigManager.getInt(configIdentifier, "scoreButtonX"),
                ConfigManager.getInt(configIdentifier, "scoreButtonY"),
                ConfigManager.getInt(configIdentifier, "scoreButtonW"),
                ConfigManager.getInt(configIdentifier, "scoreButtonH"));
        add(scoreButton);
        goBackButton.setBounds(ConfigManager.getInt(configIdentifier, "goBackButtonX"),
                ConfigManager.getInt(configIdentifier, "goBackButtonY"),
                ConfigManager.getInt(configIdentifier, "goBackButtonW"),
                ConfigManager.getInt(configIdentifier, "goBackButtonH"));
        add(goBackButton);
    }

    @Override
    protected void connectListeners() {
        viewButton.addActionListener(actionEvent -> {
            if (graphicalList.getSelectedIndex() == -1) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                        "noSubmissionHasBeenSelected");
                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners", getClass());
                return;
            }

            String selectedListItem = graphicalList.getSelectedValue();
            String selectedSubmissionId = ThumbnailParser.getIdFromThumbnailText(selectedListItem, " - ");

            if (submissionType == SubmissionType.TEXT) {
                showTextSubmission(selectedSubmissionId);
            } else if (submissionType == SubmissionType.MEDIA_FILE) {
                showMediaSubmissionDownloadLocation(selectedSubmissionId);
            }
        });

        scoreButton.addActionListener(actionEvent -> {
            if (graphicalList.getSelectedIndex() == -1) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                        "noSubmissionHasBeenSelected");
                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners", getClass());
                return;
            }

            String selectedListItem = graphicalList.getSelectedValue();
            String selectedSubmissionId = ThumbnailParser.getIdFromThumbnailText(selectedListItem, " - ");

            MasterLogger.clientInfo(clientController.getId(), "Opened the scoring option panel",
                    "connectListeners", getClass());

            String scoreText = JOptionPane.showInputDialog(mainFrame,
                    ConfigManager.getString(ConfigFileIdentifier.TEXTS, "scoreTextPrompt"));
            if (scoreText == null) {
                return;
            }

            Double score;
            try {
                score = Double.valueOf(scoreText);
            } catch (NumberFormatException e) {
                String fatalMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                        "wrongDoubleNumberFormat");
                MasterLogger.clientFatal(clientController.getId(), fatalMessage, "connectListeners",
                        getClass());
                JOptionPane.showMessageDialog(mainFrame, fatalMessage);
                return;
            }

            if (!ScoreFormatUtils.isInValidRange(score)) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS, "invalidScoreRange");
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners", getClass());
                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                return;
            }

            Double roundedScore = ScoreFormatUtils.roundToTheNearestQuarter(score);

            Response response = clientController.scoreStudentSubmission(courseId, homeworkId, selectedSubmissionId,
                    roundedScore);

            if (response.getResponseStatus() == ResponseStatus.OK) {
                MasterLogger.clientInfo(clientController.getId(), "Added score to student submission",
                        "actionPerformed", getClass());
            }
        });

        goBackButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Went back to homeworks view",
                    "connectListeners", getClass());
            stopPanelLoop();
            if (offlineModeDTO.getUserIdentifier() == UserIdentifier.PROFESSOR) {
                mainFrame.setCurrentPanel(new ProfessorHomeworksView(mainFrame, mainMenu, offlineModeDTO, courseId));
            } else if (offlineModeDTO.getUserIdentifier() == UserIdentifier.STUDENT) {
                mainFrame.setCurrentPanel(new StudentHomeworksView(mainFrame, mainMenu, offlineModeDTO, courseId));
            }
        });
    }

    private void showMediaSubmissionDownloadLocation(String selectedSubmissionId) {
        Response response = clientController.downloadSubmissionMediaFile(courseId, homeworkId, selectedSubmissionId);
        if (response == null) return;

        if (response.getResponseStatus() == ResponseStatus.OK) {
            String downloadedFilePrompt = downloadManager.saveMediaToLocalDownloads((MediaFile) response.get("mediaFile"));
            JOptionPane.showMessageDialog(mainFrame, downloadedFilePrompt);
            MasterLogger.clientInfo(clientController.getId(), downloadedFilePrompt, "connectListeners",
                    getClass());
        }
    }

    private void showTextSubmission(String selectedSubmissionId) {
        Response response = clientController.getSubmissionText(courseId, homeworkId, selectedSubmissionId);
        if (response == null) return;

        if (response.getResponseStatus() == ResponseStatus.OK) {
            String submissionText = (String) response.get("submissionText");

            JTextArea textArea = new JTextArea(submissionText);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);

            JOptionPane.showMessageDialog(mainFrame, textArea);
        }
    }
}
