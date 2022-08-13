package client.gui.menus.profile;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.OfflinePanel;
import client.gui.menus.main.MainMenu;
import client.gui.utils.EnumStringMappingUtils;
import client.gui.utils.ImageParsingUtils;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StudentProfile extends DynamicPanelTemplate implements OfflinePanel {
    private JLabel profilePicture;
    private JLabel name;
    private JLabel nationalId;
    private JLabel studentId;
    private JLabel phoneNumber;
    private JLabel emailAddress;
    private JLabel totalGPA;
    private JLabel department;
    private JLabel advisingProfessor;
    private JLabel yearOfEntry;
    private JLabel degreeLevel;
    private JLabel studentStatus;
    private ArrayList<JLabel> labelsList;
    private JSeparator separator;
    private JLabel enterNewEmailAddress;
    private JTextField newEmailAddress;
    private JButton changeEmailAddress;
    private JLabel enterNewPhoneNumber;
    private JTextField newPhoneNumber;
    private JButton changePhoneNumber;
    private String nameMessage;
    private String nationalIdMessage;
    private String studentIdMessage;
    private String phoneNumberMessage;
    private String emailAddressMessage;
    private String totalGPAMessage;
    private String departmentMessage;
    private String advisingProfessorMessage;
    private String yearOfEntryMessage;
    private String degreeLevelMessage;
    private String studentStatusMessage;

    public StudentProfile(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO,
                          boolean isOnline) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.isOnline = isOnline;
        configIdentifier = ConfigFileIdentifier.GUI_PROFILE;
        labelsList = new ArrayList<>();
        drawPanel();
        startPingingIfOnline(offlineModeDTO.getId(), this);
    }

    @Override
    protected void initializeComponents() {
        ImageIcon profilePictureIcon = ImageParsingUtils.convertPictureToScaledImageIcon(offlineModeDTO.getProfilePicture(),
                ConfigManager.getInt(configIdentifier, "profilePictureW"),
                ConfigManager.getInt(configIdentifier, "profilePictureH"));
        profilePicture = new JLabel(profilePictureIcon);
        nameMessage = ConfigManager.getString(configIdentifier, "nameMessage");
        name = new JLabel(nameMessage + offlineModeDTO.getName());
        labelsList.add(name);
        nationalIdMessage = ConfigManager.getString(configIdentifier, "nationalIdMessage");
        nationalId = new JLabel(nationalIdMessage + offlineModeDTO.getNationalId());
        labelsList.add(nationalId);
        studentIdMessage = ConfigManager.getString(configIdentifier, "studentIdMessage");
        studentId = new JLabel(studentIdMessage + offlineModeDTO.getId());
        labelsList.add(studentId);
        phoneNumberMessage = ConfigManager.getString(configIdentifier, "phoneNumberMessage");
        phoneNumber = new JLabel(phoneNumberMessage + offlineModeDTO.getPhoneNumber());
        labelsList.add(phoneNumber);
        emailAddressMessage = ConfigManager.getString(configIdentifier, "emailAddressMessage");
        emailAddress = new JLabel(emailAddressMessage + offlineModeDTO.getEmailAddress());
        labelsList.add(emailAddress);
        totalGPAMessage = ConfigManager.getString(configIdentifier, "totalGPAMessage");
        totalGPA = new JLabel(totalGPAMessage + offlineModeDTO.getGPAString());
        labelsList.add(totalGPA);
        departmentMessage = ConfigManager.getString(configIdentifier, "departmentMessage");
        department = new JLabel(departmentMessage + EnumStringMappingUtils.getDepartmentName(offlineModeDTO.getDepartmentId()));
        labelsList.add(department);
        advisingProfessorMessage = ConfigManager.getString(configIdentifier, "advisingProfessorMessage");
        advisingProfessor = new JLabel(advisingProfessorMessage + offlineModeDTO.getAdvisingProfessorName());
        labelsList.add(advisingProfessor);
        yearOfEntryMessage = ConfigManager.getString(configIdentifier, "yearOfEntryMessage");
        yearOfEntry = new JLabel(yearOfEntryMessage + offlineModeDTO.getYearOfEntry());
        labelsList.add(yearOfEntry);
        degreeLevelMessage = ConfigManager.getString(configIdentifier, "degreeLevelMessage");
        degreeLevel = new JLabel(degreeLevelMessage + offlineModeDTO.getDegreeLevel());
        labelsList.add(degreeLevel);
        studentStatusMessage = ConfigManager.getString(configIdentifier, "studentStatusMessage");
        studentStatus = new JLabel(studentStatusMessage + offlineModeDTO.getStudentStatus());
        labelsList.add(studentStatus);
        separator = new JSeparator();
        enterNewEmailAddress = new JLabel(ConfigManager.getString(configIdentifier, "enterNewEmailAddressMessage"));
        newEmailAddress = new JTextField();
        changeEmailAddress = new JButton(ConfigManager.getString(configIdentifier, "changeMessage"));
        enterNewPhoneNumber = new JLabel(ConfigManager.getString(configIdentifier, "enterNewPhoneNumberMessage"));
        newPhoneNumber = new JTextField();
        changePhoneNumber = new JButton(ConfigManager.getString(configIdentifier, "changeMessage"));
    }

    @Override
    protected void alignComponents() {
        profilePicture.setBounds(ConfigManager.getInt(configIdentifier, "profilePictureX"),
                ConfigManager.getInt(configIdentifier, "profilePictureY"),
                ConfigManager.getInt(configIdentifier, "profilePictureW"),
                ConfigManager.getInt(configIdentifier, "profilePictureH"));
        add(profilePicture);

        int currentX = ConfigManager.getInt(configIdentifier, "labelCurrentX");
        int currentY = ConfigManager.getInt(configIdentifier, "labelCurrentY");
        int increment = ConfigManager.getInt(configIdentifier, "labelInc");
        int labelWidth = ConfigManager.getInt(configIdentifier, "labelW");
        int labelHeight = ConfigManager.getInt(configIdentifier, "labelH");
        for (JLabel label : labelsList) {
            label.setBounds(currentX, currentY, labelWidth, labelHeight);
            add(label);
            currentY += increment;
        }

        separator.setOrientation(SwingConstants.VERTICAL);
        separator.setBackground(Color.BLACK);
        separator.setForeground(Color.BLACK);
        separator.setBounds(ConfigManager.getInt(configIdentifier, "separatorX"),
                ConfigManager.getInt(configIdentifier, "separatorY"),
                ConfigManager.getInt(configIdentifier, "separatorW"),
                ConfigManager.getInt(configIdentifier, "separatorH"));
        add(separator);
        enterNewPhoneNumber.setBounds(ConfigManager.getInt(configIdentifier, "enterNewPhoneNumberX"),
                ConfigManager.getInt(configIdentifier, "enterNewPhoneNumberY"),
                ConfigManager.getInt(configIdentifier, "enterNewPhoneNumberW"),
                ConfigManager.getInt(configIdentifier, "enterNewPhoneNumberH"));
        add(enterNewPhoneNumber);
        newPhoneNumber.setBounds(ConfigManager.getInt(configIdentifier, "newPhoneNumberX"),
                ConfigManager.getInt(configIdentifier, "newPhoneNumberY"),
                ConfigManager.getInt(configIdentifier, "newPhoneNumberW"),
                ConfigManager.getInt(configIdentifier, "newPhoneNumberH"));
        add(newPhoneNumber);
        changePhoneNumber.setBounds(ConfigManager.getInt(configIdentifier, "changePhoneNumberX"),
                ConfigManager.getInt(configIdentifier, "changePhoneNumberY"),
                ConfigManager.getInt(configIdentifier, "changePhoneNumberW"),
                ConfigManager.getInt(configIdentifier, "changePhoneNumberH"));
        add(changePhoneNumber);
        enterNewEmailAddress.setBounds(ConfigManager.getInt(configIdentifier, "enterNewEmailAddressX"),
                ConfigManager.getInt(configIdentifier, "enterNewEmailAddressY"),
                ConfigManager.getInt(configIdentifier, "enterNewEmailAddressW"),
                ConfigManager.getInt(configIdentifier, "enterNewEmailAddressH"));
        add(enterNewEmailAddress);
        newEmailAddress.setBounds(ConfigManager.getInt(configIdentifier, "newEmailAddressX"),
                ConfigManager.getInt(configIdentifier, "newEmailAddressY"),
                ConfigManager.getInt(configIdentifier, "newEmailAddressW"),
                ConfigManager.getInt(configIdentifier, "newEmailAddressH"));
        add(newEmailAddress);
        changeEmailAddress.setBounds(ConfigManager.getInt(configIdentifier, "changeEmailAddressX"),
                ConfigManager.getInt(configIdentifier, "changeEmailAddressY"),
                ConfigManager.getInt(configIdentifier, "changeEmailAddressW"),
                ConfigManager.getInt(configIdentifier, "changeEmailAddressH"));
        add(changeEmailAddress);
    }

    @Override
    protected void connectListeners() {
        changePhoneNumber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String newPhoneNumberText = newPhoneNumber.getText();
                MasterLogger.clientInfo(clientController.getId(), "Changed phone number to " + newPhoneNumberText,
                        "connectListeners", getClass());
                Response response = clientController.changePhoneNumber(offlineModeDTO.getId(), newPhoneNumberText);
                if (response == null) return;
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    phoneNumber.setText(ConfigManager.getString(configIdentifier, "phoneNumberMessage")
                            + newPhoneNumberText);
                }
            }
        });

        changeEmailAddress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String newEmailAddressText = newEmailAddress.getText();
                MasterLogger.clientInfo(clientController.getId(), "Changed email address to " + newEmailAddressText,
                        "connectListeners",  getClass());
                Response response = clientController.changeEmailAddress(offlineModeDTO.getId(), newEmailAddressText);
                if (response == null) return;
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    emailAddress.setText(ConfigManager.getString(configIdentifier, "emailAddressMessage")
                            + newEmailAddressText);
                }
            }
        });
    }

    @Override
    protected void updatePanel() {
        ImageIcon profilePictureIcon = ImageParsingUtils.convertPictureToScaledImageIcon(offlineModeDTO.getProfilePicture(),
                ConfigManager.getInt(configIdentifier, "profilePictureW"),
                ConfigManager.getInt(configIdentifier, "profilePictureH"));
        profilePicture.setIcon(profilePictureIcon);
        name.setText(nameMessage + offlineModeDTO.getName());
        nationalId.setText(nationalIdMessage + offlineModeDTO.getNationalId());
        studentId.setText(studentIdMessage + offlineModeDTO.getId());
        phoneNumber.setText(phoneNumberMessage + offlineModeDTO.getPhoneNumber());
        emailAddress.setText(emailAddressMessage + offlineModeDTO.getEmailAddress());
        totalGPA.setText(totalGPAMessage + offlineModeDTO.getGPAString());
        department.setText(departmentMessage + EnumStringMappingUtils.getDepartmentName(offlineModeDTO.getDepartmentId()));
        advisingProfessor.setText(advisingProfessorMessage + offlineModeDTO.getAdvisingProfessorName());
        yearOfEntry.setText(yearOfEntryMessage + offlineModeDTO.getYearOfEntry());
        degreeLevel.setText(degreeLevelMessage + offlineModeDTO.getDegreeLevel());
        studentStatus.setText(studentStatusMessage + offlineModeDTO.getStudentStatus());
    }

    @Override
    public void disableOnlineComponents() {
        stopPanelLoop();
        changeEmailAddress.setEnabled(false);
        changePhoneNumber.setEnabled(false);
    }

    @Override
    public void enableOnlineComponents() {
        restartPanelLoop();
        changeEmailAddress.setEnabled(true);
        changePhoneNumber.setEnabled(true);
    }
}
