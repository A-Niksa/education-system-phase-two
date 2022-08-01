package client.gui.menus.profile;

import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.main.MainMenu;
import client.gui.utils.ImageParsingUtils;
import client.locallogic.profile.DepartmentGetter;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class ProfessorProfile extends PanelTemplate {
    private Professor professor;
    private JLabel profilePicture;
    private JLabel name;
    private JLabel nationalId;
    private JLabel professorId;
    private JLabel phoneNumber;
    private JLabel emailAddress;
    private JLabel department;
    private JLabel officeNumber;
    private JLabel academicLevel;
    private LinkedList<JLabel> labelsList;
    private JSeparator separator;
    private JLabel enterNewEmailAddress;
    private JTextField newEmailAddress;
    private JButton changeEmailAddress;
    private JLabel enterNewPhoneNumber;
    private JTextField newPhoneNumber;
    private JButton changePhoneNumber;

    public ProfessorProfile(MainFrame mainFrame, MainMenu mainMenu, User user) {
        super(mainFrame, mainMenu);
        professor = (Professor) user;
        configIdentifier = ConfigFileIdentifier.GUI_PROFILE;
        drawPanel();
    }

    @Override
    protected void initializeComponents() {
        ImageIcon profilePictureIcon = ImageParsingUtils.convertPictureToImageIcon(professor.getProfilePicture());
        profilePicture = new JLabel(profilePictureIcon);
        labelsList = new LinkedList<>();
        name = new JLabel(ConfigManager.getString(configIdentifier, "nameMessage") +
                professor.getFirstName() + " " + professor.getLastName());
        labelsList.add(name);
        nationalId = new JLabel(ConfigManager.getString(configIdentifier, "nationalIdMessage")
                + professor.getNationalId());
        labelsList.add(nationalId);
        professorId = new JLabel(ConfigManager.getString(configIdentifier, "professorIdMessage") + professor.getId());
        labelsList.add(professorId);
        phoneNumber = new JLabel(ConfigManager.getString(configIdentifier, "phoneNumberMessage")
                + professor.getPhoneNumber());
        labelsList.add(phoneNumber);
        emailAddress = new JLabel(ConfigManager.getString(configIdentifier, "emailAddressMessage")
                + professor.getEmailAddress());
        labelsList.add(emailAddress);
        department = new JLabel(ConfigManager.getString(configIdentifier, "departmentMessage")
                + DepartmentGetter.getDepartmentName(professor.getDepartmentId()));
        labelsList.add(department);
        officeNumber = new JLabel(ConfigManager.getString(configIdentifier, "officeNumberMessage") +
                professor.getOfficeNumber());
        labelsList.add(officeNumber);
        academicLevel = new JLabel(ConfigManager.getString(configIdentifier, "academicLevelMessage") +
                professor.getAcademicLevel());
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
                Response response = clientController.changePhoneNumber(professor.getId(), newPhoneNumberText);
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
                Response response = clientController.changeEmailAddress(professor.getId(), newEmailAddressText);
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    emailAddress.setText(ConfigManager.getString(configIdentifier, "emailAddressMessage")
                            + newEmailAddressText);
                }
            }
        });
    }
}
