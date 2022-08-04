package client.gui.menus.main;

import client.gui.MainFrame;
import client.gui.menus.enrolment.viewing.CoursesListView;
import client.gui.menus.enrolment.viewing.ProfessorsListView;
import client.gui.menus.profile.StudentProfile;
import client.gui.menus.services.StudentExamsList;
import client.gui.menus.services.StudentWeeklySchedule;
import client.gui.menus.services.requests.submission.*;
import client.locallogic.main.UserGetter;
import shareables.models.pojos.users.students.Student;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentMenu extends MainMenu {
    private Student student;
    private JLabel academicStatusLabel;
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

    public StudentMenu(MainFrame mainFrame, String username) {
        super(mainFrame, username);
        configIdentifier = ConfigFileIdentifier.GUI_STUDENT_MAIN;
        student = (Student) user;
        initializeComponents();
        alignComponents();
        connectListeners();
    }

    private void initializeComponents() {
        academicStatusLabel = new JLabel(ConfigManager.getString(configIdentifier, "academicStatusLabelMessage")
                + student.getStudentStatus());
        advisingProfessorName = new JLabel(ConfigManager.getString(configIdentifier, "advisingProfessorNameMessage")
                + UserGetter.getAdvisingProfessorName(student, clientController));
        isAllowedToEnrol = new JLabel(ConfigManager.getString(configIdentifier, "isAllowedToEnrolMessage"));
        enrolmentTime = new JLabel(ConfigManager.getString(configIdentifier, "enrolmentTimeMessage"));
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
        academicStatusLabel.setBounds(ConfigManager.getInt(configIdentifier, "academicStatusLabelX"),
                ConfigManager.getInt(configIdentifier, "academicStatusLabelY"),
                ConfigManager.getInt(configIdentifier, "academicStatusLabelW"),
                ConfigManager.getInt(configIdentifier, "academicStatusLabelH"));
        add(academicStatusLabel);
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

        Student studentUser = (Student) user;
        switch (studentUser.getDegreeLevel()) {
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
                mainFrame.setCurrentPanel(new StudentProfile(mainFrame, mainMenu, user));
            }
        });

        listOfCourses.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the courses list in educational services",
                        "connectListeners", getClass());
                mainFrame.setCurrentPanel(new CoursesListView(mainFrame, mainMenu));
            }
        });

        listOfProfessors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the professors list in educational services",
                        "connectListeners", getClass());
                mainFrame.setCurrentPanel(new ProfessorsListView(mainFrame, mainMenu));
            }
        });

        temporaryScores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened temporary scores in academic standing",
                        "connectListeners",  getClass());
                // TODO
//                mainFrame.setCurrentPanel(new TemporaryStandingView(mainFrame, mainMenu, user));
            }
        });

        currentAcademicStanding.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened current standing in academic standing",
                        "connectListeners",  getClass());
                // TODO
//                mainFrame.setCurrentPanel(new CurrentStandingView(mainFrame, mainMenu, user));
            }
        });

        weeklySchedule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened weekly schedule in academic services",
                        "connectListeners",  getClass());
                mainFrame.setCurrentPanel(new StudentWeeklySchedule(mainFrame, mainMenu, user));
            }
        });

        listOfExams.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened list of exams in academic services",
                        "connectListeners", getClass());
                mainFrame.setCurrentPanel(new StudentExamsList(mainFrame, mainMenu, user));
            }
        });

        droppingOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(),
                        "Opened the dropping out subsection in academic requests", "connectListeners",
                        getClass());
                mainFrame.setCurrentPanel(new DroppingOutSubmission(mainFrame, mainMenu, user));
            }
        });

        enrolmentCertificate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(),
                        "Opened the enrolment certificate subsection in academic requests", "connectListeners",
                        getClass());
                mainFrame.setCurrentPanel(new CertificateSubmission(mainFrame, mainMenu, user));
            }
        });

        recommendationLetter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(),
                        "Opened the recommendation letters subsection in academic requests", "connectListeners",
                        getClass());
                mainFrame.setCurrentPanel(new RecommendationSubmission(mainFrame, mainMenu, user));
            }
        });

        minor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the minor requests subsection in academic requests",
                        "connectListeners", getClass());
                mainFrame.setCurrentPanel(new MinorSubmission(mainFrame, mainMenu, user));
            }
        });

        dormitory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the dorm requests subsection in academic requests",
                        "connectListeners", getClass());
                mainFrame.setCurrentPanel(new DormSubmission(mainFrame, mainMenu, user));
            }
        });

        defenseSlot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the defense slot selection in academic requests",
                        "connectListeners", getClass());
                mainFrame.setCurrentPanel(new DefenseSubmission(mainFrame, mainMenu, user));
            }
        });
    }
}