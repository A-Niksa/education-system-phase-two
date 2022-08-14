package client.gui.menus.main;

import client.gui.MainFrame;
import client.gui.menus.messaging.messengerviews.ProfessorMessengerView;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.utils.logging.MasterLogger;

public class AdminMenu extends SpecialUserMenu {
    public AdminMenu(MainFrame mainFrame, String username, OfflineModeDTO offlineModeDTO, boolean isOnline) {
        super(mainFrame, username, MainMenuType.ADMIN, offlineModeDTO, isOnline);
        startPingingIfOnline(this.offlineModeDTO.getId(), this);
    }

    public AdminMenu(MainFrame mainFrame, OfflineModeDTO offlineModeDTO, boolean isOnline) {
        super(mainFrame, MainMenuType.ADMIN, offlineModeDTO, isOnline);
        startPingingIfOnline(offlineModeDTO.getId(), this);
    }

    @Override
    protected void connectMessengerListener() {
        MainMenu mainMenu = this;

        messengerButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Opened the messenger",
                    "connectListeners", getClass());
            facilitateChangingPanel(mainMenu);
            mainFrame.setCurrentPanel(new ProfessorMessengerView(mainFrame, mainMenu, offlineModeDTO));
        });
    }

    @Override
    public void disableOnlineComponents() {
        stopPanelLoop();
    }

    @Override
    public void enableOnlineComponents() {
        restartPanelLoop();
    }
}
