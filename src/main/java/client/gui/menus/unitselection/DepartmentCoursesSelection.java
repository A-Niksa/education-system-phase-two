package client.gui.menus.unitselection;

import client.locallogic.menus.messaging.ThumbnailIdParser;
import client.locallogic.menus.unitselection.CourseSelectionUtils;
import shareables.network.DTOs.unitselection.CourseThumbnailDTO;
import shareables.utils.config.ConfigManager;

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
    private String[] departmentNames;
    private JComboBox<String> departmentNamesBox;
    private JButton chooseDepartmentButton;
    private SortingType sortingType;
    private JButton sortByCourseNameButton;
    private JButton sortByExamDateButton;
    private JButton sortByDegreeLevelButton;

    public DepartmentCoursesSelection(UnitSelectionMenu unitSelectionMenu) {
        super(unitSelectionMenu);
        sortingType = SortingType.NONE;
        updateDepartmentCourseThumbnailDTOs();
        updateDepartmentCourseThumbnailTexts();
        updateDepartmentNames();
        drawPanel();
    }

    @Override
    protected void initializeComponents() {
        departmentNamesBox = new JComboBox<>(departmentNames);
        chooseDepartmentButton = new JButton(ConfigManager.getString(configIdentifier, "chooseDepartmentButtonM"));
        sortByCourseNameButton = new JButton(ConfigManager.getString(configIdentifier, "sortByCourseNameButtonM"));
        sortByExamDateButton = new JButton(ConfigManager.getString(configIdentifier, "sortByExamDateButtonM"));
        sortByDegreeLevelButton = new JButton(ConfigManager.getString(configIdentifier, "sortByDegreeLevelButtonM"));

        departmentCoursesListModel = new DefaultListModel<>();
        Arrays.stream(departmentCourseThumbnailTexts).forEach(e -> departmentCoursesListModel.addElement(e));
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

    }

    @Override
    protected void connectListSelectionListeners() {
        departmentCoursesGraphicalList.addListSelectionListener(actionEvent -> {
            String selectedListItem = departmentCoursesGraphicalList.getSelectedValue();
            String selectedCourseId = ThumbnailIdParser.getIdFromThumbnailText(selectedListItem, " - ");

            CourseThumbnailDTO selectedCourseThumbnailDTO = CourseSelectionUtils.getCourseThumbnailDTOWithId(selectedCourseId,
                    departmentCourseThumbnailDTOs);

            removePreviousCourseSelectionButtons();
            setAppropriateCourseSelectionButtons(selectedCourseThumbnailDTO);
        });
    }

    @Override
    public void updatePanel() {

    }
}
