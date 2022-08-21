package client.gui.menus.coursewares.coursewaresview;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.DTOs.unitselection.CourseThumbnailDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class CoursewaresView extends DynamicPanelTemplate {
    protected JButton openButton;
    protected DefaultListModel<String> listModel;
    protected JList<String> graphicalList;
    protected JScrollPane scrollPane;
    protected String[] courseThumbnailTexts;
    protected ArrayList<CourseThumbnailDTO> courseThumbnailDTOs;
    protected JButton calendarButton;

    public CoursewaresView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        configIdentifier = ConfigFileIdentifier.GUI_COURSEWARES_VIEW;
        updateCourseThumbnailDTOs();
        updateCourseThumbnailTexts();
        drawPanel();
    }

    protected abstract void updateCourseThumbnailDTOs();

    private void updateCourseThumbnailTexts() {
        courseThumbnailTexts = new String[courseThumbnailDTOs.size()];
        for (int i = 0; i < courseThumbnailDTOs.size(); i++) {
            courseThumbnailTexts[i] = courseThumbnailDTOs.get(i).toShortenedString();
        }
    }

    @Override
    protected void updatePanel() {
        updateCourseThumbnailDTOs();
        String[] previousCourseThumbnailTexts = Arrays.copyOf(courseThumbnailTexts,
                courseThumbnailTexts.length);
        updateCourseThumbnailTexts();
        Arrays.stream(previousCourseThumbnailTexts)
                .filter(e -> !arrayContains(courseThumbnailTexts, e))
                .forEach(e -> listModel.removeElement(e));
        Arrays.stream(courseThumbnailTexts)
                .filter(e -> !arrayContains(previousCourseThumbnailTexts, e))
                .forEach(e -> listModel.add(0, e));
    }

    private boolean arrayContains(String[] array, String targetElement) {
        return Arrays.stream(array).anyMatch(e -> e.equals(targetElement));
    }

    @Override
    protected void initializeComponents() {
        openButton = new JButton(ConfigManager.getString(configIdentifier, "openButtonM"));
        calendarButton = new JButton(ConfigManager.getString(configIdentifier, "calendarButtonM"));

        listModel = new DefaultListModel<>();
        Arrays.stream(courseThumbnailTexts).forEach(e -> listModel.addElement(e));
        graphicalList = new JList<>(listModel);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(graphicalList);
    }

    @Override
    protected void alignComponents() {
        openButton.setBounds(ConfigManager.getInt(configIdentifier, "openButtonX"),
                ConfigManager.getInt(configIdentifier, "openButtonY"),
                ConfigManager.getInt(configIdentifier, "openButtonW"),
                ConfigManager.getInt(configIdentifier, "openButtonH"));
        add(openButton);
        calendarButton.setBounds(ConfigManager.getInt(configIdentifier, "calendarButtonX"),
                ConfigManager.getInt(configIdentifier, "calendarButtonY"),
                ConfigManager.getInt(configIdentifier, "calendarButtonW"),
                ConfigManager.getInt(configIdentifier, "calendarButtonH"));
        add(calendarButton);

        graphicalList.setLayoutOrientation(JList.VERTICAL);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);
    }
}