package client.gui.menus.main;

import client.controller.ClientController;
import client.gui.MainFrame;
import client.gui.OfflinePanel;
import client.gui.menus.login.LoginMenu;
import client.gui.utils.ImageParsingUtils;
import client.locallogic.main.DateStringFormatter;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.pinging.Loop;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class MainMenu extends JPanel implements OfflinePanel {
    protected MainFrame mainFrame;
    protected ClientController clientController;
    protected ConfigFileIdentifier configIdentifier;
    protected JLabel lastLoginTime;
    protected JLabel profilePicture;
    protected JLabel nameLabel;
    protected JLabel emailAddressLabel;
    protected JButton logOutButton;
    protected Loop panelLoop;
    protected OfflineModeDTO offlineModeDTO;
    protected boolean isOnline;
    protected Runnable pingingTask;
    protected String lastLoginTimePrompt;
    protected MainMenuType mainMenuType;
    protected JButton notificationsButton;
    protected JButton messengerButton;

    public MainMenu(MainFrame mainFrame, String username, MainMenuType mainMenuType, OfflineModeDTO offlineModeDTO,
                    boolean isOnline) {
        this.mainFrame = mainFrame;
        this.mainMenuType = mainMenuType;
        this.isOnline = isOnline;
        clientController = mainFrame.getClientController();
        initializeOfflineModeDTO(offlineModeDTO, username);
        configIdentifier = ConfigFileIdentifier.GUI_MAIN;
        configurePanel();
        drawPanel();
    }

    public MainMenu(MainFrame mainFrame, MainMenuType mainMenuType, OfflineModeDTO offlineModeDTO, boolean isOnline) {
        this.mainFrame = mainFrame;
        this.mainMenuType = mainMenuType;
        this.isOnline = isOnline;
        clientController = mainFrame.getClientController();
        initializeOfflineModeDTO(offlineModeDTO, offlineModeDTO.getId());
        configIdentifier = ConfigFileIdentifier.GUI_MAIN;
        configurePanel();
        drawPanel();
    }

    private void initializeOfflineModeDTO(OfflineModeDTO offlineModeDTO, String username) {
        if (offlineModeDTO == null) updateOfflineModeDTO(username);
        else this.offlineModeDTO = offlineModeDTO;
    }

    public void startPingingIfOnline(String userId, OfflinePanel offlinePanel) {
        initializePingingTask(userId);
        initializePanelLoop();
        if (isOnline) {
            startPanelLoop();
        } else {
            offlinePanel.goOffline(mainFrame, this, clientController);
        }
    }

    private void checkIfClientIsOnline() {
        isOnline = clientController.isClientOnline();
    }

    private void initializePingingTask(String username) {
        pingingTask = () -> {
            checkIfClientIsOnline();
            if (isOnline) {
                updateOfflineModeDTO(username);
                updatePanel();
            } else {
                notifyClientOfConnectionLoss();
                goOffline(mainFrame, this, clientController);
            }
        };
    }

    private void notifyClientOfConnectionLoss() {
        JOptionPane.showMessageDialog(mainFrame, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "connectionLoss"));
    }

    protected abstract void updatePanel();

    protected void updateOfflineModeDTO(String userId) {
        Response response = clientController.getOfflineModeDTO(userId);
        if (response == null) return;
        if (response.getResponseStatus() == ResponseStatus.OK) {
            offlineModeDTO = (OfflineModeDTO) response.get("offlineModeDTO");
        }
    }

    private void initializePanelLoop() {
        panelLoop = new Loop(ConfigManager.getDouble(ConfigFileIdentifier.CONSTANTS, "pingingFps"),
                pingingTask);
    }

    protected void startPanelLoop() {
        panelLoop.start();
    }

    protected void stopPanelLoop() {
        panelLoop.stop();
    }

    protected void restartPanelLoop() {
        panelLoop.restart();
    }

    protected void facilitateChangingPanel(OfflinePanel offlinePanel) {
        offlinePanel.removeOfflineComponents((JPanel) offlinePanel);
        stopPanelLoop();
    }

    public void drawPanel() {
        initializeComponents();
        alignComponents();
        connectListeners();
    }

    protected void configurePanel() {
        setSize(new Dimension(mainFrame.getWidth(), mainFrame.getHeight()));
        setLayout(null);
    }

    private void initializeComponents() {
        lastLoginTimePrompt = ConfigManager.getString(configIdentifier, "lastLoginTimeMessage");
        lastLoginTime = new JLabel(lastLoginTimePrompt + DateStringFormatter.format(offlineModeDTO.getLastLogin()));
        ImageIcon profilePictureIcon = ImageParsingUtils.convertPictureToScaledImageIcon(offlineModeDTO.getProfilePicture(),
                ConfigManager.getInt(configIdentifier, "profilePictureW"),
                ConfigManager.getInt(configIdentifier, "profilePictureH"));
        profilePicture = new JLabel(profilePictureIcon);
        nameLabel = new JLabel(offlineModeDTO.getName());
        emailAddressLabel = new JLabel(offlineModeDTO.getEmailAddress());
        logOutButton = new JButton(ConfigManager.getString(configIdentifier, "logOutButtonMessage"));
        notificationsButton = new JButton(ConfigManager.getString(configIdentifier, "notificationsButtonM"));
        messengerButton = new JButton(ConfigManager.getString(configIdentifier, "messengerButtonM"));
    }

    private void alignComponents() {
        profilePicture.setBounds(ConfigManager.getInt(configIdentifier, "profilePictureX"),
                ConfigManager.getInt(configIdentifier, "profilePictureY"),
                ConfigManager.getInt(configIdentifier, "profilePictureW"),
                ConfigManager.getInt(configIdentifier, "profilePictureH"));
        add(profilePicture);
        nameLabel.setBounds(ConfigManager.getInt(configIdentifier, "nameLabelX"),
                ConfigManager.getInt(configIdentifier, "nameLabelY"),
                ConfigManager.getInt(configIdentifier, "nameLabelW"),
                ConfigManager.getInt(configIdentifier, "nameLabelH"));
        add(nameLabel);
        emailAddressLabel.setBounds(ConfigManager.getInt(configIdentifier, "emailAddressLabelX"),
                ConfigManager.getInt(configIdentifier, "emailAddressLabelY"),
                ConfigManager.getInt(configIdentifier, "emailAddressLabelW"),
                ConfigManager.getInt(configIdentifier, "emailAddressLabelH"));
        add(emailAddressLabel);
        logOutButton.setBounds(ConfigManager.getInt(configIdentifier, "logOutButtonX"),
                ConfigManager.getInt(configIdentifier, "logOutButtonY"),
                ConfigManager.getInt(configIdentifier, "logOutButtonW"),
                ConfigManager.getInt(configIdentifier, "logOutButtonH"));
        add(logOutButton);
        lastLoginTime.setBounds(ConfigManager.getInt(configIdentifier, "lastLoginTimeX"),
                ConfigManager.getInt(configIdentifier, "lastLoginTimeY"),
                ConfigManager.getInt(configIdentifier, "lastLoginTimeW"),
                ConfigManager.getInt(configIdentifier, "lastLoginTimeH"));
        add(lastLoginTime);
        notificationsButton.setBounds(ConfigManager.getInt(configIdentifier, "notificationsButtonX"),
                ConfigManager.getInt(configIdentifier, "notificationsButtonY"),
                ConfigManager.getInt(configIdentifier, "notificationsButtonW"),
                ConfigManager.getInt(configIdentifier, "notificationsButtonH"));
        add(notificationsButton);
        messengerButton.setBounds(ConfigManager.getInt(configIdentifier, "messengerButtonX"),
                ConfigManager.getInt(configIdentifier, "messengerButtonY"),
                ConfigManager.getInt(configIdentifier, "messengerButtonW"),
                ConfigManager.getInt(configIdentifier, "messengerButtonH"));
        add(messengerButton);
    }

    private void connectListeners() {
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Logged out", "connectListeners",
                        getClass());
                stopPanelLoop();
                mainFrame.setCurrentPanel(new LoginMenu(mainFrame));
            }
        });
    }

    public MainMenuType getMainMenuType() {
        return mainMenuType;
    }

    public String getUsername() {
        return offlineModeDTO.getId();
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}