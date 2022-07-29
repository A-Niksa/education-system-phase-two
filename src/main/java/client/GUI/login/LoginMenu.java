package client.GUI.login;

import client.GUI.ErrorUtils;
import client.GUI.MainFrame;
import client.controller.ClientController;
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

            Response response = clientController.login(username, password, captcha, currentCaptcha);
            if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                MasterLogger.clientError(clientController.getId(), response.getErrorMessage(),
                        "alignComponents", LoginMenu.class);
                if (response.getErrorMessage()
                    .equals(ConfigManager.getString(ConfigFileIdentifier.TEXTS, "hasBeenTooLongSinceLastLogin"))) {
                    mainFrame.setCurrentPanel(new PasswordChanger(mainFrame, username));
                }
                return;
            }
            String userIdentifierString = (String) response.get("userIdentifier");
            if (userIdentifierString.equals("student")) {
                MasterLogger.clientInfo(clientController.getId(), "Logged in as student",
                        "connectListeners", LoginMenu.class);
                // TODO: open StudentMenu
                return;
            } else if (userIdentifierString.equals("professor")) {
                MasterLogger.clientInfo(clientController.getId(), "Logged in as professor",
                        "connectListeners", LoginMenu.class);
                // TODO: open ProfessorMenu
                return;
            }
        });
    }
}