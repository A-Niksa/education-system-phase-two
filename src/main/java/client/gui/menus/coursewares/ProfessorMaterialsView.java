package client.gui.menus.coursewares;

import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.utils.config.ConfigManager;

import javax.swing.*;

public class ProfessorMaterialsView extends MaterialsView {
    private JButton addButton;

    public ProfessorMaterialsView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String courseId) {
        super(mainFrame, mainMenu, offlineModeDTO, courseId);
        addAdditionFeature();
        startPinging(offlineModeDTO.getId());
    }

    private void addAdditionFeature() {
        initializeAddButton();
        alignAddButton();
        connectAdditionListeners();
    }

    private void initializeAddButton() {
        addButton = new JButton(ConfigManager.getString(configIdentifier, "addButtonM"));
    }

    private void alignAddButton() {
        addButton.setBounds(ConfigManager.getInt(configIdentifier, "addButtonX"),
                ConfigManager.getInt(configIdentifier, "addButtonY"),
                ConfigManager.getInt(configIdentifier, "addButtonW"),
                ConfigManager.getInt(configIdentifier, "addButtonH"));
        add(addButton);
    }

    private void connectAdditionListeners() {
        addButton.addActionListener(actionEvent -> {
            // TODO
        });
    }
}
