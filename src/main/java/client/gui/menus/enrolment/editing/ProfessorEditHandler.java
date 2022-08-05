package client.gui.menus.enrolment.editing;

import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.ProfessorDTO;
import shareables.utils.logging.MasterLogger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfessorEditHandler implements ActionListener {
    private MainFrame mainFrame;
    private MainMenu mainMenu;
    private Professor dean;
    private ProfessorDTO correspondingProfessorDTO;
    private int clientControllerId;

    public ProfessorEditHandler(MainFrame mainFrame, MainMenu mainMenu, Professor dean,
                                ProfessorDTO correspondingProfessorDTO) {
        this.mainFrame = mainFrame;
        this.mainMenu = mainMenu;
        this.dean = dean;
        this.correspondingProfessorDTO = correspondingProfessorDTO;
        clientControllerId = mainFrame.getClientController().getId();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String professorName = correspondingProfessorDTO.getName();
        MasterLogger.clientInfo(clientControllerId, "Opened professor editor for " + professorName,
                "actionPerformed", getClass());
        mainFrame.setCurrentPanel(new ProfessorEditor(mainFrame, mainMenu, dean, correspondingProfessorDTO));
    }
}
