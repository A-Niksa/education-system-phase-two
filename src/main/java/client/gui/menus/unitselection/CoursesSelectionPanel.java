package client.gui.menus.unitselection;

import shareables.utils.config.ConfigFileIdentifier;

import javax.swing.*;

public abstract class CoursesSelectionPanel extends JPanel {
    private UnitSelectionMenu unitSelectionMenu;
    private ConfigFileIdentifier configIdentifier;
    private JButton requestAcquisitionButton;
    private JButton acquireButton;
    private JButton removeButton;
    private JButton pinButton;
    private JButton unpinButton;

    public CoursesSelectionPanel(UnitSelectionMenu unitSelectionMenu) {
        this.unitSelectionMenu = unitSelectionMenu;
        configIdentifier = ConfigFileIdentifier.GUI_COURSES_SELECTION_PANEL;
        drawPreliminaryPanel();
    }

    private void drawPreliminaryPanel() {
        initializePreliminaryComponents();
        alignPreliminaryComponents();
        connectPreliminaryListeners();
    }

    private void initializePreliminaryComponents() {
        // TODO
    }

    private void alignPreliminaryComponents() {
        // TODO
    }

    private void connectPreliminaryListeners() {
        // TODO
    }

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
