package client.gui.menus.main;

import client.gui.MainFrame;
import client.gui.menus.enrolment.viewing.CoursesListView;
import client.gui.menus.enrolment.viewing.ProfessorsListView;
import client.gui.menus.profile.StudentProfile;
import client.gui.menus.services.StudentExamsList;
import client.gui.menus.services.StudentWeeklySchedule;
import client.gui.menus.services.requests.submission.*;
import client.gui.menus.standing.students.CurrentStandingView;
import client.gui.menus.standing.students.TemporaryStandingView;
import client.gui.utils.ImageParsingUtils;
import client.locallogic.main.DateStringFormatter;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.OfflineModeDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentMenu extends MainMenu {
    private Student student;
    private JLabel studentStatusLabel;
    private JLabel advisingProfessorName;
    private JLabel isAllowedToEnrol;
    private JLabel enrolmentTime;
    private JSeparator separator;
    private JMenuBar menuBar;
    private JMenu registrationAffairs;
    private JMenu academicServices;
    private JMenu academicStanding;
    private JMenu userProfile;
    private JMenu requestsSubMenu;
    private JMenuItem listOfCourses;
    private JMenuItem listOfProfessors;
    private JMenuItem weeklySchedule;
    private JMenuItem listOfExams;
    private JMenuItem recommendationLetter;
    private JMenuItem enrolmentCertificate;
    private JMenuItem minor;
    private JMenuItem droppingOut;
    private JMenuItem dormitory;
    private JMenuItem defenseSlot;
    private JMenuItem temporaryScores;
    private JMenuItem currentAcademicStanding;
    private JMenuItem editUserProfile;
    private String studentStatusLabelPrompt;
    private String advisingProfessorNamePrompt;
    private String enrolmentTimePrompt;

    public StudentMenu(MainFrame mainFrame, String username, OfflineModeDTO offlineModeDTO, boolean isOnline) {
        super(mainFrame, username, MainMenuType.STUDENT, offlineModeDTO, isOnline);
        configIdentifier = ConfigFileIdentifier.GUI_STUDENT_MAIN;
        initializeComponents();
        alignComponents();
        connectListeners();
        startPingingIfOnline(username, this);
    }

    public StudentMenu(MainFrame mainFrame, User user, OfflineModeDTO offlineModeDTO, boolean isOnline) {
        super(mainFrame, user, MainMenuType.STUDENT, offlineModeDTO, isOnline);
        configIdentifier = ConfigFileIdentifier.GUI_STUDENT_MAIN;
        student = (Student) user;
        initializeComponents();
        alignComponents();
        connectListeners();
        startPingingIfOnline(user.getId(), this);
    }

    @Override
    protected void updatePanel() {
        student = (Student) user;
        lastLoginTime.setText(lastLoginTimePrompt + DateStringFormatter.format(offlineModeDTO.getLastLogin()));
        nameLabel.setText(offlineModeDTO.getName());
        emailAddressLabel.setText(offlineModeDTO.getEmailAddress());
        ImageIcon profilePictureIcon = ImageParsingUtils.convertPictureToImageIcon(offlineModeDTO.getProfilePicture());
        profilePicture.setIcon(profilePictureIcon);
        studentStatusLabel.setText(studentStatusLabelPrompt + offlineModeDTO.getStudentStatus().toString());
        advisingProfessorName.setText(advisingProfessorNamePrompt + offlineModeDTO.getAdvisingProfessorName());
        isAllowedToEnrol.setText(offlineModeDTO.getPermissionToEnrolPrompt());
        enrolmentTime.setText(enrolmentTimePrompt + DateStringFormatter.formatEnrolmentTime(
                offlineModeDTO.getEnrolmentTime()
        ));
    }

    private void initializeComponents() {
        studentStatusLabelPrompt = ConfigManager.getString(configIdentifier, "academicStatusLabelMessage");
        studentStatusLabel = new JLabel(studentStatusLabelPrompt + offlineModeDTO.getStudentStatus());
        advisingProfessorNamePrompt = ConfigManager.getString(configIdentifier, "advisingProfessorNameMessage");
        advisingProfessorName = new JLabel(advisingProfessorNamePrompt + offlineModeDTO.getAdvisingProfessorName());
        isAllowedToEnrol = new JLabel(offlineModeDTO.getPermissionToEnrolPrompt());
        enrolmentTimePrompt = ConfigManager.getString(configIdentifier, "enrolmentTimeMessage");
        enrolmentTime = new JLabel(enrolmentTimePrompt + DateStringFormatter.formatEnrolmentTime(
                offlineModeDTO.getEnrolmentTime()
        ));
        separator = new JSeparator();
        menuBar = new JMenuBar();
        registrationAffairs = new JMenu(ConfigManager.getString(configIdentifier, "registrationAffairsMessage"));
        academicServices = new JMenu(ConfigManager.getString(configIdentifier, "academicServicesMessage"));
        academicStanding = new JMenu(ConfigManager.getString(configIdentifier, "academicStandingMessage"));
        userProfile = new JMenu(ConfigManager.getString(configIdentifier, "userProfileMessage"));
        listOfCourses = new JMenuItem(ConfigManager.getString(configIdentifier, "listOfCoursesMessage"));
        listOfProfessors = new JMenuItem(ConfigManager.getString(configIdentifier, "listOfProfessorsMessage"));
        weeklySchedule = new JMenuItem(ConfigManager.getString(configIdentifier, "weeklyScheduleMessage"));
        listOfExams = new JMenuItem(ConfigManager.getString(configIdentifier,"listOfExamsMessage" ));
        requestsSubMenu = new JMenu(ConfigManager.getString(configIdentifier, "requestsSubMenuMessage"));
        recommendationLetter = new JMenuItem(ConfigManager.getString(configIdentifier, "recommendationLetterMessage"));
        enrolmentCertificate = new JMenuItem(ConfigManager.getString(configIdentifier, "enrolmentCertificateMessage"));
        minor = new JMenuItem(ConfigManager.getString(configIdentifier, "minorMessage"));
        droppingOut = new JMenuItem(ConfigManager.getString(configIdentifier, "droppingOutMessage"));
        dormitory = new JMenuItem(ConfigManager.getString(configIdentifier, "dormitoryMessage"));
        defenseSlot = new JMenuItem(ConfigManager.getString(configIdentifier, "defenseSlotMessage"));
        temporaryScores = new JMenuItem(ConfigManager.getString(configIdentifier, "temporaryScoresMessage"));
        // TODO: temporary scores bug (with sina)?
        currentAcademicStanding = new JMenuItem(ConfigManager.getString(configIdentifier, "currentAcademicStandingMessage"));
        editUserProfile = new JMenuItem(ConfigManager.getString(configIdentifier, "editUserProfileMessage"));
    }

    private void alignComponents() {
        studentStatusLabel.setBounds(ConfigManager.getInt(configIdentifier, "academicStatusLabelX"),
                ConfigManager.getInt(configIdentifier, "academicStatusLabelY"),
                ConfigManager.getInt(configIdentifier, "academicStatusLabelW"),
                ConfigManager.getInt(configIdentifier, "academicStatusLabelH"));
        add(studentStatusLabel);
        advisingProfessorName.setBounds(ConfigManager.getInt(configIdentifier, "advisingProfessorNameX"),
                ConfigManager.getInt(configIdentifier, "advisingProfessorNameY"),
                ConfigManager.getInt(configIdentifier, "advisingProfessorNameW"),
                ConfigManager.getInt(configIdentifier, "advisingProfessorNameH"));
        add(advisingProfessorName);
        isAllowedToEnrol.setBounds(ConfigManager.getInt(configIdentifier, "isAllowedToEnrolX"),
                ConfigManager.getInt(configIdentifier, "isAllowedToEnrolY"),
                ConfigManager.getInt(configIdentifier, "isAllowedToEnrolW"),
                ConfigManager.getInt(configIdentifier, "isAllowedToEnrolH"));
        add(isAllowedToEnrol);
        enrolmentTime.setBounds(ConfigManager.getInt(configIdentifier, "enrolmentTimeX"),
                ConfigManager.getInt(configIdentifier, "enrolmentTimeY"),
                ConfigManager.getInt(configIdentifier, "enrolmentTimeW"),
                ConfigManager.getInt(configIdentifier, "enrolmentTimeH"));
        add(enrolmentTime);
        separator.setOrientation(SwingConstants.VERTICAL);
        separator.setBackground(Color.BLACK);
        separator.setForeground(Color.BLACK);
        separator.setBounds(ConfigManager.getInt(configIdentifier, "separatorX"),
                ConfigManager.getInt(configIdentifier, "separatorY"),
                ConfigManager.getInt(configIdentifier, "separatorW"),
                ConfigManager.getInt(configIdentifier, "separatorH"));
        add(separator);
        menuBar.setBounds(ConfigManager.getInt(configIdentifier, "menuBarX"),
                ConfigManager.getInt(configIdentifier, "menuBarY"),
                ConfigManager.getInt(configIdentifier, "menuBarW"),
                ConfigManager.getInt(configIdentifier, "menuBarH"));
        add(menuBar);
        menuBar.add(registrationAffairs);
        registrationAffairs.add(listOfCourses);
        registrationAffairs.add(listOfProfessors);
        menuBar.add(academicServices);
        alignServicesMenu();
        menuBar.add(academicStanding);
        academicStanding.add(temporaryScores);
        academicStanding.add(currentAcademicStanding);
        menuBar.add(userProfile);
        userProfile.add(editUserProfile);
    }

    private void alignServicesMenu() {
        academicServices.add(weeklySchedule);
        academicServices.add(listOfExams);
        academicServices.add(requestsSubMenu);
        requestsSubMenu.add(droppingOut);
        requestsSubMenu.add(enrolmentCertificate);

        switch (offlineModeDTO.getDegreeLevel()) {
            case UNDERGRADUATE:
                requestsSubMenu.add(recommendationLetter);
                requestsSubMenu.add(minor);
                return;
            case GRADUATE:
                requestsSubMenu.add(recommendationLetter);
                requestsSubMenu.add(dormitory);
                return;
            case PHD:
                requestsSubMenu.add(defenseSlot);
                return;
        }
    }

    private void connectListeners() {
        MainMenu mainMenu = this;

        editUserProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the profile editor in the user profile",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new StudentProfile(mainFrame, mainMenu, user, offlineModeDTO, isOnline));
            }
        });

        listOfCourses.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the courses list in educational services",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new CoursesListView(mainFrame, mainMenu));
            }
        });

        listOfProfessors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the professors list in educational services",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new ProfessorsListView(mainFrame, mainMenu));
            }
        });

        temporaryScores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened temporary scores in academic standing",
                        "connectListeners",  getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new TemporaryStandingView(mainFrame, mainMenu, user));
            }
        });

        currentAcademicStanding.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened current standing in academic standing",
                        "connectListeners",  getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new CurrentStandingView(mainFrame, mainMenu, user, offlineModeDTO, isOnline));
            }
        });

        weeklySchedule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened weekly schedule in academic services",
                        "connectListeners",  getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new StudentWeeklySchedule(mainFrame, mainMenu, user, offlineModeDTO, isOnline));
            }
        });

        listOfExams.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened list of exams in academic services",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new StudentExamsList(mainFrame, mainMenu, user, offlineModeDTO, isOnline));
            }
        });

        droppingOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(),
                        "Opened the dropping out subsection in academic requests", "connectListeners",
                        getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new DroppingOutSubmission(mainFrame, mainMenu, user));
            }
        });

        enrolmentCertificate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(),
                        "Opened the enrolment certificate subsection in academic requests", "connectListeners",
                        getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new CertificateSubmission(mainFrame, mainMenu, user));
            }
        });

        recommendationLetter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(),
                        "Opened the recommendation letters subsection in academic requests", "connectListeners",
                        getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new RecommendationSubmission(mainFrame, mainMenu, user));
            }
        });

        minor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the minor requests subsection in academic requests",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new MinorSubmission(mainFrame, mainMenu, user));
            }
        });

        dormitory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the dorm requests subsection in academic requests",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new DormSubmission(mainFrame, mainMenu, user));
            }
        });

        defenseSlot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the defense slot selection in academic requests",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new DefenseSubmission(mainFrame, mainMenu, user));
            }
        });
    }

    @Override
    public void disableOnlineComponents() {
        stopPanelLoop();
        listOfCourses.setEnabled(false);
        listOfProfessors.setEnabled(false);
        recommendationLetter.setEnabled(false);
        enrolmentCertificate.setEnabled(false);
        minor.setEnabled(false);
        droppingOut.setEnabled(false);
        dormitory.setEnabled(false);
        defenseSlot.setEnabled(false);
        temporaryScores.setEnabled(false);
    }

    @Override
    public void enableOnlineComponents() {
        restartPanelLoop();
        listOfCourses.setEnabled(true);
        listOfProfessors.setEnabled(true);
        recommendationLetter.setEnabled(true);
        enrolmentCertificate.setEnabled(true);
        minor.setEnabled(true);
        droppingOut.setEnabled(true);
        dormitory.setEnabled(true);
        defenseSlot.setEnabled(true);
        temporaryScores.setEnabled(true);
    }
}