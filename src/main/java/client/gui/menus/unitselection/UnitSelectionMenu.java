package client.gui.menus.unitselection;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.DTOs.unitselection.CourseThumbnailDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import javax.swing.*;
import java.util.ArrayList;

public class UnitSelectionMenu extends DynamicPanelTemplate {
    private DepartmentCoursesSelection departmentCoursesPanel;
    private PinnedCoursesSelection pinnedCoursesPanel;
    private JTabbedPane tabbedPane;
    private ArrayList<CourseThumbnailDTO> departmentCourseThumbnailDTOs;
    private ArrayList<CourseThumbnailDTO> recommendedCourseThumbnailDTOs; // for pinnedCoursesPanel
    private ArrayList<CourseThumbnailDTO> favoriteCourseThumbnailDTOs; // for pinnedCoursesPanel

    public UnitSelectionMenu(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        configIdentifier = ConfigFileIdentifier.GUI_UNIT_SELECTION_MENU;
        updateDepartmentCourseThumbnailDTOs();
        updatePinnedCourseThumbnailDTOs();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    private void updateDepartmentCourseThumbnailDTOs() {
        // TODO
    }

    private void updatePinnedCourseThumbnailDTOs() {
        updateRecommendedCourseThumbnailDTOs();
        updateFavoriteCourseThumbnailDTOs();
    }

    private void updateRecommendedCourseThumbnailDTOs() {
        // TODO
    }

    private void updateFavoriteCourseThumbnailDTOs() {
        // TODO:
    }

    @Override
    protected void updatePanel() {
        updateDepartmentCourseThumbnailDTOs();
        updatePinnedCourseThumbnailDTOs();

        departmentCoursesPanel.updatePanel();
        pinnedCoursesPanel.updatePanel();
    }

    @Override
    protected void initializeComponents() {
        departmentCoursesPanel = new DepartmentCoursesSelection(this);
        pinnedCoursesPanel = new PinnedCoursesSelection(this);
        tabbedPane = new JTabbedPane();
    }

    @Override
    protected void alignComponents() {
        tabbedPane.add(ConfigManager.getString(configIdentifier, "departmentCoursesPanelHeader"),
                departmentCoursesPanel);
        tabbedPane.add(ConfigManager.getString(configIdentifier, "pinnedCoursesPanelHeader"),
                pinnedCoursesPanel);

        tabbedPane.setBounds(ConfigManager.getInt(configIdentifier, "tabbedPaneX"),
                ConfigManager.getInt(configIdentifier, "tabbedPaneY"),
                ConfigManager.getInt(configIdentifier, "tabbedPaneW"),
                ConfigManager.getInt(configIdentifier, "tabbedPaneH"));
        add(tabbedPane);
    }

    @Override
    protected void connectListeners() {
    }
}
