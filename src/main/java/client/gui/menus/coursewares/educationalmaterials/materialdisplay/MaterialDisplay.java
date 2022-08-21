package client.gui.menus.coursewares.educationalmaterials.materialdisplay;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;

public abstract class MaterialDisplay extends DynamicPanelTemplate {
    protected String materialId;

    public MaterialDisplay(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String materialId) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.materialId = materialId;
    }

    @Override
    protected void updatePanel() {

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
}
