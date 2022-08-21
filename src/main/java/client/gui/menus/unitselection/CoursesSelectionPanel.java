package client.gui.menus.unitselection;

import client.controller.ClientController;
import client.gui.MainFrame;
import client.gui.utils.ErrorUtils;
import client.locallogic.menus.messaging.ThumbnailParser;
import client.locallogic.menus.unitselection.CourseSelectionUtils;
import client.locallogic.menus.unitselection.GroupThumbnailParser;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.DTOs.unitselection.CourseThumbnailDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class CoursesSelectionPanel extends JPanel {
    protected MainFrame mainFrame;
    protected UnitSelectionMenu unitSelectionMenu;
    protected ClientController clientController;
    protected OfflineModeDTO offlineModeDTO;
    protected ConfigFileIdentifier configIdentifier;
    protected DefaultListModel<String> coursesListModel;
    protected JList<String> coursesGraphicalList;
    protected JButton requestAcquisitionButton;
    protected JButton changeGroupButton;
    protected JButton acquireButton;
    protected JButton removeButton;
    protected JButton pinButton;
    protected JButton unpinButton;
    protected String selectedCourseId;
    protected ArrayList<CourseThumbnailDTO> courseThumbnailDTOs;
    protected String[] courseThumbnailTexts;

    public CoursesSelectionPanel(MainFrame mainFrame, UnitSelectionMenu unitSelectionMenu, ClientController clientController,
                                 OfflineModeDTO offlineModeDTO) {
        this.mainFrame = mainFrame;
        this.unitSelectionMenu = unitSelectionMenu;
        this.clientController = clientController;
        this.offlineModeDTO = offlineModeDTO;
        setLayout(null);
        configIdentifier = ConfigFileIdentifier.GUI_COURSES_SELECTION_PANEL;
        courseThumbnailDTOs = new ArrayList<>();
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

        if (courseThumbnailDTO.isPinnedToFavorites()) {
            add(unpinButton);
        } else {
            add(pinButton);
        }
    }

    protected void purgeAndUpdateGraphicalList() {
        if (courseThumbnailTexts == null) return;

        updateCourseThumbnailDTOs();
        updateCourseThumbnailTexts();
        coursesListModel.removeAllElements();
        Arrays.stream(courseThumbnailTexts)
                .forEach(e -> coursesListModel.addElement(e));
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
                updateGraphicalListAndRemoveItsButtons();
            }
        });

        removeButton.addActionListener(actionEvent -> {
            Response response = clientController.removeAcquiredCourse(selectedCourseId, offlineModeDTO.getId());
            if (response == null) return;

            if (response.getResponseStatus() == ResponseStatus.OK) {
                JOptionPane.showMessageDialog(mainFrame, response.getUnsolicitedMessage());
                MasterLogger.clientInfo(clientController.getId(), response.getUnsolicitedMessage(),
                        "connectPreliminaryListeners", getClass());
                updateGraphicalListAndRemoveItsButtons();
            }
        });

        pinButton.addActionListener(actionEvent -> {
            Response response = clientController.pinCourseToFavorites(selectedCourseId, offlineModeDTO.getId());
            if (response == null) return;

            if (response.getResponseStatus() == ResponseStatus.OK) {
                JOptionPane.showMessageDialog(mainFrame, response.getUnsolicitedMessage());
                MasterLogger.clientInfo(clientController.getId(), response.getUnsolicitedMessage(),
                        "connectPreliminaryListeners", getClass());
                updateGraphicalListAndRemoveItsButtons();
            }
        });

        unpinButton.addActionListener(actionEvent -> {
            Response response = clientController.unpinCourseFromFavorites(selectedCourseId, offlineModeDTO.getId());
            if (response == null) return;

            if (response.getResponseStatus() == ResponseStatus.OK) {
                JOptionPane.showMessageDialog(mainFrame, response.getUnsolicitedMessage());
                MasterLogger.clientInfo(clientController.getId(), response.getUnsolicitedMessage(),
                        "connectPreliminaryListeners", getClass());
                updateGraphicalListAndRemoveItsButtons();
            }
        });

        requestAcquisitionButton.addActionListener(actionEvent -> {
            Response response = clientController.requestCourseAcquisition(selectedCourseId, offlineModeDTO.getId());
            if (response == null) return;

            if (response.getResponseStatus() == ResponseStatus.OK) {
                JOptionPane.showMessageDialog(mainFrame, response.getUnsolicitedMessage());
                MasterLogger.clientInfo(clientController.getId(), response.getUnsolicitedMessage(),
                        "connectPreliminaryListeners", getClass());
                updateGraphicalListAndRemoveItsButtons();
            }
        });

        changeGroupButton.addActionListener(actionEvent -> {
            Response response = clientController.getCourseGroupsThumbnailDTOs(selectedCourseId, offlineModeDTO.getId());
            if (response == null) return;

            ArrayList<CourseThumbnailDTO> courseThumbnailDTOs =
                    (ArrayList<CourseThumbnailDTO>) response.get("courseThumbnailDTOs");

            String groupNumberString = JOptionPane.showInputDialog(mainFrame,
                    GroupThumbnailParser.parseThumbnailDTOsAsString(courseThumbnailDTOs, configIdentifier));
            if (groupNumberString == null) return;
            int groupNumber = Integer.parseInt(groupNumberString);

            response = clientController.changeGroupNumber(selectedCourseId, groupNumber, offlineModeDTO.getId());
            if (response == null) return;

            if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                MasterLogger.clientError(clientController.getId(), response.getErrorMessage(),
                        "connectPreliminaryListeners", getClass());
            } else {
                JOptionPane.showMessageDialog(mainFrame, response.getUnsolicitedMessage());
                MasterLogger.clientInfo(clientController.getId(), response.getUnsolicitedMessage(),
                        "connectPreliminaryListeners", getClass());
                updateGraphicalListAndRemoveItsButtons();
            }
        });
    }

    protected void connectListSelectionListeners() {
        coursesGraphicalList.addListSelectionListener(actionEvent -> {
            if (!actionEvent.getValueIsAdjusting()) {
                updateGraphicalListButtons();
            }
        });
    }

    private void updateGraphicalListAndRemoveItsButtons() {
        coursesGraphicalList.clearSelection();
        updatePanel();
        removePreviousCourseSelectionButtons();
        repaint();
    }

    private void updateGraphicalListButtons() {
        if (coursesGraphicalList.getSelectedValue() == null) return;

        updateSelectedCourseId();

        CourseThumbnailDTO selectedCourseThumbnailDTO = CourseSelectionUtils.getCourseThumbnailDTOWithId(selectedCourseId,
                courseThumbnailDTOs);
        removePreviousCourseSelectionButtons();
        setAppropriateCourseSelectionButtons(selectedCourseThumbnailDTO);
        repaint();
//        revalidate();
    }

    private void updateSelectedCourseId() {
        String selectedListItem = coursesGraphicalList.getSelectedValue();
        selectedCourseId = ThumbnailParser.getIdFromThumbnailText(selectedListItem, " - ");
    }

    protected void drawPanel() {
        initializeComponents();
        alignComponents();
        connectListeners();
        connectListSelectionListeners();
    }

    protected abstract void initializeComponents();

    protected abstract void alignComponents();

    protected abstract void connectListeners();

    protected void updateCourseThumbnailTexts() {
        courseThumbnailTexts = new String[courseThumbnailDTOs.size()];
        for (int i = 0; i < courseThumbnailDTOs.size(); i++) {
            courseThumbnailTexts[i] = courseThumbnailDTOs.get(i).toString();
        }
    }

    protected boolean arrayContains(String[] array, String targetElement) {
        return Arrays.stream(array).anyMatch(e -> e.equals(targetElement));
    }

    public abstract void updatePanel();

    protected abstract void updateCourseThumbnailDTOs();
}
