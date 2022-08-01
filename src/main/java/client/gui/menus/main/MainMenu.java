package client.gui.menus.main;

import client.controller.ClientController;
import client.gui.MainFrame;
import client.gui.menus.login.LoginMenu;
import client.gui.utils.ImageParsingUtils;
import client.locallogic.main.UserGetter;
import shareables.models.pojos.users.User;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;
import shareables.utils.timekeeping.FormattingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;

public class MainMenu extends JPanel {
    protected MainFrame mainFrame;
    protected ClientController clientController;
    protected User user;
    protected ConfigFileIdentifier configIdentifier;
    protected JLabel lastLoginTime;
    protected JLabel profilePicture;
    protected JLabel nameLabel;
    protected JLabel emailAddressLabel;
    protected JButton logOutButton;

    public MainMenu(MainFrame mainFrame, String username) {
        this.mainFrame = mainFrame;
        clientController = mainFrame.getClientController();
        user = UserGetter.getUser(username, clientController);
        configIdentifier = ConfigFileIdentifier.GUI_MAIN;
        configurePanel();
        initializeComponents();
        alignComponents();
        connectListeners();
    }

    protected void configurePanel() {
        setSize(new Dimension(mainFrame.getWidth(), mainFrame.getHeight()));
        setLayout(null);
    }

    private void initializeComponents() {
        DateFormat dateFormat = FormattingUtils.getStandardDateFormat();
        lastLoginTime = new JLabel("Last Login: " + dateFormat.format(user.getLastLogin()));
        ImageIcon profilePictureIcon = ImageParsingUtils.convertPictureToImageIcon(user.getProfilePicture());
        profilePicture = new JLabel(profilePictureIcon);
        nameLabel = new JLabel(user.getFirstName() + " " + user.getLastName());
        emailAddressLabel = new JLabel(user.getEmailAddress());
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
                mainFrame.setCurrentPanel(new LoginMenu(mainFrame));
            }
        });
    }
}
