package client.gui.menus.messaging.messengerviews;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.network.DTOs.messaging.ConversationThumbnailDTO;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class MessengerView extends DynamicPanelTemplate {
    protected JButton openConversationButton;
    protected JButton newConversationButton;
    protected DefaultListModel<String> listModel;
    protected JList<String> graphicalList;
    protected JScrollPane scrollPane;
    protected ArrayList<ConversationThumbnailDTO> conversationThumbnailDTOs;
    protected String[] conversationThumbnailTexts;

    public MessengerView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        configIdentifier = ConfigFileIdentifier.GUI_MESSENGER_VIEW;
        conversationThumbnailDTOs = new ArrayList<>();
        updateConversationThumbnailDTOs();
        updateConversationThumbnailTexts();
        drawPanel();
    }

    @Override
    protected void updatePanel() {
        if (conversationThumbnailDTOs.isEmpty()) return;
        updateConversationThumbnailDTOs();
        String[] previousConversationThumbnailTexts = Arrays.copyOf(conversationThumbnailTexts,
                conversationThumbnailTexts.length);
        updateConversationThumbnailTexts();
        Arrays.stream(conversationThumbnailTexts)
                .filter(e -> !arrayContains(previousConversationThumbnailTexts, e))
                .forEach(e -> listModel.addElement(e));
    }

    private boolean arrayContains(String[] array, String targetElement) {
        return Arrays.stream(array).anyMatch(e -> e.equals(targetElement));
    }

    private void updateConversationThumbnailTexts() {
        conversationThumbnailTexts = new String[conversationThumbnailDTOs.size()];
        for (int i = 0; i < conversationThumbnailDTOs.size(); i++) {
            conversationThumbnailTexts[i] = conversationThumbnailDTOs.get(i).toString();
        }
    }

    protected void updateConversationThumbnailDTOs() {
        Response response = clientController.getConversationThumbnailDTOs(offlineModeDTO.getId());
        if (response == null) return;
        conversationThumbnailDTOs = (ArrayList<ConversationThumbnailDTO>) response.get("conversationThumbnailDTOs");
    }

    @Override
    protected void initializeComponents() {
        openConversationButton = new JButton(ConfigManager.getString(configIdentifier, "openConversationButtonM"));
        newConversationButton = new JButton(ConfigManager.getString(configIdentifier, "newConversationButtonM"));
        updateConversationThumbnailTexts();
        listModel = new DefaultListModel<>();
        Arrays.stream(conversationThumbnailTexts).forEach(e -> listModel.addElement(e));
        graphicalList = new JList<>(listModel);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(graphicalList);
    }

    @Override
    protected void alignComponents() {
        openConversationButton.setBounds(ConfigManager.getInt(configIdentifier, "openConversationButtonX"),
                ConfigManager.getInt(configIdentifier, "openConversationButtonY"),
                ConfigManager.getInt(configIdentifier, "openConversationButtonW"),
                ConfigManager.getInt(configIdentifier, "openConversationButtonH"));
        add(openConversationButton);
        newConversationButton.setBounds(ConfigManager.getInt(configIdentifier, "newConversationButtonX"),
                ConfigManager.getInt(configIdentifier, "newConversationButtonY"),
                ConfigManager.getInt(configIdentifier, "newConversationButtonW"),
                ConfigManager.getInt(configIdentifier, "newConversationButtonH"));
        add(newConversationButton);
        graphicalList.setLayoutOrientation(JList.VERTICAL);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);
    }
}