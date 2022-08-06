package client.gui.menus.main;

import client.controller.ClientController;
import client.gui.MainFrame;
import client.gui.OfflinePanel;
import client.gui.menus.login.LoginMenu;
import client.gui.utils.ImageParsingUtils;
import client.locallogic.main.DateStringFormatter;
import client.locallogic.main.UserGetter;
import shareables.models.pojos.users.User;
import shareables.network.DTOs.OfflineModeDTO;
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
    protected User user;
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

    public MainMenu(MainFrame mainFrame, String username) {
        this.mainFrame = mainFrame;
        clientController = mainFrame.getClientController();
        user = UserGetter.getUser(username, clientController);
        configIdentifier = ConfigFileIdentifier.GUI_MAIN;
        updateOfflineModeDTO(username);
        startPinging(username);
        configurePanel();
        drawPanel();
    }

    private void startPinging(String username) {
        initializePingingTask(username);
        initializePanelLoop();
        startPanelLoop();
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

    protected abstract void updatePanel();

    protected void updateOfflineModeDTO(String userId) {
        Response response = clientController.getOfflineModeDTO(userId);
        if (response.getResponseStatus() == ResponseStatus.OK) {
            offlineModeDTO = (OfflineModeDTO) response.get("offlineModeDTO");
        }
    }

    private void notifyClientOfConnectionLoss() {
        JOptionPane.showMessageDialog(mainFrame, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "connectionLoss"));
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
        ImageIcon profilePictureIcon = ImageParsingUtils.convertPictureToImageIcon(offlineModeDTO.getProfilePicture());
        profilePicture = new JLabel(profilePictureIcon);
        nameLabel = new JLabel(offlineModeDTO.getName());
        emailAddressLabel = new JLabel(offlineModeDTO.getEmailAddress());
        logOutButton = new JButton(ConfigManager.getString(configIdentifier, "logOutButtonMessage"));
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

    public void setOfflineModeDTO(OfflineModeDTO offlineModeDTO) {
        this.offlineModeDTO = offlineModeDTO;
    }
}