package client.gui.menus.services.requests.submission;

import client.controller.ClientController;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import javax.swing.*;
import java.util.ArrayList;

public class RecommendationDisplayer {
    private RecommendationSubmission panel;
    private ConfigFileIdentifier configIdentifier;
    private OfflineModeDTO offlineModeDTO;
    private ArrayList<String> recommendationTexts; // only includes accepted recommendation requests
    private ArrayList<JLabel> currentRecommendations;
    private ClientController clientController;

    public RecommendationDisplayer(RecommendationSubmission panel, ArrayList<JLabel> currentRecommendations,
                                   OfflineModeDTO offlineModeDTO, ClientController clientController) {
        this.panel = panel;
        this.currentRecommendations = currentRecommendations;
        this.offlineModeDTO = offlineModeDTO;
        this.clientController = clientController;
        configIdentifier = ConfigFileIdentifier.GUI_RECOMMENDATION_SUBMISSION;
    }

    public void displayRecommendations() {
        updateRecommendations();
        alignRecommendations();
    }

    private void updateRecommendationTexts() {
        Response response = clientController.getStudentRecommendationTexts(offlineModeDTO.getId());
        if (response == null) return;
        recommendationTexts = (ArrayList<String>) response.get("recommendationTexts");
    }

    private void updateRecommendations() {
        updateRecommendationTexts();
        currentRecommendations.clear();
        for (String recommendationText : recommendationTexts) {
            JLabel recommendationTextLabel = new JLabel(recommendationText);
            currentRecommendations.add(recommendationTextLabel);
        }
    }

    private void alignRecommendations() {
        int currentX = ConfigManager.getInt(configIdentifier, "startingX");
        int currentY = ConfigManager.getInt(configIdentifier, "startingY");
        int labelWidth = ConfigManager.getInt(configIdentifier, "labelW");
        int labelHeight = ConfigManager.getInt(configIdentifier, "labelH");
        for (JLabel recommendationTextLabel : currentRecommendations) {
            recommendationTextLabel.setBounds(currentX, currentY, labelWidth, labelHeight);
            panel.add(recommendationTextLabel);
            recommendationTextLabel.repaint();
            currentY += labelHeight;
        }
    }
}
