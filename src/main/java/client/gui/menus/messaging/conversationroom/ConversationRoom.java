package client.gui.menus.messaging.conversationroom;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.OfflinePanel;
import client.gui.menus.main.MainMenu;
import client.gui.menus.messaging.messengerviews.AdminMessengerView;
import client.gui.menus.messaging.messengerviews.MrMohseniMessengerView;
import client.gui.menus.messaging.messengerviews.ProfessorMessengerView;
import client.gui.menus.messaging.messengerviews.StudentMessengerView;
import client.gui.utils.ErrorUtils;
import client.gui.utils.ImageParsingUtils;
import client.locallogic.localdatabase.datamodels.QueuedMessage;
import client.locallogic.menus.messaging.DownloadManager;
import client.locallogic.menus.messaging.MediaFileParser;
import client.locallogic.menus.messaging.QueuingUtils;
import shareables.models.pojos.media.MediaFile;
import shareables.models.pojos.users.UserIdentifier;
import shareables.network.DTOs.messaging.ConversationDTO;
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
import java.util.List;

public class ConversationRoom extends DynamicPanelTemplate implements OfflinePanel {
    private String contactId;
    private JButton goBackButton;
    private JButton sendMessageButton;
    private JButton sendFileButton;
    private JButton chooseFileButton;
    private JButton downloadMediaButton;
    private JTextField messageField;
    private JLabel chosenFileLabel;
    private JLabel fileChoosingBackground;
    private JLabel contactProfilePictureLabel;
    private JLabel contactNameLabel;
    private ConversationChattingPanel conversationChattingPanel;
    private JScrollPane scrollPane;
    private ConversationDTO conversationDTO;
    private File chosenFile;
    private String chosenFileLabelMessage;
    private DownloadManager downloadManager;
    private MediaFileParser mediaFileParser;

