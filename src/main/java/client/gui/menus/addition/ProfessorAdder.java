package client.gui.menus.addition;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import client.gui.utils.EnumArrayUtils;
import client.gui.utils.ErrorUtils;
import client.locallogic.menus.addition.BlueprintGenerator;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.blueprints.Blueprint;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ProfessorAdder extends DynamicPanelTemplate {
    private JTextField passwordField;
    private JTextField nationalIdField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneNumberField;
    private JTextField emailAddressField;
    private JTextField officeNumberField;
    private JTextField adviseeStudentIdsField;
    private String[] academicLevels;
    private JComboBox<String> academicLevelBox;
    private ArrayList<JTextField> textFieldsList;
    private ArrayList<JComboBox<String>> comboBoxesList;
    private JButton addProfessorButton;

    public ProfessorAdder(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        configIdentifier = ConfigFileIdentifier.GUI_PROFESSOR_ADDER;
        academicLevels = EnumArrayUtils.initializeAcademicLevels();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void initializeComponents() {
        textFieldsList = new ArrayList<>();
        comboBoxesList = new ArrayList<>();

        // TODO: adding profile picture?
        passwordField = new JTextField(ConfigManager.getString(configIdentifier, "passwordFieldM"));
        textFieldsList.add(passwordField);
        nationalIdField = new JTextField(ConfigManager.getString(configIdentifier, "nationalIdFieldM"));
        textFieldsList.add(nationalIdField);
        firstNameField = new JTextField(ConfigManager.getString(configIdentifier, "firstNameFieldM"));
        textFieldsList.add(firstNameField);
        lastNameField = new JTextField(ConfigManager.getString(configIdentifier, "lastNameFieldM"));
        textFieldsList.add(lastNameField);
        phoneNumberField = new JTextField(ConfigManager.getString(configIdentifier, "phoneNumberFieldM"));
        textFieldsList.add(phoneNumberField);
        emailAddressField = new JTextField(ConfigManager.getString(configIdentifier, "emailAddressFieldM"));
        textFieldsList.add(emailAddressField);
        officeNumberField = new JTextField(ConfigManager.getString(configIdentifier, "officeNumberFieldM"));
        textFieldsList.add(officeNumberField);
        adviseeStudentIdsField = new JTextField(ConfigManager.getString(configIdentifier, "adviseeStudentIdsFieldM"));
        textFieldsList.add(adviseeStudentIdsField);

        academicLevelBox = new JComboBox<>(academicLevels);
        comboBoxesList.add(academicLevelBox);

        addProfessorButton = new JButton(ConfigManager.getString(configIdentifier, "addProfessorButtonM"));
    }

    @Override
    protected void alignComponents() {
        int currentX = ConfigManager.getInt(configIdentifier, "startingX");
        int currentY = ConfigManager.getInt(configIdentifier, "startingY");
        int incrementOfX = ConfigManager.getInt(configIdentifier, "incX");
        int incrementOfY = ConfigManager.getInt(configIdentifier, "incY");
        int smallerIncrementOfY = ConfigManager.getInt(configIdentifier, "smallerIncY");
        int componentWidth = ConfigManager.getInt(configIdentifier, "componentW");
        int componentHeight = ConfigManager.getInt(configIdentifier, "componentH");
        for (JTextField textField : textFieldsList) {
            textField.setBounds(currentX, currentY, componentWidth, componentHeight);
            add(textField);
            currentY += incrementOfY;
        }
        for (JComboBox<String> comboBox : comboBoxesList) {
            comboBox.setBounds(currentX, currentY, componentWidth, componentHeight);
            add(comboBox);
            currentY += incrementOfY;
        }
        addProfessorButton.setBounds(currentX + incrementOfX, currentY + smallerIncrementOfY,
                ConfigManager.getInt(configIdentifier, "addProfessorButtonW"),
                ConfigManager.getInt(configIdentifier, "addProfessorButtonH"));
        add(addProfessorButton);
    }

    @Override
    protected void connectListeners() {
        addProfessorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String password = passwordField.getText();
                String nationalID = nationalIdField.getText();
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String phoneNumber = phoneNumberField.getText();
                String emailAddress = emailAddressField.getText();
                String officeNumber = officeNumberField.getText();
                String[] adviseeStudentIds = adviseeStudentIdsField.getText().split(", ");
                String academicRankString = (String) academicLevelBox.getSelectedItem();

                Blueprint blueprint = BlueprintGenerator.generateProfessorBlueprint(password, nationalID, firstName, lastName,
                        phoneNumber, emailAddress, officeNumber, adviseeStudentIds, academicRankString,
                        offlineModeDTO.getDepartmentId());

                Response response = clientController.addProfessor(blueprint);
                if (response == null) return;
                if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                    MasterLogger.clientError(clientController.getId(), response.getErrorMessage(),
                            "connectListeners", getClass());
                } else {
                    JOptionPane.showMessageDialog(mainFrame, response.getUnsolicitedMessage());
                    MasterLogger.clientInfo(clientController.getId(), response.getUnsolicitedMessage(),
                            "connectListeners", getClass());
                }
            }
        });
    }

    @Override
    protected void updatePanel() {

    }
}
