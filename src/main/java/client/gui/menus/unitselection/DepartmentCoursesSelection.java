package client.gui.menus.unitselection;

import client.controller.ClientController;
import client.gui.MainFrame;
import client.gui.utils.EnumArrayUtils;
import client.locallogic.menus.messaging.ThumbnailIdParser;
import client.locallogic.menus.unitselection.CourseSelectionUtils;
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

    private DefaultListModel<String> departmentCoursesListModel;
    private JList<String> departmentCoursesGraphicalList;
    private ArrayList<CourseThumbnailDTO> departmentCourseThumbnailDTOs;
    private String[] departmentCourseThumbnailTexts;
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
        departmentCourseThumbnailDTOs = new ArrayList<>();
        drawPanel();
    }

    @Override
    protected void initializeComponents() {
        departmentNamesBox = new JComboBox<>(departmentNameStrings);
        chooseDepartmentButton = new JButton(ConfigManager.getString(configIdentifier, "chooseDepartmentButtonM"));
        sortByCourseNameButton = new JButton(ConfigManager.getString(configIdentifier, "sortByCourseNameButtonM"));
        sortByExamDateButton = new JButton(ConfigManager.getString(configIdentifier, "sortByExamDateButtonM"));
        sortByDegreeLevelButton = new JButton(ConfigManager.getString(configIdentifier, "sortByDegreeLevelButtonM"));

        departmentCoursesListModel = new DefaultListModel<>();
        departmentCoursesGraphicalList = new JList<>(departmentCoursesListModel);
        departmentCoursesScrollPane = new JScrollPane();
        departmentCoursesScrollPane.setViewportView(departmentCoursesGraphicalList);
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

    private void purgeAndUpdateGraphicalList() {
        if (departmentCourseThumbnailTexts == null) return;

        updateCourseThumbnailDTOs();
        updateCourseThumbnailTexts();
        departmentCoursesListModel.removeAllElements();
        Arrays.stream(departmentCourseThumbnailTexts)
                .forEach(e -> departmentCoursesListModel.addElement(e));
    }

    @Override
    protected void connectListSelectionListeners() {
//        String previousSelectedCourseId = selectedCourseId;
//        updateSelectedCourseId();
//        if (!selectedCourseId.equals(previousSelectedCourseId)) {
//            CourseThumbnailDTO selectedCourseThumbnailDTO = CourseSelectionUtils.getCourseThumbnailDTOWithId(selectedCourseId,
//                    departmentCourseThumbnailDTOs);
//
//            removePreviousCourseSelectionButtons();
//            setAppropriateCourseSelectionButtons(selectedCourseThumbnailDTO);
//        }
        departmentCoursesGraphicalList.addListSelectionListener(actionEvent -> {
            if (!actionEvent.getValueIsAdjusting()) {
                updateSelectedCourseId();

                CourseThumbnailDTO selectedCourseThumbnailDTO = CourseSelectionUtils.getCourseThumbnailDTOWithId(selectedCourseId,
                        departmentCourseThumbnailDTOs);
                removePreviousCourseSelectionButtons();
                setAppropriateCourseSelectionButtons(selectedCourseThumbnailDTO);
                repaint();
                revalidate();
            }
        });
    }

    private void updateSelectedCourseId() {
        String selectedListItem = departmentCoursesGraphicalList.getSelectedValue();
        selectedCourseId = ThumbnailIdParser.getIdFromThumbnailText(selectedListItem, " - ");
    }

    @Override
    public void updatePanel() {
        if (selectedDepartmentNameString == null) return;

        if (departmentCourseThumbnailTexts != null) {
            updatePanelNormally();
        } else { // setting for the first time
            updatePanelForFirstTime();
        }
    }

    private void updatePanelNormally() {
        updateCourseThumbnailDTOs();
        String[] previousDepartmentCourseThumbnailTexts = Arrays.copyOf(departmentCourseThumbnailTexts,
                departmentCourseThumbnailTexts.length);
        updateCourseThumbnailTexts();
        Arrays.stream(previousDepartmentCourseThumbnailTexts)
                .filter(e -> !arrayContains(departmentCourseThumbnailTexts, e))
                .forEach(e -> departmentCoursesListModel.removeElement(e));
        Arrays.stream(departmentCourseThumbnailTexts)
                .filter(e -> !arrayContains(previousDepartmentCourseThumbnailTexts, e))
                .forEach(e -> departmentCoursesListModel.add(0, e));
    }

    private void updatePanelForFirstTime() {
        updateCourseThumbnailDTOs();
        updateCourseThumbnailTexts();
        Arrays.stream(departmentCourseThumbnailTexts)
                .forEach(e -> departmentCoursesListModel.addElement(e));
    }

    private boolean arrayContains(String[] array, String targetElement) {
        return Arrays.stream(array).anyMatch(e -> e.equals(targetElement));
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

        departmentCourseThumbnailDTOs = (ArrayList<CourseThumbnailDTO>) response.get("courseThumbnailDTOs");
        // TODO
    }

    @Override
    protected void updateCourseThumbnailTexts() {
        departmentCourseThumbnailTexts = new String[departmentCourseThumbnailDTOs.size()];
        for (int i = 0; i < departmentCourseThumbnailDTOs.size(); i++) {
            departmentCourseThumbnailTexts[i] = departmentCourseThumbnailDTOs.get(i).toString();
        }
    }
}