package client.gui.menus.notifications;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import client.gui.utils.ErrorUtils;
import client.locallogic.menus.messaging.ThumbnailParser;
import shareables.network.DTOs.notifications.NotificationDTO;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class NotificationsView extends DynamicPanelTemplate {
    private JButton acceptButton;
    private JButton declineButton;
    protected DefaultListModel<String> listModel;
    protected JList<String> graphicalList;
    protected JScrollPane scrollPane;
    protected ArrayList<NotificationDTO> notificationDTOs;
    protected String[] notificationTexts;

    public NotificationsView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        configIdentifier = ConfigFileIdentifier.GUI_NOTIFICATIONS_VIEW;
        notificationDTOs = new ArrayList<>();
        updateNotificationDTOs();
        updateNotificationTexts();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void updatePanel() {
        updateNotificationDTOs();
        String[] previousNotificationTexts = Arrays.copyOf(notificationTexts, notificationTexts.length);
        updateNotificationTexts();
        Arrays.stream(previousNotificationTexts)
                .filter(e -> !arrayContains(notificationTexts, e))
                .forEach(e -> listModel.removeElement(e));
        Arrays.stream(notificationTexts)
                .filter(e -> !arrayContains(previousNotificationTexts, e))
                .forEach(e -> listModel.addElement(e));
    }

    private void updateNotificationDTOs() {
        Response response = clientController.getNotificationDTOs(offlineModeDTO.getId());
        if (response == null) return;
        notificationDTOs = (ArrayList<NotificationDTO>) response.get("notificationDTOs");
    }

    private void updateNotificationTexts() {
        notificationTexts = new String[notificationDTOs.size()];
        for (int i = 0; i < notificationDTOs.size(); i++) {
            notificationTexts[i] = notificationDTOs.get(i).toString();
        }
    }

    private boolean arrayContains(String[] array, String targetElement) {
        return Arrays.stream(array).anyMatch(e -> e.equals(targetElement));
    }

    @Override
    protected void initializeComponents() {
        acceptButton = new JButton(ConfigManager.getString(configIdentifier, "acceptButtonM"));
        declineButton = new JButton(ConfigManager.getString(configIdentifier, "declineButtonM"));
        listModel = new DefaultListModel<>();
        Arrays.stream(notificationTexts).forEach(e -> listModel.addElement(e));
        graphicalList = new JList<>(listModel);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(graphicalList);
    }

    @Override
    protected void alignComponents() {
        acceptButton.setBounds(ConfigManager.getInt(configIdentifier, "acceptButtonX"),
                ConfigManager.getInt(configIdentifier, "acceptButtonY"),
                ConfigManager.getInt(configIdentifier, "acceptButtonW"),
                ConfigManager.getInt(configIdentifier, "acceptButtonH"));
        add(acceptButton);
        declineButton.setBounds(ConfigManager.getInt(configIdentifier, "declineButtonX"),
                ConfigManager.getInt(configIdentifier, "declineButtonY"),
                ConfigManager.getInt(configIdentifier, "declineButtonW"),
                ConfigManager.getInt(configIdentifier, "declineButtonH"));
        add(declineButton);

        graphicalList.setLayoutOrientation(JList.VERTICAL);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);
    }

    @Override
    protected void connectListeners() {
        acceptButton.addActionListener(actionEvent -> {
            if (showErrorIfNoNotificationHasBeenSelected()) {
                return;
            }

            String selectedListItem = graphicalList.getSelectedValue();
            String selectedNotificationId = ThumbnailParser.getIdFromThumbnailText(selectedListItem, " | ");
            Response response = clientController.acceptNotification(offlineModeDTO.getId(), selectedNotificationId);
            if (response == null) return;
            if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                MasterLogger.clientError(clientController.getId(), response.getErrorMessage(), "connectListeners",
                        getClass());
            } else {
                MasterLogger.clientInfo(clientController.getId(), response.getErrorMessage(), "connectListeners",
                        getClass());
            }
        });

        declineButton.addActionListener(actionEvent -> {
            if (showErrorIfNoNotificationHasBeenSelected()) {
                return;
            }

            String selectedListItem = graphicalList.getSelectedValue();
            String selectedNotificationId = ThumbnailParser.getIdFromThumbnailText(selectedListItem, " | ");
            Response response = clientController.declineNotification(offlineModeDTO.getId(), selectedNotificationId);
            if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                MasterLogger.clientError(clientController.getId(), response.getErrorMessage(), "connectListeners",
                        getClass());
            } else {
                MasterLogger.clientInfo(clientController.getId(), response.getErrorMessage(), "connectListeners",
                        getClass());
            }
        });
    }

    /**
     * returns true if there was an error
     */
    private boolean showErrorIfNoNotificationHasBeenSelected() {
        if (graphicalList.getSelectedIndex() == -1) {
            String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                    "noNotificationHasBeenSelected");
            JOptionPane.showMessageDialog(mainFrame, errorMessage);
            MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners", getClass());
            return true;
        }
        return false;
    }
}
