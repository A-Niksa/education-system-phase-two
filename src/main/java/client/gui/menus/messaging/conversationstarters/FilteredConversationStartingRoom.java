package client.gui.menus.messaging.conversationstarters;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import client.gui.utils.EnumArrayUtils;
import client.gui.utils.EnumStringMappingUtils;
import client.locallogic.messaging.MediaFileParser;
import shareables.models.pojos.media.MediaFile;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.models.pojos.users.students.StudentStatus;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class FilteredConversationStartingRoom extends DynamicPanelTemplate {
    private JButton goBackButton;
    private JButton sendMessageButton;
    private JButton sendFileButton;
    private JButton chooseFileButton;
    private JTextField messageField;
    private JLabel chosenFileLabel;
    private JLabel fileChoosingBackground;
    private JTextField yearOfEntryFilter;
    private String[] degreeLevels;
    private JComboBox<String> degreeLevelFilter;
    private String[] studentStatusStringsWithoutDropouts;
    private JComboBox<String> studentStatusFilter;
    private File chosenFile;
    private String chosenFileLabelMessage;
    private MediaFileParser mediaFileParser;
    private ArrayList<String> allContactIds;
    private ArrayList<String> filteredContactIds;
    int selectedYearOfEntry;
    DegreeLevel selectedDegreeLevel;
    StudentStatus selectedStudentStatus;

    public FilteredConversationStartingRoom(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO,
                                            ArrayList<String> allContactIds) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.allContactIds = allContactIds;
        configIdentifier = ConfigFileIdentifier.GUI_CONVERSATION_STARTING_ROOM;
        filteredContactIds = new ArrayList<>();
        mediaFileParser = new MediaFileParser();
        degreeLevels = EnumArrayUtils.initializeDegreeLevels();
        studentStatusStringsWithoutDropouts = EnumArrayUtils.initializeStudentStatusStringsWithoutDropouts();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void updatePanel() {
    }

    private void updateFilteredContactIds() {
        Response response = clientController.getFilteredContactIds(allContactIds, selectedYearOfEntry,
                selectedDegreeLevel, selectedStudentStatus);
        if (response == null) return;
        filteredContactIds = (ArrayList<String>) response.get("contactIds");
    }

    @Override
    protected void initializeComponents() {
        yearOfEntryFilter = new JTextField(ConfigManager.getString(configIdentifier, "yearOfEntryFilterM"));
        degreeLevelFilter = new JComboBox<>(degreeLevels);
        studentStatusFilter = new JComboBox<>(studentStatusStringsWithoutDropouts);

        sendMessageButton = new JButton(ConfigManager.getString(configIdentifier, "sendMessageButtonM"));
        sendFileButton = new JButton(ConfigManager.getString(configIdentifier, "sendFileButtonM"));
        chooseFileButton = new JButton(ConfigManager.getString(configIdentifier, "chooseFileButtonM"));
        messageField = new JTextField(ConfigManager.getString(configIdentifier, "messageFieldM"));
        chosenFileLabelMessage = ConfigManager.getString(configIdentifier, "chosenFileLabelM");
        chosenFileLabel = new JLabel(chosenFileLabelMessage);
        fileChoosingBackground = new JLabel();

        goBackButton = new JButton(ConfigManager.getString(configIdentifier, "goBackButtonM"));
    }

    @Override
    protected void alignComponents() {
        yearOfEntryFilter.setBounds(ConfigManager.getInt(configIdentifier, "yearOfEntryFilterX"),
                ConfigManager.getInt(configIdentifier, "yearOfEntryFilterY"),
                ConfigManager.getInt(configIdentifier, "yearOfEntryFilterW"),
                ConfigManager.getInt(configIdentifier, "yearOfEntryFilterH"));
        add(yearOfEntryFilter);
        degreeLevelFilter.setBounds(ConfigManager.getInt(configIdentifier, "degreeLevelFilterX"),
                ConfigManager.getInt(configIdentifier, "degreeLevelFilterY"),
                ConfigManager.getInt(configIdentifier, "degreeLevelFilterW"),
                ConfigManager.getInt(configIdentifier, "degreeLevelFilterH"));
        add(degreeLevelFilter);
        studentStatusFilter.setBounds(ConfigManager.getInt(configIdentifier, "studentStatusFilterX"),
                ConfigManager.getInt(configIdentifier, "studentStatusFilterY"),
                ConfigManager.getInt(configIdentifier, "studentStatusFilterW"),
                ConfigManager.getInt(configIdentifier, "studentStatusFilterH"));
        add(studentStatusFilter);

        sendMessageButton.setBounds(ConfigManager.getInt(configIdentifier, "sendMessageButtonX"),
                ConfigManager.getInt(configIdentifier, "sendMessageButtonY"),
                ConfigManager.getInt(configIdentifier, "sendMessageButtonW"),
                ConfigManager.getInt(configIdentifier, "sendMessageButtonH"));
        add(sendMessageButton);
        sendFileButton.setBounds(ConfigManager.getInt(configIdentifier, "sendFileButtonX"),
                ConfigManager.getInt(configIdentifier, "sendFileButtonY"),
                ConfigManager.getInt(configIdentifier, "sendFileButtonW"),
                ConfigManager.getInt(configIdentifier, "sendFileButtonH"));
        add(sendFileButton);
        chooseFileButton.setBounds(ConfigManager.getInt(configIdentifier, "chooseFileButtonX"),
                ConfigManager.getInt(configIdentifier, "chooseFileButtonY"),
                ConfigManager.getInt(configIdentifier, "chooseFileButtonW"),
                ConfigManager.getInt(configIdentifier, "chooseFileButtonH"));
        add(chooseFileButton);
        messageField.setBounds(ConfigManager.getInt(configIdentifier, "messageFieldX"),
                ConfigManager.getInt(configIdentifier, "messageFieldY"),
                ConfigManager.getInt(configIdentifier, "messageFieldW"),
                ConfigManager.getInt(configIdentifier, "messageFieldH"));
        add(messageField);
        chosenFileLabel.setBounds(ConfigManager.getInt(configIdentifier, "chosenFileLabelX"),
                ConfigManager.getInt(configIdentifier, "chosenFileLabelY"),
                ConfigManager.getInt(configIdentifier, "chosenFileLabelW"),
                ConfigManager.getInt(configIdentifier, "chosenFileLabelH"));
        add(chosenFileLabel);
        fileChoosingBackground.setOpaque(true);
        fileChoosingBackground.setBackground(Color.WHITE);
        fileChoosingBackground.setBounds(ConfigManager.getInt(configIdentifier, "fileChoosingBackgroundX"),
                ConfigManager.getInt(configIdentifier, "fileChoosingBackgroundY"),
                ConfigManager.getInt(configIdentifier, "fileChoosingBackgroundW"),
                ConfigManager.getInt(configIdentifier, "fileChoosingBackgroundH"));
        add(fileChoosingBackground);

        goBackButton.setBounds(ConfigManager.getInt(configIdentifier, "goBackButtonX"),
                ConfigManager.getInt(configIdentifier, "goBackButtonY"),
                ConfigManager.getInt(configIdentifier, "goBackButtonW"),
                ConfigManager.getInt(configIdentifier, "goBackButtonH"));
        add(goBackButton);
    }

    @Override
    protected void connectListeners() {
        sendMessageButton.addActionListener(actionEvent -> {
            selectedYearOfEntry = getSelectedYearOfEntry();
            selectedDegreeLevel = getSelectedDegreeLevel();
            selectedStudentStatus = getSelectedStudentStatus();

            if (selectedYearOfEntry == -1 || selectedDegreeLevel == null || selectedStudentStatus == null) {
                return;
            }

            updateFilteredContactIds();
            if (filteredContactIds.isEmpty()) {
                displayAndLogError("noContactsWithFilterFound", "connectListeners");
                return;
            }

            Response response = clientController.sendTextMessage(offlineModeDTO.getId(), filteredContactIds,
                    messageField.getText());
            if (response == null) return;

            if (response.getResponseStatus() == ResponseStatus.OK) {
                String successPrompt = ConfigManager.getString(ConfigFileIdentifier.TEXTS, "sentTextMessage");
                JOptionPane.showMessageDialog(mainFrame, successPrompt);
                MasterLogger.clientInfo(clientController.getId(), successPrompt,
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
            selectedYearOfEntry = getSelectedYearOfEntry();
            selectedDegreeLevel = getSelectedDegreeLevel();
            selectedStudentStatus = getSelectedStudentStatus();

            if (selectedYearOfEntry == -1 || selectedDegreeLevel == null || selectedStudentStatus == null) {
                return;
            }

            updateFilteredContactIds();
            if (filteredContactIds.isEmpty()) {
                displayAndLogError("noContactsWithFilterFound", "connectListeners");
                return;
            }

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

            Response response = clientController.sendMediaMessage(offlineModeDTO.getId(), this.filteredContactIds,
                    convertedMediaFile);
            if (response.getResponseStatus() == ResponseStatus.OK) {
                String successPrompt = ConfigManager.getString(ConfigFileIdentifier.TEXTS, "mediaFileSent");

                JOptionPane.showMessageDialog(mainFrame, successPrompt);
                MasterLogger.clientInfo(clientController.getId(), successPrompt, "connectListeners", getClass());

                chosenFile = null;
                chosenFileLabel.setText(chosenFileLabelMessage);
            }
        });

        goBackButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Went back to messenger view",
                    "connectListeners", getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(new MrMohseniConversationStarter(mainFrame, mainMenu, offlineModeDTO));
        });
    }

    private StudentStatus getSelectedStudentStatus() {
        String selectedStudentStatusString = (String) studentStatusFilter.getSelectedItem();
        StudentStatus selectedStudentStatus = EnumStringMappingUtils.getStudentStatus(selectedStudentStatusString);
        if (selectedStudentStatus == null) {
            displayAndLogError("noStudentStatusSelected", "getSelectedStudentStatus");
            return null;
        } else {
            return selectedStudentStatus;
        }
    }

    private DegreeLevel getSelectedDegreeLevel() {
        String selectedDegreeLevelString = (String) degreeLevelFilter.getSelectedItem();
        DegreeLevel selectedDegreeLevel = EnumStringMappingUtils.getDegreeLevel(selectedDegreeLevelString);
        if (selectedDegreeLevel == null) {
            displayAndLogError("noDegreeLevelSelected", "getSelectedDegreeLevel");
            return null;
        } else {
            return selectedDegreeLevel;
        }
    }

    private int getSelectedYearOfEntry() {
        String yearOfEntryString = yearOfEntryFilter.getText();
        try {
            return Integer.parseInt(yearOfEntryString);
        } catch (NumberFormatException e) {
            displayAndLogError("wrongYearOfEntryFormat", "getSelectedYearOfEntry");
        }
        return -1;
    }

    private void displayAndLogError(String errorConfigKey, String methodName) {
        String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS, errorConfigKey);

        JOptionPane.showMessageDialog(mainFrame, errorMessage);
        MasterLogger.clientError(clientController.getId(), errorMessage, methodName,
                getClass());
    }
}
