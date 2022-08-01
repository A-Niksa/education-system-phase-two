package client.gui.menus.profile;

import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.main.MainMenu;
import client.gui.utils.ImageParsingUtils;
import client.locallogic.main.UserGetter;
import client.locallogic.profile.DepartmentGetter;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.students.Student;
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

public class StudentProfile extends PanelTemplate {
    private Student student;
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

    public StudentProfile(MainFrame mainFrame, MainMenu mainMenu, User user) {
        super(mainFrame, mainMenu);
        student = (Student) user;
        configIdentifier = ConfigFileIdentifier.GUI_PROFILE;
        labelsList = new ArrayList<>();
        drawPanel();
    }

    @Override
    protected void initializeComponents() {
        ImageIcon profilePictureIcon = ImageParsingUtils.convertPictureToImageIcon(student.getProfilePicture());
        profilePicture = new JLabel(profilePictureIcon);
        name = new JLabel(ConfigManager.getString(configIdentifier, "nameMessage") +
                student.getFirstName() + " " + student.getLastName());
        labelsList.add(name);
        nationalId = new JLabel(ConfigManager.getString(configIdentifier, "nationalIdMessage")
                + student.getNationalId());
        labelsList.add(nationalId);
        studentId = new JLabel(ConfigManager.getString(configIdentifier, "studentIdMessage") + student.getId());
        labelsList.add(studentId);
        phoneNumber = new JLabel(ConfigManager.getString(configIdentifier, "phoneNumberMessage")
                + student.getPhoneNumber());
        labelsList.add(phoneNumber);
        emailAddress = new JLabel(ConfigManager.getString(configIdentifier, "emailAddressMessage")
                + student.getEmailAddress());
        labelsList.add(emailAddress);
        totalGPA = new JLabel(ConfigManager.getString(configIdentifier, "totalGPAMessage")
                + student.calculateAndGetGPAString());
        labelsList.add(totalGPA);
        department = new JLabel(ConfigManager.getString(configIdentifier, "departmentMessage")
                + DepartmentGetter.getDepartmentName(student.getDepartmentId()));
        labelsList.add(department);
        advisingProfessor = new JLabel(ConfigManager.getString(configIdentifier, "advisingProfessorMessage")
                + UserGetter.getAdvisingProfessorName(student, clientController));
        labelsList.add(advisingProfessor);
        yearOfEntry = new JLabel(ConfigManager.getString(configIdentifier, "yearOfEntryMessage") +
                student.getYearOfEntry());
        labelsList.add(yearOfEntry);
        degreeLevel = new JLabel(ConfigManager.getString(configIdentifier, "degreeLevelMessage")
                + student.getDegreeLevel());
        labelsList.add(degreeLevel);
        studentStatus = new JLabel(ConfigManager.getString(configIdentifier, "studentStatusMessage")
                + student.getStudentStatus());
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
                Response response = clientController.changePhoneNumber(student.getId(), newPhoneNumberText);
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
                Response response = clientController.changeEmailAddress(student.getId(), newEmailAddressText);
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    emailAddress.setText(ConfigManager.getString(configIdentifier, "emailAddressMessage")
                            + newEmailAddressText);
                }
            }
        });
    }
}
