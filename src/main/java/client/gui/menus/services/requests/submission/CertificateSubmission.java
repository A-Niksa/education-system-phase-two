package client.gui.menus.services.requests.submission;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CertificateSubmission extends DynamicPanelTemplate {
    private Student student;
    private JLabel generatingPrompt;
    private JButton generateButton;
    private JSeparator separator;
    private JLabel yourCertificatePrompt;
    private JLabel certificateText;

    public CertificateSubmission(MainFrame mainFrame, MainMenu mainMenu, User user, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        student = (Student) user;
        configIdentifier = ConfigFileIdentifier.GUI_CERTIFICATE_SUBMISSION;
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void initializeComponents() {
        generatingPrompt = new JLabel(ConfigManager.getString(configIdentifier, "generatingPromptM"));
        generateButton = new JButton(ConfigManager.getString(configIdentifier, "generateButtonM"));
        separator = new JSeparator();
        yourCertificatePrompt = new JLabel();
        certificateText = new JLabel();
    }

    @Override
    protected void alignComponents() {
        generatingPrompt.setBounds(ConfigManager.getInt(configIdentifier, "generatingPromptX"),
                ConfigManager.getInt(configIdentifier, "generatingPromptY"),
                ConfigManager.getInt(configIdentifier, "generatingPromptW"),
                ConfigManager.getInt(configIdentifier, "generatingPromptH"));
        generatingPrompt.setFont(new Font("", Font.BOLD,
                ConfigManager.getInt(configIdentifier, "generatingPromptFontSize")));
        add(generatingPrompt);
        generateButton.setBounds(ConfigManager.getInt(configIdentifier, "generateButtonX"),
                ConfigManager.getInt(configIdentifier, "generateButtonY"),
                ConfigManager.getInt(configIdentifier, "generateButtonW"),
                ConfigManager.getInt(configIdentifier, "generateButtonH"));
        add(generateButton);
        separator.setOrientation(SwingConstants.VERTICAL);
        separator.setBackground(Color.BLACK);
        separator.setForeground(Color.BLACK);
        separator.setBounds(ConfigManager.getInt(configIdentifier, "separatorX"),
                ConfigManager.getInt(configIdentifier, "separatorY"),
                ConfigManager.getInt(configIdentifier, "separatorW"),
                ConfigManager.getInt(configIdentifier, "separatorH"));
        add(separator);
        yourCertificatePrompt.setBounds(ConfigManager.getInt(configIdentifier, "yourCertificatePromptX"),
                ConfigManager.getInt(configIdentifier, "yourCertificatePromptY"),
                ConfigManager.getInt(configIdentifier, "yourCertificatePromptW"),
                ConfigManager.getInt(configIdentifier, "yourCertificatePromptH"));
        add(yourCertificatePrompt);
        certificateText.setBounds(ConfigManager.getInt(configIdentifier, "certificateTextX"),
                ConfigManager.getInt(configIdentifier, "certificateTextY"),
                ConfigManager.getInt(configIdentifier, "certificateTextW"),
                ConfigManager.getInt(configIdentifier, "certificateTextH"));
        certificateText.setFont(new Font("", Font.BOLD,
                ConfigManager.getInt(configIdentifier, "certificateTextFontSize")));
        add(certificateText);
    }

    @Override
    protected void connectListeners() {
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Response response = clientController.askForCertificate(offlineModeDTO.getId());
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    yourCertificatePrompt.setText(ConfigManager.getString(configIdentifier, "yourCertificatePromptM"));
                    certificateText.setText((String) response.get("certificateText"));
                    MasterLogger.clientInfo(clientController.getId(), "Received an enrolment certificate",
                            "connectListeners", getClass());
                }
            }
        });
    }

    @Override
    protected void updatePanel() {
    }
}
