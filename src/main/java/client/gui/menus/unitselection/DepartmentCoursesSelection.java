package client.gui.menus.unitselection;

import client.controller.ClientController;
import client.gui.MainFrame;
import client.gui.utils.EnumArrayUtils;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.DTOs.unitselection.CourseThumbnailDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DepartmentCoursesSelection extends CoursesSelectionPanel {
    private enum SortingType {
        NONE,
        ALPHABETICAL,
        EXAM_DATE,
        DEGREE_LEVEL
    }

    private JScrollPane departmentCoursesScrollPane;
    private String[] departmentNameStrings;
    private JComboBox<String> departmentNamesBox;
    private String selectedDepartmentNameString;
    private JButton chooseDepartmentButton;
    private SortingType sortingType;
    private JButton sortByCourseNameButton;
    private JButton sortByExamDateButton;
    private JButton sortByDegreeLevelButton;

    public DepartmentCoursesSelection(MainFrame mainFrame, UnitSelectionMenu unitSelectionMenu,
                                      ClientController clientController, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, unitSelectionMenu, clientController, offlineModeDTO);
        sortingType = SortingType.NONE;
        departmentNameStrings = EnumArrayUtils.initializeDepartmentNameStrings();
        drawPanel();
    }

    @Override
    protected void initializeComponents() {
        departmentNamesBox = new JComboBox<>(departmentNameStrings);
        chooseDepartmentButton = new JButton(ConfigManager.getString(configIdentifier, "chooseDepartmentButtonM"));
        sortByCourseNameButton = new JButton(ConfigManager.getString(configIdentifier, "sortByCourseNameButtonM"));
        sortByExamDateButton = new JButton(ConfigManager.getString(configIdentifier, "sortByExamDateButtonM"));
        sortByDegreeLevelButton = new JButton(ConfigManager.getString(configIdentifier, "sortByDegreeLevelButtonM"));

        coursesListModel = new DefaultListModel<>();
        coursesGraphicalList = new JList<>(coursesListModel);
        departmentCoursesScrollPane = new JScrollPane();
        departmentCoursesScrollPane.setViewportView(coursesGraphicalList);
    }

    @Override
    protected void alignComponents() {
        departmentNamesBox.setBounds(ConfigManager.getInt(configIdentifier, "departmentNamesBoxX"),
                ConfigManager.getInt(configIdentifier, "departmentNamesBoxY"),
                ConfigManager.getInt(configIdentifier, "departmentNamesBoxW"),
                ConfigManager.getInt(configIdentifier, "departmentNamesBoxH"));
        add(departmentNamesBox);
        chooseDepartmentButton.setBounds(ConfigManager.getInt(configIdentifier, "chooseDepartmentButtonX"),
                ConfigManager.getInt(configIdentifier, "chooseDepartmentButtonY"),
                ConfigManager.getInt(configIdentifier, "chooseDepartmentButtonW"),
                ConfigManager.getInt(configIdentifier, "chooseDepartmentButtonH"));
        add(chooseDepartmentButton);
        sortByCourseNameButton.setBounds(ConfigManager.getInt(configIdentifier, "sortByCourseNameButtonX"),
                ConfigManager.getInt(configIdentifier, "sortByCourseNameButtonY"),
                ConfigManager.getInt(configIdentifier, "sortByCourseNameButtonW"),
                ConfigManager.getInt(configIdentifier, "sortByCourseNameButtonH"));
        add(sortByCourseNameButton);
        sortByExamDateButton.setBounds(ConfigManager.getInt(configIdentifier, "sortByExamDateButtonX"),
                ConfigManager.getInt(configIdentifier, "sortByExamDateButtonY"),
                ConfigManager.getInt(configIdentifier, "sortByExamDateButtonW"),
                ConfigManager.getInt(configIdentifier, "sortByExamDateButtonH"));
        add(sortByExamDateButton);
        sortByDegreeLevelButton.setBounds(ConfigManager.getInt(configIdentifier, "sortByDegreeLevelButtonX"),
                ConfigManager.getInt(configIdentifier, "sortByDegreeLevelButtonY"),
                ConfigManager.getInt(configIdentifier, "sortByDegreeLevelButtonW"),
                ConfigManager.getInt(configIdentifier, "sortByDegreeLevelButtonH"));
        add(sortByDegreeLevelButton);

        departmentCoursesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        departmentCoursesScrollPane.setBounds(ConfigManager.getInt(configIdentifier, "departmentCoursesScrollPaneX"),
                ConfigManager.getInt(configIdentifier, "departmentCoursesScrollPaneY"),
                ConfigManager.getInt(configIdentifier, "departmentCoursesScrollPaneW"),
                ConfigManager.getInt(configIdentifier, "departmentCoursesScrollPaneH"));
        add(departmentCoursesScrollPane);
    }

    @Override
    protected void connectListeners() {
        chooseDepartmentButton.addActionListener(actionEvent -> {
            selectedDepartmentNameString = (String) departmentNamesBox.getSelectedItem();
            purgeAndUpdateGraphicalList();
            MasterLogger.clientInfo(clientController.getId(), "Department courses set to " +
                            selectedDepartmentNameString, "connectListeners", getClass());
        });

        sortByCourseNameButton.addActionListener(actionEvent -> {
            sortingType = SortingType.ALPHABETICAL;
            purgeAndUpdateGraphicalList();
            MasterLogger.clientInfo(clientController.getId(), "Set sorting type to alphabetical",
                    "connectListeners", getClass());
        });

        sortByExamDateButton.addActionListener(actionEvent -> {
            sortingType = SortingType.EXAM_DATE;
            purgeAndUpdateGraphicalList();
            MasterLogger.clientInfo(clientController.getId(), "Set sorting type to be done by exam date",
                    "connectListeners", getClass());
        });

        sortByDegreeLevelButton.addActionListener(actionEvent -> {
            sortingType = SortingType.DEGREE_LEVEL;
            purgeAndUpdateGraphicalList();
            MasterLogger.clientInfo(clientController.getId(), "Set sorting type to be done by degree level",
                    "connectListeners", getClass());
        });
    }

    @Override
    public void updatePanel() {
        if (selectedDepartmentNameString == null) return;

        if (courseThumbnailTexts != null) {
            updatePanelNormally();
        } else { // setting for the first time
            updatePanelForFirstTime();
        }
    }

    private void updatePanelNormally() {
        updateCourseThumbnailDTOs();
        String[] previousCourseThumbnailTexts = Arrays.copyOf(courseThumbnailTexts,
                courseThumbnailTexts.length);
        updateCourseThumbnailTexts();
        Arrays.stream(previousCourseThumbnailTexts)
                .filter(e -> !arrayContains(courseThumbnailTexts, e))
                .forEach(e -> coursesListModel.removeElement(e));
        Arrays.stream(courseThumbnailTexts)
                .filter(e -> !arrayContains(previousCourseThumbnailTexts, e))
                .forEach(e -> coursesListModel.add(0, e));
    }

    private void updatePanelForFirstTime() {
        updateCourseThumbnailDTOs();
        updateCourseThumbnailTexts();
        Arrays.stream(courseThumbnailTexts)
                .forEach(e -> coursesListModel.addElement(e));
    }

    @Override
    protected void updateCourseThumbnailDTOs() {
        Response response = null;
        if (sortingType == SortingType.NONE) {
            response = clientController.getDepartmentCourseThumbnailDTOs(selectedDepartmentNameString, offlineModeDTO.getId());
        } else if (sortingType == SortingType.ALPHABETICAL) {
            response = clientController.getDepartmentCourseThumbnailDTOsAlphabetically(selectedDepartmentNameString,
                    offlineModeDTO.getId());
        } else if (sortingType == SortingType.EXAM_DATE) {
            response = clientController.getDepartmentCourseThumbnailDTOsInExamDateOrder(selectedDepartmentNameString,
                    offlineModeDTO.getId());
        } else if (sortingType == SortingType.DEGREE_LEVEL) {
            response = clientController.getDepartmentCourseThumbnailDTOsInDegreeLevelOrder(selectedDepartmentNameString,
                    offlineModeDTO.getId());
        }

        if (response == null) return;

        courseThumbnailDTOs = (ArrayList<CourseThumbnailDTO>) response.get("courseThumbnailDTOs");
    }
}