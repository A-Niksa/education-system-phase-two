package client.gui.menus.coursewares.homeworks.display;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.coursewares.homeworks.listview.StudentHomeworksView;
import client.gui.menus.main.MainMenu;
import client.locallogic.menus.coursewares.ScoreFormattingUtils;
import client.locallogic.menus.messaging.DownloadManager;
import client.locallogic.menus.messaging.MediaFileParser;
import shareables.models.pojos.coursewares.SubmissionType;
import shareables.models.pojos.media.MediaFile;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class HomeworkDisplay extends DynamicPanelTemplate {
    private String courseId;
    private String homeworkId;
    private SubmissionType submissionType;
    private JButton seeHomeworkDescriptionButton;
    private JButton downloadHomeworkPDFButton;
    private JButton goBackButton;
    private JButton sendTextButton;
    private JButton sendFileButton;
    private JButton chooseFileButton;
    private JTextField textField;
    private JLabel chosenFileLabel;
    private JLabel fileChoosingBackground;
    private File chosenFile;
    private String chosenFileLabelMessage;
    private Double score;
    private JLabel scoreLabel;
    private MediaFileParser mediaFileParser;
    private DownloadManager downloadManager;
    private String scoreLabelMessage;

    public HomeworkDisplay(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String courseId,
                           String homeworkId) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.courseId = courseId;
        this.homeworkId = homeworkId;
        configIdentifier = ConfigFileIdentifier.GUI_HOMEWORK_DISPLAY;
        initializeSubmissionType();
        updateScore();
        mediaFileParser = new MediaFileParser();
        downloadManager = new DownloadManager();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    private void initializeSubmissionType() {
        Response response = clientController.getHomeworkSubmissionType(courseId, homeworkId);
        if (response == null) return;
        submissionType = (SubmissionType) response.get("submissionType");
    }

    @Override
    protected void updatePanel() {
        updateScore();
        scoreLabel.setText(scoreLabelMessage + ScoreFormattingUtils.getScoreString(score));
    }

    private void updateScore() {
        Response response = clientController.getStudentHomeworkScore(courseId, homeworkId, offlineModeDTO.getId());
        if (response == null) return;
        score = (Double) response.get("score");
    }

    @Override
    protected void initializeComponents() {
        sendTextButton = new JButton(ConfigManager.getString(configIdentifier, "sendTextButtonM"));
        sendFileButton = new JButton(ConfigManager.getString(configIdentifier, "sendFileButtonM"));
        chooseFileButton = new JButton(ConfigManager.getString(configIdentifier, "chooseFileButtonM"));
        textField = new JTextField(ConfigManager.getString(configIdentifier, "textFieldM"));
        chosenFileLabelMessage = ConfigManager.getString(configIdentifier, "chosenFileLabelM");
        chosenFileLabel = new JLabel(chosenFileLabelMessage);
        fileChoosingBackground = new JLabel();

        seeHomeworkDescriptionButton = new JButton(ConfigManager.getString(configIdentifier,
                "seeHomeworkDescriptionButtonM"));
        downloadHomeworkPDFButton = new JButton(ConfigManager.getString(configIdentifier,
                "downloadHomeworkPDFButtonM"));
        goBackButton = new JButton(ConfigManager.getString(configIdentifier, "goBackButtonM"));

        scoreLabelMessage = ConfigManager.getString(configIdentifier, "scoreLabelM");
        scoreLabel = new JLabel(scoreLabelMessage + ScoreFormattingUtils.getScoreString(score), SwingConstants.CENTER);
    }

    @Override
    protected void alignComponents() {
        seeHomeworkDescriptionButton.setBounds(ConfigManager.getInt(configIdentifier, "seeHomeworkDescriptionButtonX"),
                ConfigManager.getInt(configIdentifier, "seeHomeworkDescriptionButtonY"),
                ConfigManager.getInt(configIdentifier, "seeHomeworkDescriptionButtonW"),
                ConfigManager.getInt(configIdentifier, "seeHomeworkDescriptionButtonH"));
        add(seeHomeworkDescriptionButton);
        downloadHomeworkPDFButton.setBounds(ConfigManager.getInt(configIdentifier, "downloadHomeworkPDFButtonX"),
                ConfigManager.getInt(configIdentifier, "downloadHomeworkPDFButtonY"),
                ConfigManager.getInt(configIdentifier, "downloadHomeworkPDFButtonW"),
                ConfigManager.getInt(configIdentifier, "downloadHomeworkPDFButtonH"));
        add(downloadHomeworkPDFButton);
        goBackButton.setBounds(ConfigManager.getInt(configIdentifier, "goBackButtonX"),
                ConfigManager.getInt(configIdentifier, "goBackButtonY"),
                ConfigManager.getInt(configIdentifier, "goBackButtonW"),
                ConfigManager.getInt(configIdentifier, "goBackButtonH"));
        add(goBackButton);

        scoreLabel.setBounds(ConfigManager.getInt(configIdentifier, "scoreLabelX"),
                ConfigManager.getInt(configIdentifier, "scoreLabelY"),
                ConfigManager.getInt(configIdentifier, "scoreLabelW"),
                ConfigManager.getInt(configIdentifier, "scoreLabelH"));
        add(scoreLabel);

        sendTextButton.setBounds(ConfigManager.getInt(configIdentifier, "sendTextButtonX"),
                ConfigManager.getInt(configIdentifier, "sendTextButtonY"),
                ConfigManager.getInt(configIdentifier, "sendTextButtonW"),
                ConfigManager.getInt(configIdentifier, "sendTextButtonH"));
        sendFileButton.setBounds(ConfigManager.getInt(configIdentifier, "sendFileButtonX"),
                ConfigManager.getInt(configIdentifier, "sendFileButtonY"),
                ConfigManager.getInt(configIdentifier, "sendFileButtonW"),
                ConfigManager.getInt(configIdentifier, "sendFileButtonH"));
        chooseFileButton.setBounds(ConfigManager.getInt(configIdentifier, "chooseFileButtonX"),
                ConfigManager.getInt(configIdentifier, "chooseFileButtonY"),
                ConfigManager.getInt(configIdentifier, "chooseFileButtonW"),
                ConfigManager.getInt(configIdentifier, "chooseFileButtonH"));
        textField.setBounds(ConfigManager.getInt(configIdentifier, "textFieldX"),
                ConfigManager.getInt(configIdentifier, "textFieldY"),
                ConfigManager.getInt(configIdentifier, "textFieldW"),
                ConfigManager.getInt(configIdentifier, "textFieldH"));
        chosenFileLabel.setBounds(ConfigManager.getInt(configIdentifier, "chosenFileLabelX"),
                ConfigManager.getInt(configIdentifier, "chosenFileLabelY"),
                ConfigManager.getInt(configIdentifier, "chosenFileLabelW"),
                ConfigManager.getInt(configIdentifier, "chosenFileLabelH"));
        fileChoosingBackground.setOpaque(true);
        fileChoosingBackground.setBackground(Color.WHITE);
        fileChoosingBackground.setBounds(ConfigManager.getInt(configIdentifier, "fileChoosingBackgroundX"),
                ConfigManager.getInt(configIdentifier, "fileChoosingBackgroundY"),
                ConfigManager.getInt(configIdentifier, "fileChoosingBackgroundW"),
                ConfigManager.getInt(configIdentifier, "fileChoosingBackgroundH"));

        if (submissionType == SubmissionType.TEXT) {
            add(textField);
            add(sendTextButton);
        } else if (submissionType == SubmissionType.MEDIA_FILE) {
            add(chooseFileButton);
            add(chosenFileLabel);
            add(sendFileButton);
            add(fileChoosingBackground);
        }
    }

    @Override
    protected void connectListeners() {
        sendTextButton.addActionListener(actionEvent -> {
            Response response = clientController.submitHomeworkText(offlineModeDTO.getId(), courseId, homeworkId,
                    textField.getText());
            if (response == null) return;

            if (response.getResponseStatus() == ResponseStatus.OK) {
                MasterLogger.clientInfo(clientController.getId(), "Text homework submitted",
                        "connectListeners", getClass());
            }
        });

        chooseFileButton.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));

            int returnedValue = fileChooser.showOpenDialog(mainFrame);
            if (returnedValue == JFileChooser.APPROVE_OPTION) {
                chosenFile = fileChooser.getSelectedFile();
                chosenFileLabel.setText(chosenFileLabelMessage + chosenFile.getPath());
                MasterLogger.clientInfo(clientController.getId(), "Media file chosen (ID: " + chosenFile.getPath()
                        + ")", "connectListeners", getClass());
            }
        });

        sendFileButton.addActionListener(actionEvent -> {
            if (mediaFileParser.isFileTooLarge(chosenFile)) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS, "fileTooLarge");

                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners",
                        getClass());

                return;
            }

            if (chosenFile == null) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS, "noFileHasBeenChosen");

                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners",
                        getClass());

                return;
            }

            MediaFile convertedMediaFile = mediaFileParser.convertFileToMediaFile(chosenFile);
            if (convertedMediaFile == null) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS, "fileNotSupported");

                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners",
                        getClass());

                return;
            }

            Response response = clientController.submitHomeworkMedia(offlineModeDTO.getId(), courseId, homeworkId,
                    convertedMediaFile);
            if (response.getResponseStatus() == ResponseStatus.OK) {
                String successPrompt = ConfigManager.getString(ConfigFileIdentifier.TEXTS, "mediaFileSent");

                JOptionPane.showMessageDialog(mainFrame, successPrompt);
                MasterLogger.clientInfo(clientController.getId(), successPrompt, "connectListeners", getClass());

                chosenFile = null;
                chosenFileLabel.setText(chosenFileLabelMessage);
            }
        });

        seeHomeworkDescriptionButton.addActionListener(actionEvent -> {
            Response response = clientController.getHomeworkDescription(courseId, homeworkId);
            if (response == null) return;

            if (response.getResponseStatus() == ResponseStatus.OK) {
                String description = (String) response.get("description");
                if (description == null) description = "";
                JTextArea descriptionTextArea = new JTextArea(description);
                descriptionTextArea.setLineWrap(true);
                descriptionTextArea.setWrapStyleWord(true);
                descriptionTextArea.setEditable(false);

                JOptionPane.showMessageDialog(mainFrame, descriptionTextArea);
            }
        });

        downloadHomeworkPDFButton.addActionListener(actionEvent -> {
            Response response = clientController.getHomeworkPDF(courseId, homeworkId);
            if (response == null) return;

            MediaFile homeworkPDF = (MediaFile) response.get("mediaFile");
            if (homeworkPDF == null) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS, "noHomeworkPDF");
                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners", getClass());
                return;
            }

            if (response.getResponseStatus() == ResponseStatus.OK) {
                String downloadedFilePrompt = downloadManager.saveMediaToLocalDownloads(homeworkPDF);
                JOptionPane.showMessageDialog(mainFrame, downloadedFilePrompt);
                MasterLogger.clientInfo(clientController.getId(), downloadedFilePrompt, "connectListeners",
                        getClass());
            }
        });

        goBackButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Went back to homeworks view",
                    "connectListeners", getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(new StudentHomeworksView(mainFrame, mainMenu, offlineModeDTO, courseId));
        });
    }
}