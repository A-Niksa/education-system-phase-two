package client.gui.menus.main;

import client.gui.MainFrame;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;

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
