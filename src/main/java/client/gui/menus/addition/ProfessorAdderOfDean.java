package client.gui.menus.addition;

import client.gui.MainFrame;
import client.gui.menus.enrolment.editing.ProfessorsListEditor;
import client.gui.menus.main.MainMenu;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfessorAdderOfDean extends ProfessorAdder {
    private JButton goBackButton;
    private int clientControllerId;

    public ProfessorAdderOfDean(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO,
                                int clientControllerId) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.clientControllerId = clientControllerId;
        initializeAndAlignBackButton();
        add(goBackButton);
        connectBackListener();
    }

    private void initializeAndAlignBackButton() {
        goBackButton = new JButton(ConfigManager.getString(configIdentifier, "goBackButtonM"));
        goBackButton.setBounds(ConfigManager.getInt(configIdentifier, "goBackButtonX"),
                ConfigManager.getInt(configIdentifier, "goBackButtonY"),
                ConfigManager.getInt(configIdentifier, "goBackButtonW"),
                ConfigManager.getInt(configIdentifier, "goBackButtonH"));
    }

    private void connectBackListener() {
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientControllerId, "Went back to professors list editor",
                        "connectBackListener", getClass());
                stopPanelLoop();
                mainFrame.setCurrentPanel(new ProfessorsListEditor(mainFrame, mainMenu, offlineModeDTO));
            }
        });
    }
}
