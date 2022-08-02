package client.gui.menus.services.requests.submission;

import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.main.MainMenu;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.students.Student;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DroppingOutSubmission extends PanelTemplate {
    private Student student;
    private JLabel submissionPrompt;
    private JButton submitRequest;
    private JLabel submittedPrompt;

    public DroppingOutSubmission(MainFrame mainFrame, MainMenu mainMenu, User user) {
        super(mainFrame, mainMenu);
        student = (Student) user;
        configIdentifier = ConfigFileIdentifier.GUI_DROPPING_OUT_SUBMISSION;
        submittedPrompt = new JLabel();
        updateSubmittedPrompt();
        drawPanel();
    }

    private void updateSubmittedPrompt() {
        Response response = clientController.getDroppingOutSubmissionStatus(student.getId());
        boolean academicRequestHasBeenSubmitted = (boolean) response.get("academicRequestHasBeenSubmitted");
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
    }

    @Override
    protected void connectListeners() {
        submitRequest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Response response = clientController.askForDroppingOut(student.getId());
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    MasterLogger.clientInfo(clientController.getId(), "Submitted a drop-out request",
                            "connectListeners", getClass());
                    updateSubmittedPrompt();
                }
            }
        });
    }
}
