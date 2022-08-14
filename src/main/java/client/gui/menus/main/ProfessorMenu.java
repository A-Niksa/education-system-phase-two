package client.gui.menus.main;

import client.gui.MainFrame;
import client.gui.menus.addition.ProfessorAdder;
import client.gui.menus.addition.StudentAdder;
import client.gui.menus.enrolment.management.CoursesListManager;
import client.gui.menus.enrolment.management.ProfessorsListManager;
import client.gui.menus.enrolment.viewing.CoursesListView;
import client.gui.menus.enrolment.viewing.ProfessorsListView;
import client.gui.menus.messaging.messengerviews.ProfessorMessengerView;
import client.gui.menus.notifications.NotificationsView;
import client.gui.menus.profile.ProfessorProfile;
import client.gui.menus.services.ProfessorExamsList;
import client.gui.menus.services.ProfessorWeeklySchedule;
import client.gui.menus.services.requests.management.DroppingOutManager;
import client.gui.menus.services.requests.management.MinorManager;
import client.gui.menus.services.requests.management.RecommendationManager;
import client.gui.menus.standing.deputies.CurrentStandingMaster;
import client.gui.menus.standing.deputies.TemporaryStandingMaster;
import client.gui.menus.standing.professors.TemporaryStandingManager;
import client.gui.utils.ImageParsingUtils;
import client.locallogic.menus.main.DateStringFormatter;
import shareables.models.pojos.users.professors.AcademicRole;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfessorMenu extends MainMenu {
    private AcademicRole role;
    private JMenuBar menuBar;
    private JMenu registrationAffairs;
    private JMenu academicServices;
    private JMenu academicStanding;
    private JMenu userProfile;
    private JMenu studentRequestsSubMenu;
    private JMenuItem listOfCourses;
    private JMenuItem listOfProfessors;
    private JMenuItem weeklySchedule;
    private JMenuItem listOfExams;
    private JMenuItem recommendationLetter;
    private JMenuItem minor;
    private JMenuItem droppingOut;
    private JMenuItem temporaryScores;
    private JMenuItem temporaryScoresForDeputy;
    private JMenuItem viewStudentsAcademicStanding;
    private JMenuItem editUserProfile;
    private JButton addStudent;
    private JButton addProfessor;

    public ProfessorMenu(MainFrame mainFrame, String username, OfflineModeDTO offlineModeDTO, boolean isOnline) {
        super(mainFrame, username, MainMenuType.PROFESSOR, offlineModeDTO, isOnline);
        configIdentifier = ConfigFileIdentifier.GUI_PROFESSOR_MAIN;
        initializeAcademicRole();
        initializeComponents();
        alignComponents();
        connectListeners();
        startPingingIfOnline(username, this);
    }

    public ProfessorMenu(MainFrame mainFrame, OfflineModeDTO offlineModeDTO, boolean isOnline) {
        super(mainFrame, MainMenuType.PROFESSOR, offlineModeDTO, isOnline);
        configIdentifier = ConfigFileIdentifier.GUI_PROFESSOR_MAIN;
        role = offlineModeDTO.getAcademicRole();
        initializeComponents();
        alignComponents();
        connectListeners();
        startPingingIfOnline(offlineModeDTO.getId(), this);
    }

    private void initializeAcademicRole() {
        if (offlineModeDTO != null) role = offlineModeDTO.getAcademicRole();
//        else role = professor.getAcademicRole();
        else role = AcademicRole.NORMAL;
    }

    @Override
    protected void updatePanel() {
        lastLoginTime.setText(lastLoginTimePrompt + DateStringFormatter.format(offlineModeDTO.getLastLogin()));
        nameLabel.setText(offlineModeDTO.getName());
        emailAddressLabel.setText(offlineModeDTO.getEmailAddress());
        ImageIcon profilePictureIcon = ImageParsingUtils.convertPictureToScaledImageIcon(offlineModeDTO.getProfilePicture(),
                ConfigManager.getInt(ConfigFileIdentifier.GUI_MAIN, "profilePictureW"),
                ConfigManager.getInt(ConfigFileIdentifier.GUI_MAIN, "profilePictureH"));
        profilePicture.setIcon(profilePictureIcon);
        role = offlineModeDTO.getAcademicRole();
    }

    private void initializeComponents() {
        menuBar = new JMenuBar();
        registrationAffairs = new JMenu(ConfigManager.getString(configIdentifier, "registrationAffairsMessage"));
        academicServices = new JMenu(ConfigManager.getString(configIdentifier, "academicServicesMessage"));
        academicStanding = new JMenu(ConfigManager.getString(configIdentifier, "academicStandingMessage"));
        userProfile = new JMenu(ConfigManager.getString(configIdentifier, "userProfileMessage"));
        listOfCourses = new JMenuItem(ConfigManager.getString(configIdentifier, "listOfCoursesMessage"));
        listOfProfessors = new JMenuItem(ConfigManager.getString(configIdentifier, "listOfProfessorsMessage"));
        weeklySchedule = new JMenuItem(ConfigManager.getString(configIdentifier, "weeklyScheduleMessage"));
        listOfExams = new JMenuItem(ConfigManager.getString(configIdentifier, "listOfExamsMessage"));
        studentRequestsSubMenu = new JMenu(ConfigManager.getString(configIdentifier, "studentRequestsSubMenuMessage"));
        recommendationLetter = new JMenuItem(ConfigManager.getString(configIdentifier, "recommendationLetterMessage"));
        minor = new JMenuItem(ConfigManager.getString(configIdentifier, "minorMessage"));
        droppingOut = new JMenuItem(ConfigManager.getString(configIdentifier, "droppingOutMessage"));
        temporaryScores = new JMenuItem(ConfigManager.getString(configIdentifier, "temporaryScoresMessage"));
        temporaryScoresForDeputy = new JMenuItem(ConfigManager.getString(configIdentifier,
                "temporaryScoresForDeputyMessage"));
        viewStudentsAcademicStanding = new JMenuItem(ConfigManager.getString(configIdentifier,
                "viewStudentsAcademicStandingMessage"));
        editUserProfile = new JMenuItem(ConfigManager.getString(configIdentifier, "editUserProfileMessage"));
        addStudent = new JButton(ConfigManager.getString(configIdentifier, "addStudentMessage"));
        addProfessor = new JButton(ConfigManager.getString(configIdentifier, "addProfessorMessage"));
    }

    private void alignComponents() {
        menuBar.setBounds(ConfigManager.getInt(configIdentifier, "menuBarX"),
                ConfigManager.getInt(configIdentifier, "menuBarY"),
                ConfigManager.getInt(configIdentifier, "menuBarW"),
                ConfigManager.getInt(configIdentifier, "menuBarH"));
        add(menuBar);
        menuBar.add(registrationAffairs);
        registrationAffairs.add(listOfCourses);
        registrationAffairs.add(listOfProfessors);
        menuBar.add(academicServices);
        academicServices.add(weeklySchedule);
        academicServices.add(listOfExams);
        academicServices.add(studentRequestsSubMenu);
        alignRequestsSubMenu();
        menuBar.add(academicStanding);
        alignStandingMenu();
        menuBar.add(userProfile);
        userProfile.add(editUserProfile);
        addUserAdditionButtons();
    }

    private void addUserAdditionButtons() {
        if (role == AcademicRole.DEPUTY) {
            addStudent.setBounds(ConfigManager.getInt(configIdentifier, "addStudentX"),
                    ConfigManager.getInt(configIdentifier, "addStudentY"),
                    ConfigManager.getInt(configIdentifier, "addStudentW"),
                    ConfigManager.getInt(configIdentifier, "addStudentH"));
            add(addStudent);
            addProfessor.setBounds(ConfigManager.getInt(configIdentifier, "addProfessorX"),
                    ConfigManager.getInt(configIdentifier, "addProfessorY"),
                    ConfigManager.getInt(configIdentifier, "addProfessorW"),
                    ConfigManager.getInt(configIdentifier, "addProfessorH"));
            add(addProfessor);
        }
    }

    private void alignStandingMenu() {
        academicStanding.add(temporaryScores);

        if (role == AcademicRole.DEPUTY) {
            academicStanding.add(temporaryScoresForDeputy);
            academicStanding.add(viewStudentsAcademicStanding);
        }
    }

    private void alignRequestsSubMenu() {
        studentRequestsSubMenu.add(recommendationLetter);

        if (role == AcademicRole.DEPUTY || offlineModeDTO.isTemporaryDeputy()) {
            studentRequestsSubMenu.add(droppingOut);
            studentRequestsSubMenu.add(minor);
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
                mainFrame.setCurrentPanel(new ProfessorProfile(mainFrame, mainMenu, offlineModeDTO, isOnline));
            }
        });

        listOfCourses.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the courses list in educational services",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                if (role == AcademicRole.DEPUTY) {
                    mainFrame.setCurrentPanel(new CoursesListManager(mainFrame, mainMenu, offlineModeDTO));
                } else {
                    mainFrame.setCurrentPanel(new CoursesListView(mainFrame, mainMenu, offlineModeDTO));
                }
            }
        });

        listOfProfessors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the professors list in educational services",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                if (role == AcademicRole.DEAN) {
                    mainFrame.setCurrentPanel(new ProfessorsListManager(mainFrame, mainMenu, offlineModeDTO));
                } else {
                    mainFrame.setCurrentPanel(new ProfessorsListView(mainFrame, mainMenu, offlineModeDTO));
                }
            }
        });

        weeklySchedule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened weekly schedule in academic services",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new ProfessorWeeklySchedule(mainFrame, mainMenu, offlineModeDTO, isOnline));
            }
        });

        listOfExams.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened list of exams in academic services",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new ProfessorExamsList(mainFrame, mainMenu, offlineModeDTO, isOnline));
            }
        });

        recommendationLetter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the recommendation letters subsection in academic requests",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new RecommendationManager(mainFrame, mainMenu, offlineModeDTO));
            }
        });

        droppingOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the dropping out subsection in academic requests",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new DroppingOutManager(mainFrame, mainMenu, offlineModeDTO));
            }
        });

        minor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the minor requests subsection in academic requests",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new MinorManager(mainFrame, mainMenu, offlineModeDTO));
            }
        });

        temporaryScoresForDeputy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened temporary scores for deputies in academic standing",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new TemporaryStandingMaster(mainFrame, mainMenu, offlineModeDTO));
            }
        });

        temporaryScores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened temporary scores in academic standing",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new TemporaryStandingManager(mainFrame, mainMenu, offlineModeDTO));
            }
        });

        viewStudentsAcademicStanding.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened current student standings in academic standing",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new CurrentStandingMaster(mainFrame, mainMenu, offlineModeDTO));
            }
        });

        addStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the student addition section",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new StudentAdder(mainFrame, mainMenu, offlineModeDTO));
            }
        });

        addProfessor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the professor addition section",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new ProfessorAdder(mainFrame, mainMenu, offlineModeDTO));
            }
        });

        messengerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the messenger",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new ProfessorMessengerView(mainFrame, mainMenu, offlineModeDTO, isOnline));
            }
        });

        notificationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the notifications section",
                        "connectListeners", getClass());
                facilitateChangingPanel(mainMenu);
                mainFrame.setCurrentPanel(new NotificationsView(mainFrame, mainMenu, offlineModeDTO));
            }
        });
    }

    @Override
    public void disableOnlineComponents() {
        stopPanelLoop();
        listOfCourses.setEnabled(false);
        listOfProfessors.setEnabled(false);
        recommendationLetter.setEnabled(false);
        minor.setEnabled(false);
        droppingOut.setEnabled(false);
        temporaryScores.setEnabled(false);
        temporaryScoresForDeputy.setEnabled(false);
        viewStudentsAcademicStanding.setEnabled(false);
        addStudent.setEnabled(false);
        addProfessor.setEnabled(false);
        notificationsButton.setEnabled(false);
    }

    @Override
    public void enableOnlineComponents() {
        restartPanelLoop();
        listOfCourses.setEnabled(true);
        listOfProfessors.setEnabled(true);
        recommendationLetter.setEnabled(true);
        minor.setEnabled(true);
        droppingOut.setEnabled(true);
        temporaryScores.setEnabled(true);
        temporaryScoresForDeputy.setEnabled(true);
        viewStudentsAcademicStanding.setEnabled(true);
        addStudent.setEnabled(true);
        addProfessor.setEnabled(true);
        notificationsButton.setEnabled(true);
    }
}
