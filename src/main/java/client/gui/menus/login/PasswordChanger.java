package client.gui.menus.login;

import client.gui.utils.ErrorUtils;
import client.gui.MainFrame;
import client.controller.ClientController;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import javax.swing.*;
import java.awt.*;

public class PasswordChanger extends JPanel {
    private MainFrame mainFrame;
    private String userId;
    private ClientController clientController;
    private JTextField newPasswordField;
    private JButton changePassword;

    public PasswordChanger(MainFrame mainFrame, String userId) {
        this.mainFrame = mainFrame;
        this.userId = userId;
        clientController = mainFrame.getClientController();
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
        newPasswordField = new JTextField(ConfigManager.getString(ConfigFileIdentifier.GUI_LOGIN,
                "newPasswordFieldMessage"));
        changePassword = new JButton(ConfigManager.getString(ConfigFileIdentifier.GUI_LOGIN, "changePasswordMessage"));
    }

    private void alignComponents() {
        newPasswordField.setBounds(ConfigManager.getInt(ConfigFileIdentifier.GUI_LOGIN, "newPasswordFieldX"),
                ConfigManager.getInt(ConfigFileIdentifier.GUI_LOGIN, "newPasswordFieldY"),
                ConfigManager.getInt(ConfigFileIdentifier.GUI_LOGIN, "newPasswordFieldW"),
                ConfigManager.getInt(ConfigFileIdentifier.GUI_LOGIN, "newPasswordFieldH"));
        add(newPasswordField);
        changePassword.setBounds(ConfigManager.getInt(ConfigFileIdentifier.GUI_LOGIN, "changePasswordX"),
                ConfigManager.getInt(ConfigFileIdentifier.GUI_LOGIN, "changePasswordY"),
                ConfigManager.getInt(ConfigFileIdentifier.GUI_LOGIN, "changePasswordW"),
                ConfigManager.getInt(ConfigFileIdentifier.GUI_LOGIN, "changePasswordH"));
        add(changePassword);
    }

    private void connectListeners() {
        changePassword.addActionListener(e -> {
            String newPassword = newPasswordField.getText();

            Response response = clientController.changePassword(userId, newPassword);
            if (response == null) return;
            if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                return;
            }
            mainFrame.setCurrentPanel(new LoginMenu(mainFrame));
        });
    }
}