package client.gui.menus.services.requests.submission;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.models.pojos.academicrequests.AcademicRequestStatus;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DroppingOutSubmission extends DynamicPanelTemplate {
    private JLabel submissionPrompt;
    private JButton submitRequest;
    private JLabel submittedPrompt;
    private JLabel resultPrompt;
    private AcademicRequestStatus academicRequestStatus;
    private boolean academicRequestHasBeenSubmitted;

    public DroppingOutSubmission(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        configIdentifier = ConfigFileIdentifier.GUI_DROPPING_OUT_SUBMISSION;
        submittedPrompt = new JLabel();
        resultPrompt = new JLabel();
        updateDroppingOutStatus();
        updateSubmittedPrompt();
        updateResultPrompt();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    private void updateDroppingOutStatus() {
        Response response = clientController.getDroppingOutSubmissionStatus(offlineModeDTO.getId());
        if (response == null) return;
        academicRequestStatus = (AcademicRequestStatus) response.get("academicRequestStatus");
        academicRequestHasBeenSubmitted = (boolean) response.get("academicRequestHasBeenSubmitted");
    }

    private void updateResultPrompt() {
        if (academicRequestStatus == AcademicRequestStatus.ACCEPTED) {
            resultPrompt.setText(ConfigManager.getString(configIdentifier, "requestAcceptedM"));
        } else if (academicRequestStatus == AcademicRequestStatus.DECLINED) {
            resultPrompt.setText(ConfigManager.getString(configIdentifier, "requestDeclinedM"));
        } else {
            resultPrompt.setText("");
        }
    }

    private void updateSubmittedPrompt() {
        if (academicRequestHasBeenSubmitted) {
            submittedPrompt.setText(ConfigManager.getString(configIdentifier, "submittedPromptM"));
        } else {
            submittedPrompt.setText("");
        }
    }

    @Override
    protected void initializeComponents() {
        submissionPrompt = new JLabel(ConfigManager.getString(configIdentifier, "submissionPromptM"));
        submitRequest = new JButton(ConfigManager.getString(configIdentifier, "submitRequestM"));
    }

    @Override
    protected void alignComponents() {
        submissionPrompt.setBounds(ConfigManager.getInt(configIdentifier, "submissionPromptX"),
                ConfigManager.getInt(configIdentifier, "submissionPromptY"),
                ConfigManager.getInt(configIdentifier, "submissionPromptW"),
                ConfigManager.getInt(configIdentifier, "submissionPromptH"));
        submissionPrompt.setFont(new Font("", Font.BOLD,
                ConfigManager.getInt(configIdentifier, "submissionPromptFontSize")));
        add(submissionPrompt);
        submitRequest.setBounds(ConfigManager.getInt(configIdentifier, "submitRequestX"),
                ConfigManager.getInt(configIdentifier, "submitRequestY"),
                ConfigManager.getInt(configIdentifier, "submitRequestW"),
                ConfigManager.getInt(configIdentifier, "submitRequestH"));
        add(submitRequest);
        submittedPrompt.setBounds(ConfigManager.getInt(configIdentifier, "submittedPromptX"),
                ConfigManager.getInt(configIdentifier, "submittedPromptY"),
                ConfigManager.getInt(configIdentifier, "submittedPromptW"),
                ConfigManager.getInt(configIdentifier, "submittedPromptH"));
        add(submittedPrompt);
        resultPrompt.setBounds(ConfigManager.getInt(configIdentifier, "resultPromptX"),
                ConfigManager.getInt(configIdentifier, "resultPromptY"),
                ConfigManager.getInt(configIdentifier, "resultPromptW"),
                ConfigManager.getInt(configIdentifier, "resultPromptH"));
        add(resultPrompt);
    }

    @Override
    protected void connectListeners() {
        submitRequest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Response response = clientController.askForDroppingOut(offlineModeDTO.getId());
                if (response == null) return;
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    MasterLogger.clientInfo(clientController.getId(), "Submitted a drop-out request",
                            "connectListeners", getClass());
                    updateDroppingOutStatus();
                    updateSubmittedPrompt();
                }
            }
        });
    }

    @Override
    protected void updatePanel() {
        updateDroppingOutStatus();
        updateSubmittedPrompt();
        updateResultPrompt();
    }
}
