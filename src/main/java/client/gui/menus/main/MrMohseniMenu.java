package client.gui.menus.main;

import client.gui.MainFrame;
import client.gui.menus.messaging.messengerviews.MrMohseniMessengerView;
import client.gui.menus.messaging.messengerviews.ProfessorMessengerView;
import client.gui.menus.searching.MrMohseniSearcher;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;

public class MrMohseniMenu extends SpecialUserMenu {
    private JButton studentSearchButton;

    public MrMohseniMenu(MainFrame mainFrame, String username, OfflineModeDTO offlineModeDTO, boolean isOnline) {
        super(mainFrame, username, MainMenuType.MR_MOHSENI, offlineModeDTO, isOnline);
        addStudentSearchButton();
        connectStudentSearchListener();
        startPingingIfOnline(this.offlineModeDTO.getId(), this);
    }

    public MrMohseniMenu(MainFrame mainFrame, OfflineModeDTO offlineModeDTO, boolean isOnline) {
        super(mainFrame, MainMenuType.MR_MOHSENI, offlineModeDTO, isOnline);
        addStudentSearchButton();
        connectStudentSearchListener();
        startPingingIfOnline(offlineModeDTO.getId(), this);
    }

    private void addStudentSearchButton() {
        studentSearchButton = new JButton(ConfigManager.getString(configIdentifier, "studentSearchButtonM"));
        studentSearchButton.setBounds(ConfigManager.getInt(configIdentifier, "studentSearchButtonX"),
                ConfigManager.getInt(configIdentifier, "studentSearchButtonY"),
                ConfigManager.getInt(configIdentifier, "studentSearchButtonW"),
                ConfigManager.getInt(configIdentifier, "studentSearchButtonH"));
        add(studentSearchButton);
    }

    @Override
    protected void connectMessengerListener() {
    }

    private void connectStudentSearchListener() {
        MainMenu mainMenu = this;

        messengerButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Opened the messenger",
                    "connectListeners", getClass());
            facilitateChangingPanel(mainMenu);
            mainFrame.setCurrentPanel(new MrMohseniMessengerView(mainFrame, mainMenu, offlineModeDTO, isOnline));
        });

        studentSearchButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Opened the student searching section",
                    "connectListeners", getClass());
            facilitateChangingPanel(mainMenu);
            mainFrame.setCurrentPanel(new MrMohseniSearcher(mainFrame, mainMenu, offlineModeDTO));
        });
    }

    @Override
    public void disableOnlineComponents() {
        stopPanelLoop();
        studentSearchButton.setEnabled(false);

    }

    @Override
    public void enableOnlineComponents() {
        restartPanelLoop();
        studentSearchButton.setEnabled(true);
    }
}
