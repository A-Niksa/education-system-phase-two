package client.gui.menus.addition;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.main.MainMenu;
import client.gui.utils.EnumArrayUtils;
import client.locallogic.addition.BlueprintGenerator;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.OfflineModeDTO;
import shareables.network.blueprints.Blueprint;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StudentAdder extends DynamicPanelTemplate {
    private Professor professor;
    private JTextField passwordField;
    private JTextField nationalIdField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneNumberField;
    private JTextField emailAddressField;
    private JTextField yearOfEntryField;
    private String[] studentStatusStrings;
    private JComboBox<String> studentStatusBox;
    private String[] degreeLevels;
    private JComboBox<String> degreeLevelBox;
    private String[] professorNames;
    private JComboBox<String> advisingProfessorNameBox;
    private ArrayList<JTextField> textFieldsList;
    private ArrayList<JComboBox<String>> comboBoxesList;
    private JButton addStudentButton;

    public StudentAdder(MainFrame mainFrame, MainMenu mainMenu, Professor professor, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.professor = professor;
        configIdentifier = ConfigFileIdentifier.GUI_STUDENT_ADDER;
        studentStatusStrings = EnumArrayUtils.initializeStudentStatusStrings();
        degreeLevels = EnumArrayUtils.initializeDegreeLevels();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    private void updateDepartmentProfessorNames() {
        Response response = clientController.getDepartmentProfessorNames(offlineModeDTO.getDepartmentId());
        professorNames = (String[]) response.get("stringArray");
    }

    @Override
    protected void initializeComponents() {
        textFieldsList = new ArrayList<>();
        comboBoxesList = new ArrayList<>();

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
        yearOfEntryField = new JTextField(ConfigManager.getString(configIdentifier, "yearOfEntryFieldM"));
        textFieldsList.add(yearOfEntryField);

        studentStatusBox = new JComboBox<>(studentStatusStrings);
        comboBoxesList.add(studentStatusBox);
        degreeLevelBox = new JComboBox<>(degreeLevels);
        comboBoxesList.add(degreeLevelBox);

        updateDepartmentProfessorNames();
        advisingProfessorNameBox = new JComboBox<>(professorNames);
        comboBoxesList.add(advisingProfessorNameBox);

        addStudentButton = new JButton(ConfigManager.getString(configIdentifier, "addStudentButtonM"));
    }

    @Override
    protected void alignComponents() {
        int currentX = 350;
        int currentY = 100;
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
        addStudentButton.setBounds(currentX + incrementOfX, currentY + smallerIncrementOfY,
                ConfigManager.getInt(configIdentifier, "addStudentButtonW"),
                ConfigManager.getInt(configIdentifier, "addStudentButtonH"));
        add(addStudentButton);
    }

    @Override
    protected void connectListeners() {
        addStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String password = passwordField.getText();
                String nationalId = nationalIdField.getText();
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String phoneNumber = phoneNumberField.getText();
                String emailAddress = emailAddressField.getText();
                int yearOfEntry = Integer.parseInt(yearOfEntryField.getText());
                String studentStatusString = (String) studentStatusBox.getSelectedItem();
                String degreeLevelString = (String) degreeLevelBox.getSelectedItem();
                String advisingProfessorName = (String) advisingProfessorNameBox.getSelectedItem();

                Blueprint studentBlueprint = BlueprintGenerator.generateStudentBlueprint(password, nationalId, firstName,
                        lastName, phoneNumber, emailAddress, yearOfEntry, studentStatusString, degreeLevelString,
                        advisingProfessorName, offlineModeDTO.getDepartmentId());

                Response response = clientController.addStudent(studentBlueprint);
                if (response.getResponseStatus() == ResponseStatus.OK) {
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
