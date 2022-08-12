package client.gui.menus.messaging;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import client.gui.utils.ErrorUtils;
import client.locallogic.messaging.DownloadManager;
import client.locallogic.messaging.MediaFileParser;
import shareables.models.pojos.media.MediaFile;
import shareables.network.DTOs.ConversationDTO;
import shareables.network.DTOs.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ConversationRoom extends DynamicPanelTemplate {
    private String contactId;
    private JButton goBackButton;
    private JButton sendMessageButton;
    private JButton sendFileButton;
    private JButton chooseFileButton;
    private JButton downloadMediaButton;
    private JTextField messageField;
    private JLabel chosenFileLabel;
    private JLabel fileChoosingBackground;
    private ConversationChattingPanel conversationChattingPanel;
    private JScrollPane scrollPane;
    private ConversationDTO conversationDTO;
    private MessengerView messengerView;
    private File chosenFile;
    private String chosenFileLabelMessage;
    private DownloadManager downloadManager;
    private MediaFileParser mediaFileParser;

    public ConversationRoom(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, ConversationDTO conversationDTO,
                            MessengerView messengerView, String contactId) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.conversationDTO = conversationDTO;
        this.messengerView = messengerView;
        this.contactId = contactId;
        configIdentifier = ConfigFileIdentifier.GUI_CONVERSATION_ROOM;
        downloadManager = new DownloadManager();
        mediaFileParser = new MediaFileParser();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void updatePanel() {
        conversationChattingPanel.updateConversationChattingPanel();
    }

    @Override
    protected void initializeComponents() {
        sendMessageButton = new JButton(ConfigManager.getString(configIdentifier, "sendMessageButtonM"));
        sendFileButton = new JButton(ConfigManager.getString(configIdentifier, "sendFileButtonM"));
        chooseFileButton = new JButton(ConfigManager.getString(configIdentifier, "chooseFileButtonM"));
        downloadMediaButton = new JButton(ConfigManager.getString(configIdentifier, "downloadMediaButtonM"));
        messageField = new JTextField(ConfigManager.getString(configIdentifier, "messageFieldM"));
        chosenFileLabelMessage = ConfigManager.getString(configIdentifier, "chosenFileLabelM");
        chosenFileLabel = new JLabel(chosenFileLabelMessage);
        fileChoosingBackground = new JLabel();
        conversationChattingPanel = new ConversationChattingPanel(configIdentifier, conversationDTO, clientController,
                offlineModeDTO, contactId);
        scrollPane = new JScrollPane(conversationChattingPanel);

        goBackButton = new JButton(ConfigManager.getString(configIdentifier, "goBackButtonM"));
    }

    @Override
    protected void alignComponents() {
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
        downloadMediaButton.setBounds(ConfigManager.getInt(configIdentifier, "downloadMediaButtonX"),
                ConfigManager.getInt(configIdentifier, "downloadMediaButtonY"),
                ConfigManager.getInt(configIdentifier, "downloadMediaButtonW"),
                ConfigManager.getInt(configIdentifier, "downloadMediaButtonH"));
        add(downloadMediaButton);

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.setValue(scrollBar.getMaximum());
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);

        goBackButton.setBounds(ConfigManager.getInt(configIdentifier, "goBackButtonX"),
                ConfigManager.getInt(configIdentifier, "goBackButtonY"),
                ConfigManager.getInt(configIdentifier, "goBackButtonW"),
                ConfigManager.getInt(configIdentifier, "goBackButtonH"));
        add(goBackButton);
    }

    @Override
    protected void connectListeners() {
        sendMessageButton.addActionListener(actionEvent -> {
            Response response = clientController.sendTextMessage(offlineModeDTO.getId(), conversationDTO.getContactId(),
                    messageField.getText());
            if (response.getResponseStatus() == ResponseStatus.OK) {
                MasterLogger.clientInfo(clientController.getId(), "Sent a message to contact (ID: " +
                                conversationDTO.getContactId() + ")", "connectListeners", getClass());
                conversationChattingPanel.updateConversationChattingPanel();
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

            Response response = clientController.sendMediaMessage(offlineModeDTO.getId(), conversationDTO.getContactId(),
                    convertedMediaFile);
            if (response.getResponseStatus() == ResponseStatus.OK) {
                JOptionPane.showMessageDialog(mainFrame, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                        "mediaFileSent"));

                MasterLogger.clientInfo(clientController.getId(), "Media file sent to contact (ID: " +
                        contactId + ")", "connectListeners", getClass());
                conversationChattingPanel.updateConversationChattingPanel();
            }
        });

        downloadMediaButton.addActionListener(actionEvent -> {
            String mediaToDownloadId = JOptionPane.showInputDialog(mainFrame,
                    ConfigManager.getString(configIdentifier, "downloadMediaPromptM"));
            Response response = clientController.downloadMediaFromConversation(offlineModeDTO.getId(),
                    conversationDTO.getContactId(), mediaToDownloadId);

            if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                MasterLogger.clientError(clientController.getId(), response.getErrorMessage(), "connectListeners",
                        getClass());
            } else {
                String downloadedFilePrompt = downloadManager.saveMediaToLocalDownloads((MediaFile) response.get("mediaFile"));
                JOptionPane.showMessageDialog(mainFrame, downloadedFilePrompt);
                MasterLogger.clientInfo(clientController.getId(), downloadedFilePrompt, "connectListeners",
                        getClass());
            }
        });

        goBackButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Went back to messenger view",
                    "connectListeners", getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(messengerView);
        });
    }
}
