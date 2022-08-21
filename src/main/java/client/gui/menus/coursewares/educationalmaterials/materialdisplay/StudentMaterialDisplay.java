package client.gui.menus.coursewares.educationalmaterials.materialdisplay;

import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;

public class StudentMaterialDisplay extends MaterialDisplay {
    public StudentMaterialDisplay(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String materialId) {
        super(mainFrame, mainMenu, offlineModeDTO, materialId);
        startPinging(offlineModeDTO.getId());
    }
}