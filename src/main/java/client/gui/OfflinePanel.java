package client.gui;

import client.controller.ClientController;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigIdSupplier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public interface OfflinePanel {
    public static final ConfigFileIdentifier CONFIG_IDENTIFIER = ConfigFileIdentifier.GUI_OFFLINE_PANEL;
    public static final List<JComponent> OFFLINE_COMPONENTS = new ArrayList<>();

    void disableOnlineComponents(); // includes disabling/stopping the panelLoop as well

    void enableOnlineComponents(); // includes enabling/starting the panelLoop as well

    default void goOffline(MainFrame mainFrame, JPanel panel, ClientController clientController) {
        disableOnlineComponents();
        setOfflineComponents(mainFrame, panel, clientController);
        MasterLogger.clientInfo(clientController.getId(), "Gui configured for offline mode", "goOffline",
                getClass());
    }

    default void refreshConnection(MainFrame mainFrame, JPanel panel, ClientController clientController) {
        clientController.attemptServerConnection();
        MasterLogger.clientInfo(clientController.getId(), "Attempting refresh", "refreshConnection",
                getClass());
        if (clientController.isClientOnline()) {
            clientController.setClientId(ConfigIdSupplier.nextClientId());
            enableOnlineComponents();
            removeOfflineComponents(panel);
            MasterLogger.clientInfo(clientController.getId(), "Gui configured for online mode",
                    "refreshConnection", getClass());
        } else {
            MasterLogger.clientInfo(clientController.getId(), ConfigManager.getString(CONFIG_IDENTIFIER,
                    "cannotConnectToServer"), "refreshConnection", getClass());
            JOptionPane.showMessageDialog(mainFrame,
                    ConfigManager.getString(CONFIG_IDENTIFIER, "cannotConnectToServer"));
        }
    }

    /**
     * offline components refers to: 1) the refresh button, and 2) the offline status label
     */
    default void setOfflineComponents(MainFrame mainFrame, JPanel panel, ClientController clientController) {
        initializeOfflineComponents();
        alignOfflineComponents(panel);
        connectOfflineComponentListeners(mainFrame, panel, clientController);
        panel.repaint();
        panel.validate();
    }

    default void initializeOfflineComponents() {
        JButton refreshButton = new JButton(ConfigManager.getString(CONFIG_IDENTIFIER, "refreshButtonM"));
        OFFLINE_COMPONENTS.add(refreshButton);
        JLabel offlineModeLabel = new JLabel(ConfigManager.getString(CONFIG_IDENTIFIER, "offlineModeLabelM"));
        OFFLINE_COMPONENTS.add(offlineModeLabel);
    }

    default void alignOfflineComponents(JPanel panel) {
        JButton refreshButton = (JButton) OFFLINE_COMPONENTS.get(0);
        refreshButton.setBounds(ConfigManager.getInt(CONFIG_IDENTIFIER, "refreshButtonX"),
                ConfigManager.getInt(CONFIG_IDENTIFIER, "refreshButtonY"),
                ConfigManager.getInt(CONFIG_IDENTIFIER, "refreshButtonW"),
                ConfigManager.getInt(CONFIG_IDENTIFIER, "refreshButtonH"));
        panel.add(refreshButton);
        JLabel offlineModeLabel = (JLabel) OFFLINE_COMPONENTS.get(1);
        offlineModeLabel.setBounds(ConfigManager.getInt(CONFIG_IDENTIFIER, "offlineModeLabelX"),
                ConfigManager.getInt(CONFIG_IDENTIFIER, "offlineModeLabelY"),
                ConfigManager.getInt(CONFIG_IDENTIFIER, "offlineModeLabelW"),
                ConfigManager.getInt(CONFIG_IDENTIFIER, "offlineModeLabelH"));
        panel.add(offlineModeLabel);
    }

    default void connectOfflineComponentListeners(MainFrame mainFrame, JPanel panel, ClientController clientController) {
        JButton refreshButton = (JButton) OFFLINE_COMPONENTS.get(0);
        refreshButton.addActionListener(actionEvent -> {
            refreshConnection(mainFrame, panel, clientController);
        });
    }

    default void removeOfflineComponents(JPanel panel) {
        OFFLINE_COMPONENTS.forEach(e -> panel.remove(e));
        OFFLINE_COMPONENTS.clear();
    }
}
