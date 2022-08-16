package client.gui.menus.unitselection;

import client.controller.ClientController;
import client.gui.MainFrame;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;

public class PinnedCoursesSelection extends CoursesSelectionPanel {
    public PinnedCoursesSelection(MainFrame mainFrame, UnitSelectionMenu unitSelectionMenu,
                                  ClientController clientController, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, unitSelectionMenu, clientController, offlineModeDTO);
        drawPanel();
    }

    @Override
    protected void initializeComponents() {

    }

    @Override
    protected void alignComponents() {

    }

    @Override
    protected void connectListeners() {

    }

    @Override
    protected void connectListSelectionListeners() {

    }

    @Override
    public void updatePanel() {

    }

    @Override
    protected void updateCourseThumbnailDTOs() {

    }

    @Override
    protected void updateCourseThumbnailTexts() {

    }
}
