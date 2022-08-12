package client.gui.menus.messaging;

import client.controller.ClientController;
import client.gui.utils.ConversationDisplayUtils;
import shareables.network.DTOs.ConversationDTO;
import shareables.network.DTOs.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;

import javax.swing.*;
import java.awt.*;

public class ConversationChattingPanel extends JPanel {
    private ConfigFileIdentifier configIdentifier;
    private ConversationDTO conversationDTO;
    private ClientController clientController;
    private OfflineModeDTO offlineModeDTO;
    private String contactId;
    private String contactName;
    private JPanel fillerPanel;
    private JTextArea conversationTextArea;
    private GridBagConstraints gridBagConstraints;

    public ConversationChattingPanel(ConfigFileIdentifier configIdentifier, ConversationDTO conversationDTO,
                                     ClientController clientController, OfflineModeDTO offlineModeDTO,
                                     String contactId) {
        this.configIdentifier = configIdentifier;
        this.conversationDTO = conversationDTO;
        this.clientController = clientController;
        this.offlineModeDTO = offlineModeDTO;
        this.contactId = contactId;
        setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();
        drawPanel();
        updateConversationTextArea();
    }

    public void updateConversationChattingPanel() {
        updateConversationDTO();
        updateConversationTextArea();
    }

    private void updateConversationDTO() {
        Response response = clientController.getContactConversationDTO(offlineModeDTO.getId(), contactId);
        if (response == null) return;
        conversationDTO = (ConversationDTO) response.get("conversationDTO");
    }

    private void updateConversationTextArea() {
        String conversationText = ConversationDisplayUtils.getDisplayableConversationText(
                conversationDTO.getConversation().getMessages(), offlineModeDTO.getId(), conversationDTO.getContactName());
        conversationTextArea.setText(conversationText);
    }

    private void drawPanel() {
        initializeComponents();
        alignComponents();
        repaint();
        revalidate();
    }

    private void initializeComponents() {
        fillerPanel = new JPanel();
        conversationTextArea = new JTextArea();
    }

    private void alignComponents() {
        gridBagConstraints.fill = GridBagConstraints.BOTH;

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        fillerPanel.setBackground(Color.WHITE);
        add(fillerPanel, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        conversationTextArea.setEditable(false);
        add(conversationTextArea, gridBagConstraints);
    }
}
