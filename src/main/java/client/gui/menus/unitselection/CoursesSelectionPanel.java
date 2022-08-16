package client.gui.menus.unitselection;

import shareables.network.DTOs.unitselection.CourseThumbnailDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import javax.swing.*;

public abstract class CoursesSelectionPanel extends JPanel {
    protected UnitSelectionMenu unitSelectionMenu;
    protected ConfigFileIdentifier configIdentifier;
    protected JButton requestAcquisitionButton;
    protected JButton changeGroupButton;
    protected JButton acquireButton;
    protected JButton removeButton;
    protected JButton pinButton;
    protected JButton unpinButton;

    public CoursesSelectionPanel(UnitSelectionMenu unitSelectionMenu) {
        this.unitSelectionMenu = unitSelectionMenu;
        setLayout(null);
        configIdentifier = ConfigFileIdentifier.GUI_COURSES_SELECTION_PANEL;
        drawPreliminaryPanel();
    }

    private void drawPreliminaryPanel() {
        initializePreliminaryComponents();
        alignPreliminaryComponents();
        connectPreliminaryListeners();
    }

    private void initializePreliminaryComponents() {
        pinButton = new JButton(ConfigManager.getString(configIdentifier, "pinButtonM"));
        unpinButton = new JButton(ConfigManager.getString(configIdentifier, "unpinButtonM"));
        acquireButton = new JButton(ConfigManager.getString(configIdentifier, "acquireButtonM"));
        removeButton = new JButton(ConfigManager.getString(configIdentifier, "removeButtonM"));
        changeGroupButton = new JButton(ConfigManager.getString(configIdentifier, "changeGroupButtonM"));
        requestAcquisitionButton = new JButton(ConfigManager.getString(configIdentifier,
                "requestAcquisitionButtonM"));
    }

    private void alignPreliminaryComponents() {
        pinButton.setBounds(ConfigManager.getInt(configIdentifier, "pinButtonX"),
                ConfigManager.getInt(configIdentifier, "pinButtonY"),
                ConfigManager.getInt(configIdentifier, "pinButtonW"),
                ConfigManager.getInt(configIdentifier, "pinButtonH"));
        unpinButton.setBounds(ConfigManager.getInt(configIdentifier, "unpinButtonX"),
                ConfigManager.getInt(configIdentifier, "unpinButtonY"),
                ConfigManager.getInt(configIdentifier, "unpinButtonW"),
                ConfigManager.getInt(configIdentifier, "unpinButtonH"));
        acquireButton.setBounds(ConfigManager.getInt(configIdentifier, "acquireButtonX"),
                ConfigManager.getInt(configIdentifier, "acquireButtonY"),
                ConfigManager.getInt(configIdentifier, "acquireButtonW"),
                ConfigManager.getInt(configIdentifier, "acquireButtonH"));
        removeButton.setBounds(ConfigManager.getInt(configIdentifier, "removeButtonX"),
                ConfigManager.getInt(configIdentifier, "removeButtonY"),
                ConfigManager.getInt(configIdentifier, "removeButtonW"),
                ConfigManager.getInt(configIdentifier, "removeButtonH"));
        changeGroupButton.setBounds(ConfigManager.getInt(configIdentifier, "changeGroupButtonX"),
                ConfigManager.getInt(configIdentifier, "changeGroupButtonY"),
                ConfigManager.getInt(configIdentifier, "changeGroupButtonW"),
                ConfigManager.getInt(configIdentifier, "changeGroupButtonH"));
        requestAcquisitionButton.setBounds(ConfigManager.getInt(configIdentifier, "requestAcquisitionButtonX"),
                ConfigManager.getInt(configIdentifier, "requestAcquisitionButtonY"),
                ConfigManager.getInt(configIdentifier, "requestAcquisitionButtonW"),
                ConfigManager.getInt(configIdentifier, "requestAcquisitionButtonH"));
        // TODO
    }

    protected void removePreviousCourseSelectionButtons() {
        remove(pinButton);
        remove(unpinButton);
        remove(acquireButton);
        remove(removeButton);
        remove(changeGroupButton);
        remove(requestAcquisitionButton);
    }

    protected void setAppropriateCourseSelectionButtons(CourseThumbnailDTO courseThumbnailDTO) {
        if (courseThumbnailDTO.isAcquired()) {
            add(removeButton);
            add(changeGroupButton);
        } else {
            add(acquireButton);
            add(requestAcquisitionButton);
        }

        if (courseThumbnailDTO.isPinned()) {
            add(unpinButton);
        } else {
            add(pinButton);
        }
    }

    private void connectPreliminaryListeners() {
        // TODO
    }

    protected abstract void connectListSelectionListeners();

    protected void drawPanel() {
        initializeComponents();
        alignComponents();
        connectListeners();
    }

    protected abstract void initializeComponents();

    protected abstract void alignComponents();

    protected abstract void connectListeners();

    public abstract void updatePanel();
}
