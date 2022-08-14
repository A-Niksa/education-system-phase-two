package client.gui.menus.searching;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import client.gui.utils.EnumStringMappingUtils;
import client.gui.utils.ImageParsingUtils;
import shareables.network.DTOs.StudentDTO;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.util.ArrayList;

public class StudentProfileView extends DynamicPanelTemplate {
    private StudentDTO studentDTO;
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
    private JButton goBackButton;

    public StudentProfileView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, StudentDTO studentDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.studentDTO = studentDTO;
        configIdentifier = ConfigFileIdentifier.GUI_STUDENT_PROFILE_VIEW;
        labelsList = new ArrayList<>();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void updatePanel() {
        ImageIcon profilePictureIcon = ImageParsingUtils.convertPictureToScaledImageIcon(studentDTO.getProfilePicture(),
                ConfigManager.getInt(configIdentifier, "profilePictureW"),
                ConfigManager.getInt(configIdentifier, "profilePictureH"));
        profilePicture.setIcon(profilePictureIcon);
        name.setText(nameMessage + studentDTO.getName());
        nationalId.setText(nationalIdMessage + studentDTO.getNationalId());
        studentId.setText(studentIdMessage + studentDTO.getId());
        phoneNumber.setText(phoneNumberMessage + studentDTO.getPhoneNumber());
        emailAddress.setText(emailAddressMessage + studentDTO.getEmailAddress());
        totalGPA.setText(totalGPAMessage + studentDTO.getGPAString());
        department.setText(departmentMessage + studentDTO.getDepartmentName());
        advisingProfessor.setText(advisingProfessorMessage + studentDTO.getAdvisingProfessorName());
        yearOfEntry.setText(yearOfEntryMessage + studentDTO.getYearOfEntry());
        degreeLevel.setText(degreeLevelMessage + studentDTO.getDegreeLevel());
        studentStatus.setText(studentStatusMessage + studentDTO.getStudentStatus());
    }

    @Override
    protected void initializeComponents() {
        ImageIcon profilePictureIcon = ImageParsingUtils.convertPictureToScaledImageIcon(studentDTO.getProfilePicture(),
                ConfigManager.getInt(configIdentifier, "profilePictureW"),
                ConfigManager.getInt(configIdentifier, "profilePictureH"));
        profilePicture = new JLabel(profilePictureIcon);
        nameMessage = ConfigManager.getString(configIdentifier, "nameMessage");
        name = new JLabel(nameMessage + studentDTO.getName(), SwingConstants.CENTER);
        labelsList.add(name);
        nationalIdMessage = ConfigManager.getString(configIdentifier, "nationalIdMessage");
        nationalId = new JLabel(nationalIdMessage + studentDTO.getNationalId(), SwingConstants.CENTER);
        labelsList.add(nationalId);
        studentIdMessage = ConfigManager.getString(configIdentifier, "studentIdMessage");
        studentId = new JLabel(studentIdMessage + studentDTO.getId(), SwingConstants.CENTER);
        labelsList.add(studentId);
        phoneNumberMessage = ConfigManager.getString(configIdentifier, "phoneNumberMessage");
        phoneNumber = new JLabel(phoneNumberMessage + studentDTO.getPhoneNumber(), SwingConstants.CENTER);
        labelsList.add(phoneNumber);
        emailAddressMessage = ConfigManager.getString(configIdentifier, "emailAddressMessage");
        emailAddress = new JLabel(emailAddressMessage + studentDTO.getEmailAddress(), SwingConstants.CENTER);
        labelsList.add(emailAddress);
        totalGPAMessage = ConfigManager.getString(configIdentifier, "totalGPAMessage");
        totalGPA = new JLabel(totalGPAMessage + studentDTO.getGPAString(), SwingConstants.CENTER);
        labelsList.add(totalGPA);
        departmentMessage = ConfigManager.getString(configIdentifier, "departmentMessage");
        department = new JLabel(departmentMessage + studentDTO.getDepartmentName(), SwingConstants.CENTER);
        labelsList.add(department);
        advisingProfessorMessage = ConfigManager.getString(configIdentifier, "advisingProfessorMessage");
        advisingProfessor = new JLabel(advisingProfessorMessage + studentDTO.getAdvisingProfessorName(),
                SwingConstants.CENTER);
        labelsList.add(advisingProfessor);
        yearOfEntryMessage = ConfigManager.getString(configIdentifier, "yearOfEntryMessage");
        yearOfEntry = new JLabel(yearOfEntryMessage + studentDTO.getYearOfEntry(), SwingConstants.CENTER);
        labelsList.add(yearOfEntry);
        degreeLevelMessage = ConfigManager.getString(configIdentifier, "degreeLevelMessage");
        degreeLevel = new JLabel(degreeLevelMessage + studentDTO.getDegreeLevel(), SwingConstants.CENTER);
        labelsList.add(degreeLevel);
        studentStatusMessage = ConfigManager.getString(configIdentifier, "studentStatusMessage");
        studentStatus = new JLabel(studentStatusMessage + studentDTO.getStudentStatus(), SwingConstants.CENTER);
        labelsList.add(studentStatus);

        goBackButton = new JButton(ConfigManager.getString(configIdentifier, "goBackButtonM"));
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

        goBackButton.setBounds(ConfigManager.getInt(configIdentifier, "goBackButtonX"),
                ConfigManager.getInt(configIdentifier, "goBackButtonY"),
                ConfigManager.getInt(configIdentifier, "goBackButtonW"),
                ConfigManager.getInt(configIdentifier, "goBackButtonH"));
        add(goBackButton);
    }

    @Override
    protected void connectListeners() {
        goBackButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Went back to student searching section",
                    "connectListeners", getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(new MrMohseniSearcher(mainFrame, mainMenu, offlineModeDTO));
        });
    }
}
