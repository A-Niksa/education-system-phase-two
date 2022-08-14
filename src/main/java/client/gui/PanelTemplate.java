package client.gui;

import client.controller.ClientController;
import client.gui.menus.main.*;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class PanelTemplate extends JPanel {
    protected MainFrame mainFrame;
    protected MainMenu mainMenu;
    protected ClientController clientController;
    protected JButton mainMenuButton;
    protected ConfigFileIdentifier configIdentifier;

    public PanelTemplate(MainFrame mainFrame, MainMenu mainMenu) {
        this.mainFrame = mainFrame;
        this.mainMenu = mainMenu;
        clientController = mainFrame.getClientController();
        configIdentifier = ConfigFileIdentifier.GUI_PANEL_TEMPLATE;
        configurePanel();
        initializeTemplateComponents();
        alignTemplateComponents();
        connectTemplateListeners();
    }

    protected void drawPanel() {
        initializeComponents();
        alignComponents();
        connectListeners();
    }

    private void configurePanel() {
        setSize(new Dimension(mainFrame.getWidth(), mainFrame.getHeight()));
        setLayout(null);
    }

    private void initializeTemplateComponents() {
        mainMenuButton = new JButton(ConfigManager.getString(configIdentifier, "mainMenuButtonM"));
    }

    protected abstract void initializeComponents();

    private void alignTemplateComponents() {
        mainMenuButton.setBounds(ConfigManager.getInt(configIdentifier, "mainMenuButtonX"),
                ConfigManager.getInt(configIdentifier, "mainMenuButtonY"),
                ConfigManager.getInt(configIdentifier, "mainMenuButtonW"),
                ConfigManager.getInt(configIdentifier, "mainMenuButtonH"));
        add(mainMenuButton);
    }

    protected abstract void alignComponents();

    protected void connectTemplateListeners() {
        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Returned to the main menu",
                        "connectListeners", getClass());
                MainMenu newMainMenu;
                switch (mainMenu.getMainMenuType()) {
                    case PROFESSOR:
                        newMainMenu = new ProfessorMenu(mainFrame, mainMenu.getUsername(), null, true);
                        break;
                    case STUDENT:
                        newMainMenu = new StudentMenu(mainFrame, mainMenu.getUsername(), null, true);
                        break;
                    case MR_MOHSENI:
                        newMainMenu = new MrMohseniMenu(mainFrame, mainMenu.getUsername(), null, true);
                        break;
                    case ADMIN:
                        newMainMenu = new AdminMenu(mainFrame, mainMenu.getUsername(), null, true);
                        break;
                    default:
                        newMainMenu = null; // added for explicitness
                }
                mainFrame.setCurrentPanel(newMainMenu);
                mainMenu = newMainMenu;
            }
        });
    }

    protected abstract void connectListeners();
}