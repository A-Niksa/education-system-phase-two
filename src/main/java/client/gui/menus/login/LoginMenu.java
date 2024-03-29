package client.gui.menus.login;

import client.gui.menus.main.AdminMenu;
import client.gui.menus.main.MrMohseniMenu;
import client.gui.utils.ErrorUtils;
import client.gui.MainFrame;
import client.controller.ClientController;
import client.gui.menus.main.ProfessorMenu;
import client.gui.menus.main.StudentMenu;
import shareables.models.pojos.users.UserIdentifier;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.*;

public class LoginMenu extends JPanel {
    private MainFrame mainFrame;
    private ClientController clientController;
    private ConfigFileIdentifier configIdentifier;
    private CaptchaLoader captchaLoader;
    private JLabel welcomeMessage;
    private JLabel enterUsernameMessage;
    private JLabel enterPasswordMessage;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel enterCaptchaMessage;
    private JLabel captchaImage;
    private JTextField captchaField;
    private JButton changeCaptchaButton;

    public LoginMenu(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        clientController = mainFrame.getClientController();
        configIdentifier = ConfigFileIdentifier.GUI_LOGIN;
        captchaLoader = new CaptchaLoader();
        configurePanel();
        initializeComponents();
        alignComponents();
        connectListeners();
    }

    private void configurePanel() {
        setSize(new Dimension(mainFrame.getWidth(), mainFrame.getHeight()));
        setLayout(null);
    }

    private void initializeComponents() {
        welcomeMessage = new JLabel(ConfigManager.getString(configIdentifier, "welcomeMessage"));
        enterUsernameMessage = new JLabel(ConfigManager.getString(configIdentifier, "enterUsernameMessage"));
        enterPasswordMessage = new JLabel(ConfigManager.getString(configIdentifier, "enterPasswordMessage"));
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton(ConfigManager.getString(configIdentifier, "loginButtonMessage"));
        enterCaptchaMessage = new JLabel(ConfigManager.getString(configIdentifier, "enterCaptchaMessage"));
        captchaImage = new JLabel(captchaLoader.nextCaptchaImageIcon());
        captchaField = new JTextField();
        changeCaptchaButton = new JButton(ConfigManager.getString(configIdentifier, "changeCaptchaButtonMessage"));
    }

    private void alignComponents() {
        welcomeMessage.setBounds(ConfigManager.getInt(configIdentifier, "welcomeMessageX"),
                ConfigManager.getInt(configIdentifier, "welcomeMessageY"),
                ConfigManager.getInt(configIdentifier, "welcomeMessageW"),
                ConfigManager.getInt(configIdentifier, "welcomeMessageH"));
        welcomeMessage.setFont(new Font("", Font.BOLD, ConfigManager.getInt(configIdentifier, "welcomeMessageFontSize")));
        add(welcomeMessage);
        enterUsernameMessage.setBounds(ConfigManager.getInt(configIdentifier, "enterUsernameMessageX"),
                ConfigManager.getInt(configIdentifier, "enterUsernameMessageY"),
                ConfigManager.getInt(configIdentifier, "enterUsernameMessageW"),
                ConfigManager.getInt(configIdentifier, "enterUsernameMessageH"));
        add(enterUsernameMessage);
        enterPasswordMessage.setBounds(ConfigManager.getInt(configIdentifier, "enterPasswordMessageX"),
                ConfigManager.getInt(configIdentifier, "enterPasswordMessageY"),
                ConfigManager.getInt(configIdentifier, "enterPasswordMessageW"),
                ConfigManager.getInt(configIdentifier, "enterPasswordMessageH"));
        add(enterPasswordMessage);
        usernameField.setBounds(ConfigManager.getInt(configIdentifier, "usernameFieldX"),
                ConfigManager.getInt(configIdentifier, "usernameFieldY"),
                ConfigManager.getInt(configIdentifier, "usernameFieldW"),
                ConfigManager.getInt(configIdentifier, "usernameFieldH"));
        add(usernameField);
        passwordField.setBounds(ConfigManager.getInt(configIdentifier, "passwordFieldX"),
                ConfigManager.getInt(configIdentifier, "passwordFieldY"),
                ConfigManager.getInt(configIdentifier, "passwordFieldW"),
                ConfigManager.getInt(configIdentifier, "passwordFieldH"));
        add(passwordField);
        enterCaptchaMessage.setBounds(ConfigManager.getInt(configIdentifier, "enterCaptchaMessageX"),
                ConfigManager.getInt(configIdentifier, "enterCaptchaMessageY"),
                ConfigManager.getInt(configIdentifier, "enterCaptchaMessageW"),
                ConfigManager.getInt(configIdentifier, "enterCaptchaMessageH"));
        add(enterCaptchaMessage);
        captchaImage.setBounds(ConfigManager.getInt(configIdentifier, "captchaImageX"),
                ConfigManager.getInt(configIdentifier, "captchaImageY"),
                ConfigManager.getInt(configIdentifier, "captchaImageW"),
                ConfigManager.getInt(configIdentifier, "captchaImageH"));
        add(captchaImage);
        changeCaptchaButton.setBounds(ConfigManager.getInt(configIdentifier, "changeCaptchaButtonX"),
                ConfigManager.getInt(configIdentifier, "changeCaptchaButtonY"),
                ConfigManager.getInt(configIdentifier, "changeCaptchaButtonW"),
                ConfigManager.getInt(configIdentifier, "changeCaptchaButtonH"));
        add(changeCaptchaButton);
        captchaField.setBounds(ConfigManager.getInt(configIdentifier, "captchaFieldX"),
                ConfigManager.getInt(configIdentifier, "captchaFieldY"),
                ConfigManager.getInt(configIdentifier, "captchaFieldW"),
                ConfigManager.getInt(configIdentifier, "captchaFieldH"));
        add(captchaField);
        loginButton.setBounds(ConfigManager.getInt(configIdentifier, "loginButtonX"),
                ConfigManager.getInt(configIdentifier, "loginButtonY"),
                ConfigManager.getInt(configIdentifier, "loginButtonW"),
                ConfigManager.getInt(configIdentifier, "loginButtonH"));
        add(loginButton);
    }

