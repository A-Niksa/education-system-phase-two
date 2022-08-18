package client.gui.menus.unitselection;

import client.controller.ClientController;
import client.gui.MainFrame;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.DTOs.unitselection.CourseThumbnailDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class PinnedCoursesSelection extends CoursesSelectionPanel {
    private JScrollPane pinnedCoursesScrollPane;

    public PinnedCoursesSelection(MainFrame mainFrame, UnitSelectionMenu unitSelectionMenu,
                                  ClientController clientController, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, unitSelectionMenu, clientController, offlineModeDTO);
        updateCourseThumbnailDTOs();
        updateCourseThumbnailTexts();
        drawPanel();
    }

    @Override
    protected void initializeComponents() {
        coursesListModel = new DefaultListModel<>();
        Arrays.stream(courseThumbnailTexts).forEach(e -> coursesListModel.addElement(e));

        coursesGraphicalList = new JList<>(coursesListModel);
        pinnedCoursesScrollPane = new JScrollPane();
        pinnedCoursesScrollPane.setViewportView(coursesGraphicalList);
    }

    @Override
    protected void alignComponents() {
        pinnedCoursesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        pinnedCoursesScrollPane.setBounds(ConfigManager.getInt(configIdentifier, "pinnedCoursesScrollPaneX"),
                ConfigManager.getInt(configIdentifier, "pinnedCoursesScrollPaneY"),
                ConfigManager.getInt(configIdentifier, "pinnedCoursesScrollPaneW"),
                ConfigManager.getInt(configIdentifier, "pinnedCoursesScrollPaneH"));
        add(pinnedCoursesScrollPane);
    }

    @Override
    protected void connectListeners() {
    }

    @Override
    public void updatePanel() {
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

    @Override
    protected void updateCourseThumbnailDTOs() {
        Response response = clientController.getPinnedCourseThumbnailDTOs(offlineModeDTO.getId());
        if (response == null) return;

        courseThumbnailDTOs = (ArrayList<CourseThumbnailDTO>) response.get("courseThumbnailDTOs");
    }
}
