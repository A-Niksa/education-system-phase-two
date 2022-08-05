package client.gui.menus.enrolment.management;

import client.gui.MainFrame;
import client.gui.menus.enrolment.editing.ProfessorsListEditor;
import client.gui.menus.enrolment.viewing.ProfessorsListView;
import client.gui.menus.main.MainMenu;
import shareables.models.pojos.users.professors.Professor;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfessorsListManager extends ProfessorsListView {
    private Professor professor;
    private JButton openEditor;

    public ProfessorsListManager(MainFrame mainFrame, MainMenu mainMenu, Professor professor) {
        super(mainFrame, mainMenu);
        this.professor = professor;
        configIdentifier = ConfigFileIdentifier.GUI_LIST_MANAGER;
        drawPanel();
        setEditorButton();
        connectEditor();
    }

    private void setEditorButton() {
        openEditor = new JButton(ConfigManager.getString(configIdentifier, "openEditorM"));
        openEditor.setBounds(ConfigManager.getInt(configIdentifier, "openEditorX"),
                ConfigManager.getInt(configIdentifier, "openEditorY"),
                ConfigManager.getInt(configIdentifier, "openEditorW"),
                ConfigManager.getInt(configIdentifier, "openEditorH"));
        add(openEditor);
    }

    private void connectEditor() {
        openEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened professors list editor",
                        "connectEditor",getClass());
                mainFrame.setCurrentPanel(new ProfessorsListEditor(mainFrame, mainMenu, professor));
            }
        });
    }
}