    private void connectListeners() {
        changeCaptchaButton.addActionListener(e -> {
            captchaImage.setIcon(captchaLoader.nextCaptchaImageIcon());
            repaint();
            revalidate();
        });

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String captcha = captchaField.getText();
            String currentCaptcha = captchaLoader.getCurrentCaptchaString();

            if (!clientController.isClientOnline()) {
                JOptionPane.showMessageDialog(mainFrame,
                        ConfigManager.getString(configIdentifier, "clientNotConnected"));
                clientController.attemptServerConnection();
                return;
            }

            Response response = clientController.logIn(username, password, captcha, currentCaptcha);
            if (response == null) return;
            if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                MasterLogger.clientError(clientController.getId(), response.getErrorMessage(),
                        "alignComponents", LoginMenu.class);
                if (response.getErrorMessage()
                    .equals(ConfigManager.getString(ConfigFileIdentifier.TEXTS, "hasBeenTooLongSinceLastLogin"))) {
                    mainFrame.setCurrentPanel(new PasswordChanger(mainFrame, username));
                }
                return;
            }

            clientController.startClientLocalDatabaseManager(username);
            clientController.loadLocalDatabase();
            MasterLogger.clientInfo(clientController.getId(), "Started and loaded local database",
                    "connectListeners", getClass());

            UserIdentifier userIdentifier = (UserIdentifier) response.get("userIdentifier");
            if (userIdentifier == UserIdentifier.STUDENT) {
                MasterLogger.clientInfo(clientController.getId(), "Logged in as student",
                        "connectListeners", getClass());
                mainFrame.setCurrentPanel(new StudentMenu(mainFrame, username, null, true));
            } else if (userIdentifier == UserIdentifier.PROFESSOR) {
                MasterLogger.clientInfo(clientController.getId(), "Logged in as professor",
                        "connectListeners", getClass());
                mainFrame.setCurrentPanel(new ProfessorMenu(mainFrame, username, null, true));
            } else if (userIdentifier == UserIdentifier.MR_MOHSENI) {
                MasterLogger.clientInfo(clientController.getId(), "Logged in as Mr. Mohseni",
                        "connectListeners", getClass());
                mainFrame.setCurrentPanel(new MrMohseniMenu(mainFrame, username, null, true));
            } else if (userIdentifier == UserIdentifier.ADMIN) {
                MasterLogger.clientInfo(clientController.getId(), "Logged in as admin",
                        "connectListeners", getClass());
                mainFrame.setCurrentPanel(new AdminMenu(mainFrame, username, null, true));
            }
        });
    }
}