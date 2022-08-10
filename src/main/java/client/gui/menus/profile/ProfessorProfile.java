package client.gui.menus.profile;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.OfflinePanel;
import client.gui.PanelTemplate;
import client.gui.menus.main.MainMenu;
import client.gui.utils.ImageParsingUtils;
import client.locallogic.profile.DepartmentGetter;
import server.network.clienthandling.logicutils.general.EnumStringMappingUtils;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.OfflineModeDTO;
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

public class ProfessorProfile extends DynamicPanelTemplate implements OfflinePanel {
    private JLabel profilePicture;
    private JLabel name;
    private JLabel nationalId;
    private JLabel professorId;
    private JLabel phoneNumber;
    private JLabel emailAddress;
    private JLabel department;
    private JLabel officeNumber;
    private JLabel academicLevel;
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
    private String professorIdMessage;
    private String phoneNumberMessage;
    private String emailAddressMessage;
    private String departmentMessage;
    private String officeNumberMessage;
    private String academicLevelMessage;

    public ProfessorProfile(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO,
                            boolean isOnline) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.isOnline = isOnline;
        configIdentifier = ConfigFileIdentifier.GUI_PROFILE;
        drawPanel();
        startPingingIfOnline(offlineModeDTO.getId(), this);
    }

    @Override
    protected void initializeComponents() {
        ImageIcon profilePictureIcon = ImageParsingUtils.convertPictureToImageIcon(offlineModeDTO.getProfilePicture());
        profilePicture = new JLabel(profilePictureIcon);
        labelsList = new ArrayList<>();
        nameMessage = ConfigManager.getString(configIdentifier, "nameMessage");
        name = new JLabel(nameMessage + offlineModeDTO.getName());
        labelsList.add(name);
        nationalIdMessage = ConfigManager.getString(configIdentifier, "nationalIdMessage");
        nationalId = new JLabel(nationalIdMessage + offlineModeDTO.getNationalId());
        labelsList.add(nationalId);
        professorIdMessage = ConfigManager.getString(configIdentifier, "professorIdMessage");
        professorId = new JLabel(professorIdMessage + offlineModeDTO.getId());
        labelsList.add(professorId);
        phoneNumberMessage = ConfigManager.getString(configIdentifier, "phoneNumberMessage");
        phoneNumber = new JLabel(phoneNumberMessage + offlineModeDTO.getPhoneNumber());
        labelsList.add(phoneNumber);
        emailAddressMessage = ConfigManager.getString(configIdentifier, "emailAddressMessage");
        emailAddress = new JLabel(emailAddressMessage + offlineModeDTO.getEmailAddress());
        labelsList.add(emailAddress);
        departmentMessage = ConfigManager.getString(configIdentifier, "departmentMessage");
        department = new JLabel(departmentMessage +
                EnumStringMappingUtils.getDepartmentName(offlineModeDTO.getDepartmentId()));
        labelsList.add(department);
        officeNumberMessage = ConfigManager.getString(configIdentifier, "officeNumberMessage");
        officeNumber = new JLabel(officeNumberMessage + offlineModeDTO.getOfficeNumber());
        labelsList.add(officeNumber);
        academicLevelMessage = ConfigManager.getString(configIdentifier, "academicLevelMessage");
        academicLevel = new JLabel(academicLevelMessage + offlineModeDTO.getAcademicLevel());
        labelsList.add(academicLevel);
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

        int currentX = ConfigManager.getInt(configIdentifier, "professorLabelCurrentX");
        int currentY = ConfigManager.getInt(configIdentifier, "professorLabelCurrentY");
        int increment = ConfigManager.getInt(configIdentifier, "professorLabelInc");
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
        ImageIcon profilePictureIcon = ImageParsingUtils.convertPictureToImageIcon(offlineModeDTO.getProfilePicture());
        profilePicture.setIcon(profilePictureIcon);
        name.setText(nameMessage + offlineModeDTO.getName());
        nationalId.setText(nationalIdMessage + offlineModeDTO.getNationalId());
        professorId.setText(professorIdMessage + offlineModeDTO.getId());
        phoneNumber.setText(phoneNumberMessage + offlineModeDTO.getPhoneNumber());
        emailAddress.setText(emailAddressMessage + offlineModeDTO.getEmailAddress());
        department.setText(departmentMessage + EnumStringMappingUtils.getDepartmentName(offlineModeDTO.getDepartmentId()));
        officeNumber.setText(officeNumberMessage + offlineModeDTO.getOfficeNumber());
        academicLevel.setText(academicLevelMessage + offlineModeDTO.getAcademicLevel());
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
