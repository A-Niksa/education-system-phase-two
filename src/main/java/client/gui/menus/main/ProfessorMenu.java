package client.gui.menus.main;

import client.gui.MainFrame;
import client.gui.menus.enrolment.management.CoursesListManager;
import client.gui.menus.enrolment.viewing.CoursesListView;
import client.gui.menus.enrolment.viewing.ProfessorsListView;
import client.gui.menus.profile.ProfessorProfile;
import client.gui.menus.services.ProfessorExamsList;
import client.gui.menus.services.ProfessorWeeklySchedule;
import client.gui.menus.services.requests.management.DroppingOutManager;
import shareables.models.pojos.users.professors.AcademicRole;
import shareables.models.pojos.users.professors.Professor;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfessorMenu extends MainMenu {
    private Professor professor;
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
    private JMenuItem viewStudentsAcademicStanding;
    private JMenuItem editUserProfile;
    private JButton addStudent;
    private JButton addProfessor;

    public ProfessorMenu(MainFrame mainFrame, String username) {
        super(mainFrame, username);
        configIdentifier = ConfigFileIdentifier.GUI_PROFESSOR_MAIN;
        professor = (Professor) user;
        role = professor.getAcademicRole();
        initializeComponents();
        alignComponents();
        connectListeners();
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
            academicStanding.add(viewStudentsAcademicStanding);
        }
    }

    private void alignRequestsSubMenu() {
        studentRequestsSubMenu.add(recommendationLetter);

        if (role == AcademicRole.DEPUTY) {
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
                mainFrame.setCurrentPanel(new ProfessorProfile(mainFrame, mainMenu, user));
            }
        });

        listOfCourses.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the courses list in educational services",
                        "connectListeners", getClass());
                if (role == AcademicRole.DEPUTY) {
                    mainFrame.setCurrentPanel(new CoursesListManager(mainFrame, mainMenu, professor));
                } else {
                    mainFrame.setCurrentPanel(new CoursesListView(mainFrame, mainMenu));
                }
            }
        });

        listOfProfessors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the professors list in educational services",
                        "connectListeners", getClass());
                if (role == AcademicRole.DEAN) {
                    // TODO
//                    mainFrame.setCurrentPanel(new ProfessorsListManager(mainFrame, mainMenu, professorUser));
                } else {
                    mainFrame.setCurrentPanel(new ProfessorsListView(mainFrame, mainMenu));
                }
            }
        });

        weeklySchedule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened weekly schedule in academic services",
                        "connectListeners", getClass());
                mainFrame.setCurrentPanel(new ProfessorWeeklySchedule(mainFrame, mainMenu, professor));
            }
        });

        listOfExams.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened list of exams in academic services",
                        "connectListeners", getClass());
                mainFrame.setCurrentPanel(new ProfessorExamsList(mainFrame, mainMenu, professor));
            }
        });

        recommendationLetter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the recommendation letters subsection in academic requests",
                        "connectListeners", getClass());
                // TODO
//                mainFrame.setCurrentPanel(new RecommendationManager(mainFrame, mainMenu, professorUser));
            }
        });

        droppingOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the dropping out subsection in academic requests",
                        "connectListeners", getClass());
                mainFrame.setCurrentPanel(new DroppingOutManager(mainFrame, mainMenu, professor));
            }
        });

        minor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the minor requests subsection in academic requests",
                        "connectListeners", getClass());
                // TODO
//                mainFrame.setCurrentPanel(new MinorManager(mainFrame, mainMenu, professorUser));
            }
        });

        temporaryScores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (role == AcademicRole.DEPUTY) {
                    MasterLogger.clientInfo(clientController.getId(), "Opened temporary scores in academic standing",
                            "connectListeners", getClass());
                    // TODO
//                    mainFrame.setCurrentPanel(new TemporaryStandingMaster(mainFrame, mainMenu, professorUser));
                } else { // NORMAL or DEAN by design
                    MasterLogger.clientInfo(clientController.getId(), "Opened temporary scores in academic standing",
                            "connectListeners", getClass());
                    // TODO
//                    mainFrame.setCurrentPanel(new TemporaryStandingManager(mainFrame, mainMenu, professorUser));
                }
            }
        });

        viewStudentsAcademicStanding.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened current student standings in academic standing",
                        "connectListeners", getClass());
                // TODO
//                mainFrame.setCurrentPanel(new CurrentStandingMaster(mainFrame, mainMenu, professorUser));
            }
        });

        addStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the student addition section",
                        "connectListeners", getClass());
                // TODO
//                mainFrame.setCurrentPanel(new StudentAdder(mainFrame, mainMenu, professorUser));
            }
        });

        addProfessor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Opened the professor addition section",
                        "connectListeners", getClass());
                // TODO
//                mainFrame.setCurrentPanel(new ProfessorAdder(mainFrame, mainMenu, professorUser));
            }
        });
    }
}
