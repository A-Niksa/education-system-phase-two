package client.gui.menus.unitselection;

import client.controller.ClientController;
import client.gui.MainFrame;
import client.gui.utils.ErrorUtils;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.DTOs.unitselection.CourseThumbnailDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;

public abstract class CoursesSelectionPanel extends JPanel {
    protected MainFrame mainFrame;
    protected UnitSelectionMenu unitSelectionMenu;
    protected ClientController clientController;
    protected OfflineModeDTO offlineModeDTO;
    protected ConfigFileIdentifier configIdentifier;
    protected JButton requestAcquisitionButton;
    protected JButton changeGroupButton;
    protected JButton acquireButton;
    protected JButton removeButton;
    protected JButton pinButton;
    protected JButton unpinButton;
    protected String selectedCourseId;

    public CoursesSelectionPanel(MainFrame mainFrame, UnitSelectionMenu unitSelectionMenu, ClientController clientController,
                                 OfflineModeDTO offlineModeDTO) {
        this.mainFrame = mainFrame;
        this.unitSelectionMenu = unitSelectionMenu;
        this.clientController = clientController;
        this.offlineModeDTO = offlineModeDTO;
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
        acquireButton.addActionListener(actionEvent -> {
            Response response = clientController.acquireCourse(selectedCourseId, offlineModeDTO.getId());
            if (response == null) return;

            if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                MasterLogger.clientError(clientController.getId(), response.getErrorMessage(),
                        "connectPreliminaryListeners", getClass());
            } else {
                JOptionPane.showMessageDialog(mainFrame, response.getUnsolicitedMessage());
                MasterLogger.clientInfo(clientController.getId(), response.getUnsolicitedMessage(),
                        "connectPreliminaryListeners", getClass());
            }
        });
        // TODO
    }

    protected abstract void connectListSelectionListeners();

    protected void drawPanel() {
        initializeComponents();
        alignComponents();
        connectListeners();
        connectListSelectionListeners();
    }

    protected abstract void initializeComponents();

    protected abstract void alignComponents();

    protected abstract void connectListeners();

    public abstract void updatePanel();

    protected abstract void updateCourseThumbnailDTOs();

    protected abstract void updateCourseThumbnailTexts();
}
