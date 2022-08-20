package client.gui.menus.enrolment.editing;

import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.DTOs.generalmodels.ProfessorDTO;
import shareables.utils.logging.MasterLogger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfessorEditHandler implements ActionListener {
    private MainFrame mainFrame;
    private MainMenu mainMenu;
    private ProfessorsListEditor professorsListEditor;
    private ProfessorDTO correspondingProfessorDTO;
    private OfflineModeDTO offlineModeDTO;
    private int clientControllerId;

    public ProfessorEditHandler(MainFrame mainFrame, MainMenu mainMenu, ProfessorsListEditor professorsListEditor,
                                ProfessorDTO correspondingProfessorDTO, OfflineModeDTO offlineModeDTO) {
        this.mainFrame = mainFrame;
        this.mainMenu = mainMenu;
        this.professorsListEditor = professorsListEditor;
        this.correspondingProfessorDTO = correspondingProfessorDTO;
        this.offlineModeDTO = offlineModeDTO;
        clientControllerId = mainFrame.getClientController().getId();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String professorName = correspondingProfessorDTO.getName();
        MasterLogger.clientInfo(clientControllerId, "Opened professor editor for " + professorName,
                "actionPerformed", getClass());
        professorsListEditor.stopPanelLoop();
        mainFrame.setCurrentPanel(new ProfessorEditor(mainFrame, mainMenu, correspondingProfessorDTO, offlineModeDTO));
    }
}