    public ConversationRoom(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, ConversationDTO conversationDTO,
                            String contactId, boolean isOnline) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.conversationDTO = conversationDTO;
        this.contactId = contactId;
        this.isOnline = isOnline;
        configIdentifier = ConfigFileIdentifier.GUI_CONVERSATION_ROOM;
        downloadManager = new DownloadManager();
        mediaFileParser = new MediaFileParser();
        drawPanel();
        startPingingIfOnline(offlineModeDTO.getId(), this);
    }

    @Override
    protected void updatePanel() {
        conversationChattingPanel.updateConversationChattingPanel(isOnline);
        conversationDTO = conversationChattingPanel.getConversationDTO();
        ImageIcon profilePictureIcon = ImageParsingUtils.convertPictureToScaledImageIcon(conversationDTO.getContactProfilePicture(),
                ConfigManager.getInt(configIdentifier, "contactProfilePictureLabelW"),
                ConfigManager.getInt(configIdentifier, "contactProfilePictureLabelH"));
        contactProfilePictureLabel.setIcon(profilePictureIcon);
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

        ImageIcon profilePictureIcon = ImageParsingUtils.convertPictureToScaledImageIcon(conversationDTO.getContactProfilePicture(),
                ConfigManager.getInt(configIdentifier, "contactProfilePictureLabelW"),
                ConfigManager.getInt(configIdentifier, "contactProfilePictureLabelH"));
        contactProfilePictureLabel = new JLabel(profilePictureIcon);
        contactNameLabel = new JLabel(conversationDTO.getContactName(), SwingConstants.RIGHT);

        conversationChattingPanel = new ConversationChattingPanel(configIdentifier, conversationDTO, clientController,
                offlineModeDTO, contactId, isOnline);
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

        contactProfilePictureLabel.setBounds(ConfigManager.getInt(configIdentifier, "contactProfilePictureLabelX"),
                ConfigManager.getInt(configIdentifier, "contactProfilePictureLabelY"),
                ConfigManager.getInt(configIdentifier, "contactProfilePictureLabelW"),
                ConfigManager.getInt(configIdentifier, "contactProfilePictureLabelH"));
        add(contactProfilePictureLabel);

        contactNameLabel.setBounds(ConfigManager.getInt(configIdentifier, "contactNameLabelX"),
                ConfigManager.getInt(configIdentifier, "contactNameLabelY"),
                ConfigManager.getInt(configIdentifier, "contactNameLabelW"),
                ConfigManager.getInt(configIdentifier, "contactNameLabelH"));
        add(contactNameLabel);

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
            String adminId = ConfigManager.getString(ConfigFileIdentifier.CONSTANTS, "adminId");
            if (!isOnline && conversationDTO.getContactId().equals(adminId)) {
                QueuedMessage queuedMessage = QueuingUtils.getTextQueuedMessage(offlineModeDTO.getId(), messageField.getText());
                queuedMessagesManager.submitQueuedMessage(queuedMessage);

                JOptionPane.showMessageDialog(mainFrame, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                        "textMessageQueuedForSending"));
                MasterLogger.clientInfo(clientController.getId(), "Text message queued for sending to contact (ID: " +
                        contactId + ")", "connectListeners", getClass());

                return;
            }

            ArrayList<String> receiverList = new ArrayList<>(List.of(conversationDTO.getContactId()));

            Response response = clientController.sendTextMessage(offlineModeDTO.getId(), receiverList,
                    messageField.getText());
            if (response == null) return;

            if (response.getResponseStatus() == ResponseStatus.OK) {
                MasterLogger.clientInfo(clientController.getId(), "Sent a message to contact (ID: " +
                                conversationDTO.getContactId() + ")", "connectListeners", getClass());
                conversationChattingPanel.updateConversationChattingPanel(isOnline);

                messageField.setText(ConfigManager.getString(configIdentifier, "messageFieldM"));
            }
        });

        chooseFileButton.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));

            int returnedValue = fileChooser.showOpenDialog(mainFrame);
            if (returnedValue == JFileChooser.APPROVE_OPTION) {
                chosenFile = fileChooser.getSelectedFile();
                chosenFileLabel.setText(chosenFileLabelMessage + chosenFile.getPath());
                MasterLogger.clientInfo(clientController.getId(), "Media file chosen (Path: " + chosenFile.getPath()
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

            String adminId = ConfigManager.getString(ConfigFileIdentifier.CONSTANTS, "adminId");
            if (!isOnline && conversationDTO.getContactId().equals(adminId)) {
                QueuedMessage queuedMessage = QueuingUtils.getMediaQueuedMessage(offlineModeDTO.getId(), convertedMediaFile);
                queuedMessagesManager.submitQueuedMessage(queuedMessage);

                JOptionPane.showMessageDialog(mainFrame, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                        "mediaFileQueuedForSending"));
                MasterLogger.clientInfo(clientController.getId(), "Media file queued for sending to contact (ID: " +
                        contactId + ")", "connectListeners", getClass());

                return;
            }

            ArrayList<String> receiverList = new ArrayList<>(List.of(conversationDTO.getContactId()));
            Response response = clientController.sendMediaMessage(offlineModeDTO.getId(), receiverList,
                    convertedMediaFile);
            if (response.getResponseStatus() == ResponseStatus.OK) {
                JOptionPane.showMessageDialog(mainFrame, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                        "mediaFileSent"));

                MasterLogger.clientInfo(clientController.getId(), "Media file sent to contact (ID: " +
                        contactId + ")", "connectListeners", getClass());

                conversationChattingPanel.updateConversationChattingPanel(isOnline);
                chosenFile = null;
                chosenFileLabel.setText(chosenFileLabelMessage);
            }
        });

        downloadMediaButton.addActionListener(actionEvent -> {
            String mediaToDownloadId = JOptionPane.showInputDialog(mainFrame,
                    ConfigManager.getString(configIdentifier, "downloadMediaPromptM"));
            Response response = clientController.downloadMediaFromConversation(offlineModeDTO.getId(),
                    conversationDTO.getContactId(), mediaToDownloadId);

            if (response == null) return;

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
            MasterLogger.clientInfo(clientController.getId(), "Went back to messenger listview",
                    "connectListeners", getClass());
            facilitateChangingPanel(this);
            if (offlineModeDTO.getUserIdentifier() == UserIdentifier.STUDENT) {
                mainFrame.setCurrentPanel(new StudentMessengerView(mainFrame, mainMenu, offlineModeDTO, isOnline));
            } else if (offlineModeDTO.getUserIdentifier() == UserIdentifier.PROFESSOR) {
                mainFrame.setCurrentPanel(new ProfessorMessengerView(mainFrame, mainMenu, offlineModeDTO, isOnline));
            } else if (offlineModeDTO.getUserIdentifier() == UserIdentifier.MR_MOHSENI) {
                mainFrame.setCurrentPanel(new MrMohseniMessengerView(mainFrame, mainMenu, offlineModeDTO, isOnline));
            } else if (offlineModeDTO.getUserIdentifier() == UserIdentifier.ADMIN) {
                mainFrame.setCurrentPanel(new AdminMessengerView(mainFrame, mainMenu, offlineModeDTO, isOnline));
            }
        });
    }

    @Override
    public void disableOnlineComponents() {
        stopPanelLoop();

        String adminId = ConfigManager.getString(ConfigFileIdentifier.CONSTANTS, "adminId");
        if (!contactId.equals(adminId)) {
            sendMessageButton.setEnabled(false);
            messageField.setEnabled(false);

            sendFileButton.setEnabled(false);
            chooseFileButton.setEnabled(false);
        }
    }

    @Override
    public void enableOnlineComponents() {
        restartPanelLoop();

        String adminId = ConfigManager.getString(ConfigFileIdentifier.CONSTANTS, "adminId");
        if (!contactId.equals(adminId)) {
            sendMessageButton.setEnabled(true);
            messageField.setEnabled(true);

            sendFileButton.setEnabled(true);
            chooseFileButton.setEnabled(true);
        }
    }
}
