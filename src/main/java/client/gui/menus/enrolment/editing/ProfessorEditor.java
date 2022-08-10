package client.gui.menus.enrolment.editing;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.main.MainMenu;
import client.gui.utils.EnumArrayUtils;
import client.gui.utils.ErrorUtils;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.OfflineModeDTO;
import shareables.network.DTOs.ProfessorDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfessorEditor extends DynamicPanelTemplate {
    private Professor dean;
    private ProfessorDTO professorToEditDTO;
    private JButton goBackButton;
    private JLabel professorName;
    private String[] academicLevels;
    private JComboBox<String> newAcademicLevel;
    private JButton changeAcademicLevel;
    private JTextField newOfficeNumber;
    private JButton changeOfficeNumber;
    private JButton demoteFromDeputy;
    private JButton promoteToDeputy;
    private JButton removeProfessor;

    public ProfessorEditor(MainFrame mainFrame, MainMenu mainMenu, Professor dean, ProfessorDTO professorToEditDTO,
                           OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.dean = dean;
        this.professorToEditDTO = professorToEditDTO;
        configIdentifier = ConfigFileIdentifier.GUI_PROFESSOR_EDITOR;
        academicLevels = EnumArrayUtils.initializeAcademicLevels();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void initializeComponents() {
        goBackButton = new JButton(ConfigManager.getString(configIdentifier, "goBackButtonM"));
        professorName = new JLabel(professorToEditDTO.getName(), SwingConstants.CENTER);
        newAcademicLevel = new JComboBox<>(academicLevels);
        changeAcademicLevel = new JButton(ConfigManager.getString(configIdentifier, "changeAcademicLevelM"));
        newOfficeNumber = new JTextField(ConfigManager.getString(configIdentifier, "newOfficeNumberM"));
        changeOfficeNumber = new JButton(ConfigManager.getString(configIdentifier, "changeOfficeNumberM"));
        demoteFromDeputy = new JButton(ConfigManager.getString(configIdentifier, "demoteFromDeputyM"));
        promoteToDeputy = new JButton(ConfigManager.getString(configIdentifier, "promoteToDeputyM"));
        removeProfessor = new JButton(ConfigManager.getString(configIdentifier, "removeProfessorM"));
    }

    @Override
    protected void alignComponents() {
        goBackButton.setBounds(ConfigManager.getInt(configIdentifier, "goBackButtonX"),
                ConfigManager.getInt(configIdentifier, "goBackButtonY"),
                ConfigManager.getInt(configIdentifier, "goBackButtonW"),
                ConfigManager.getInt(configIdentifier, "goBackButtonH"));
        add(goBackButton);
        // TODO: perhaps adding advising professor editing?
        professorName.setBounds(ConfigManager.getInt(configIdentifier, "professorNameX"),
                ConfigManager.getInt(configIdentifier, "professorNameY"),
                ConfigManager.getInt(configIdentifier, "professorNameW"),
                ConfigManager.getInt(configIdentifier, "professorNameH"));
        professorName.setFont(new Font("", Font.BOLD,
                ConfigManager.getInt(configIdentifier, "professorNameFontSize")));
        add(professorName);

        newAcademicLevel.setBounds(ConfigManager.getInt(configIdentifier, "newAcademicLevelX"),
                ConfigManager.getInt(configIdentifier, "newAcademicLevelY"),
                ConfigManager.getInt(configIdentifier, "newAcademicLevelW"),
                ConfigManager.getInt(configIdentifier, "newAcademicLevelH"));
        add(newAcademicLevel);
        changeAcademicLevel.setBounds(ConfigManager.getInt(configIdentifier, "changeAcademicLevelX"),
                ConfigManager.getInt(configIdentifier, "changeAcademicLevelY"),
                ConfigManager.getInt(configIdentifier, "changeAcademicLevelW"),
                ConfigManager.getInt(configIdentifier, "changeAcademicLevelH"));
        add(changeAcademicLevel);

        newOfficeNumber.setBounds(ConfigManager.getInt(configIdentifier, "newOfficeNumberX"),
                ConfigManager.getInt(configIdentifier, "newOfficeNumberY"),
                ConfigManager.getInt(configIdentifier, "newOfficeNumberW"),
                ConfigManager.getInt(configIdentifier, "newOfficeNumberH"));
        add(newOfficeNumber);
        changeOfficeNumber.setBounds(ConfigManager.getInt(configIdentifier, "changeOfficeNumberX"),
                ConfigManager.getInt(configIdentifier, "changeOfficeNumberY"),
                ConfigManager.getInt(configIdentifier, "changeOfficeNumberW"),
                ConfigManager.getInt(configIdentifier, "changeOfficeNumberH"));
        add(changeOfficeNumber);

        demoteFromDeputy.setBounds(ConfigManager.getInt(configIdentifier, "demoteFromDeputyX"),
                ConfigManager.getInt(configIdentifier, "demoteFromDeputyY"),
                ConfigManager.getInt(configIdentifier, "demoteFromDeputyW"),
                ConfigManager.getInt(configIdentifier, "demoteFromDeputyH"));
        add(demoteFromDeputy);
        promoteToDeputy.setBounds(ConfigManager.getInt(configIdentifier, "promoteToDeputyX"),
                ConfigManager.getInt(configIdentifier, "promoteToDeputyY"),
                ConfigManager.getInt(configIdentifier, "promoteToDeputyW"),
                ConfigManager.getInt(configIdentifier, "promoteToDeputyH"));
        add(promoteToDeputy);
        removeProfessor.setBounds(ConfigManager.getInt(configIdentifier, "removeProfessorX"),
                ConfigManager.getInt(configIdentifier, "removeProfessorY"),
                ConfigManager.getInt(configIdentifier, "removeProfessorW"),
                ConfigManager.getInt(configIdentifier, "removeProfessorH"));
        add(removeProfessor);
    }

    @Override
    protected void connectListeners() {
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Went back to professors list editor",
                        "connectListeners", getClass());
                stopPanelLoop();
                mainFrame.setCurrentPanel(new ProfessorsListEditor(mainFrame, mainMenu, dean, offlineModeDTO));
            }
        });

        changeAcademicLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String selectedAcademicLevel = (String) newAcademicLevel.getSelectedItem();
                Response response = clientController.changeProfessorAcademicLevel(professorToEditDTO.getId(),
                        selectedAcademicLevel);
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    MasterLogger.clientInfo(clientController.getId(), professorName.getText() + "'s academic level" +
                            " set to " + selectedAcademicLevel, "connectListeners", getClass());
                }
            }
        });

        // TODO: adding advisees editor?

        changeOfficeNumber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String selectedOfficeNumber = newOfficeNumber.getText();
                Response response = clientController.changeProfessorOfficeNumber(professorToEditDTO.getId(),
                        selectedOfficeNumber);
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    MasterLogger.clientInfo(clientController.getId(), professorName.getText() +
                            "'s office number set to " + selectedOfficeNumber, "connectListeners", getClass());
                }
            }
        });

        demoteFromDeputy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Response response = clientController.demoteProfessorFromDeputyRole(professorToEditDTO.getId(),
                        offlineModeDTO.getDepartmentId()); // we get the department id from the operating dean of the department
                if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                    MasterLogger.clientError(clientController.getId(), response.getErrorMessage(),
                            "connectListeners", getClass());
                } else {
                    MasterLogger.clientInfo(clientController.getId(), "Selected professor demoted to normal professor",
                            "connectListeners", getClass());
                }
            }
        });

        promoteToDeputy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Response response = clientController.promoteProfessorToDeputyRole(professorToEditDTO.getId(),
                        offlineModeDTO.getDepartmentId());
                if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                    MasterLogger.clientError(clientController.getId(), response.getErrorMessage(),
                            "connectListeners", getClass());
                } else {
                    MasterLogger.clientInfo(clientController.getId(), "Selected professor promoted to deputy",
                            "connectListeners", getClass());
                }
            }
        });

        removeProfessor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Response response = clientController.removeProfessor(professorToEditDTO.getId(),
                        offlineModeDTO.getDepartmentId());
                if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                    MasterLogger.clientError(clientController.getId(), response.getErrorMessage(),
                            "connectListeners", getClass());
                } else if (response.getUnsolicitedMessage() != null)  {
                    JOptionPane.showMessageDialog(mainFrame, response.getUnsolicitedMessage());
                    MasterLogger.clientInfo(clientController.getId(), response.getUnsolicitedMessage(),
                            "connectListeners", getClass());
                } else {
                    MasterLogger.clientInfo(clientController.getId(), "Removed the selected professor",
                            "connectListeners", getClass());
                }
            }
        });
    }

    @Override
    protected void updatePanel() {

    }
}