package client.gui.menus.coursewares.educationalmaterials.listview;

import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;

public class StudentMaterialsView extends MaterialsView {
    public StudentMaterialsView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String courseId) {
        super(mainFrame, mainMenu, offlineModeDTO, courseId);
        startPinging(offlineModeDTO.getId());
    }
}
