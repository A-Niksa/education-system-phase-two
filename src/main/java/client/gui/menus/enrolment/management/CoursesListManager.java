package client.gui.menus.enrolment.management;

import client.gui.MainFrame;
import client.gui.menus.enrolment.editing.CoursesListEditor;
import client.gui.menus.enrolment.viewing.CoursesListView;
import client.gui.menus.main.MainMenu;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.OfflineModeDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CoursesListManager extends CoursesListView {
    private Professor professor;
    private JButton openEditor;

    public CoursesListManager(MainFrame mainFrame, MainMenu mainMenu, Professor professor, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.professor = professor;
        configIdentifier = ConfigFileIdentifier.GUI_LIST_MANAGER;
        setEditorButton();
        connectEditorListener();
        drawPanel();
    }

    private void setEditorButton() {
        openEditor = new JButton(ConfigManager.getString(configIdentifier, "openEditorM"));
        openEditor.setBounds(ConfigManager.getInt(configIdentifier, "openEditorX"),
                ConfigManager.getInt(configIdentifier, "openEditorY"),
                ConfigManager.getInt(configIdentifier, "openEditorW"),
                ConfigManager.getInt(configIdentifier, "openEditorH"));
        add(openEditor);
    }

    private void connectEditorListener() {
        openEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened courses list editor",
                        "connectEditorListener", getClass());
                stopPanelLoop();
                mainFrame.setCurrentPanel(new CoursesListEditor(mainFrame, mainMenu, professor, offlineModeDTO));
            }
        });
    }
}
