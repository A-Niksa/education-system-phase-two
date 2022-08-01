package client.gui;

import client.gui.menus.login.LoginMenu;
import client.controller.ClientController;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private ClientController clientController;
    private ConfigFileIdentifier configIdentifier;
    private JPanel currentPanel;

    public MainFrame(ClientController clientController) {
        this.clientController = clientController;
        configIdentifier = ConfigFileIdentifier.GUI;
        initializePanel();
        configureFrame();
        repaintFrame();
    }

    private void initializePanel() {
        LoginMenu loginMenu = new LoginMenu(this);
        setCurrentPanel(loginMenu);
    }

    public void setCurrentPanel(JPanel currentPanel) {
        this.currentPanel = currentPanel;
        getContentPane().removeAll();
        getContentPane().add(this.currentPanel);
        repaintFrame();
    }

    private void configureFrame() {
        setSize(new Dimension(ConfigManager.getInt(configIdentifier, "frameWidth"),
                ConfigManager.getInt(configIdentifier, "frameHeight")));
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void repaintFrame() {
        repaint();
        revalidate();
    }

    public ClientController getClientController() {
        return clientController;
    }
}