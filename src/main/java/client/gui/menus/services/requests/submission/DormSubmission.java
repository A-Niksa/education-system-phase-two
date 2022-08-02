package client.gui.menus.services.requests.submission;

import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.main.MainMenu;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.students.Student;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DormSubmission extends PanelTemplate {
    private Student student;
    private JLabel dormPrompt;
    private JButton submitRequest;
    private JSeparator separator;
    private JLabel resultText;

    public DormSubmission(MainFrame mainFrame, MainMenu mainMenu, User user) {
        super(mainFrame, mainMenu);
        student = (Student) user;
        configIdentifier = ConfigFileIdentifier.GUI_DORM_SUBMISSION;
        drawPanel();
    }

    @Override
    protected void initializeComponents() {
        dormPrompt = new JLabel(ConfigManager.getString(configIdentifier, "dormPromptMessage"));
        submitRequest = new JButton(ConfigManager.getString(configIdentifier, "submitRequestMessage"));
        separator = new JSeparator();
        resultText = new JLabel();
    }

    @Override
    protected void alignComponents() {
        dormPrompt.setBounds(ConfigManager.getInt(configIdentifier, "dormPromptX"),
                ConfigManager.getInt(configIdentifier, "dormPromptY"),
                ConfigManager.getInt(configIdentifier, "dormPromptW"),
                ConfigManager.getInt(configIdentifier, "dormPromptH"));
        dormPrompt.setFont(new Font("", Font.BOLD,
                ConfigManager.getInt(configIdentifier, "dormPromptFontSize")));
        add(dormPrompt);
        submitRequest.setBounds(ConfigManager.getInt(configIdentifier, "submitRequestX"),
                ConfigManager.getInt(configIdentifier, "submitRequestY"),
                ConfigManager.getInt(configIdentifier, "submitRequestW"),
                ConfigManager.getInt(configIdentifier, "submitRequestH"));
        add(submitRequest);
        separator.setOrientation(SwingConstants.VERTICAL);
        separator.setBackground(Color.BLACK);
        separator.setForeground(Color.BLACK);
        separator.setBounds(ConfigManager.getInt(configIdentifier, "separatorX"),
                ConfigManager.getInt(configIdentifier, "separatorY"),
                ConfigManager.getInt(configIdentifier, "separatorW"),
                ConfigManager.getInt(configIdentifier, "separatorH"));
        add(separator);
        resultText.setBounds(ConfigManager.getInt(configIdentifier, "resultTextX"),
                ConfigManager.getInt(configIdentifier, "resultTextY"),
                ConfigManager.getInt(configIdentifier, "resultTextW"),
                ConfigManager.getInt(configIdentifier, "resultTextH"));
        resultText.setFont(new Font("", Font.BOLD,
                ConfigManager.getInt(configIdentifier, "resultTextFontSize")));
        add(resultText);
    }

    @Override
    protected void connectListeners() {
        submitRequest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                resultText.setText(ConfigManager.getString(configIdentifier, "resultTextEvaluationMessage"));
                MasterLogger.clientInfo(clientController.getId(), "Requested a dorm room",
                        "connectListeners", getClass());

                Response response = clientController.askForDorm();
                boolean willGetDorm = (boolean) response.get("willGetDorm");
                String randomResult;
                if (willGetDorm) {
                    randomResult = ConfigManager.getString(configIdentifier, "successfulResultMessage");
                } else {
                    randomResult = ConfigManager.getString(configIdentifier, "unsuccessfulResultMessage");
                }

                Timer timer = new Timer(ConfigManager.getInt(ConfigFileIdentifier.CONSTANTS, "dormRequestDelayTime"),
                        e -> resultText.setText(randomResult));
                timer.setRepeats(false);
                timer.start();

                if (willGetDorm) {
                    MasterLogger.clientInfo(clientController.getId(), "Student has been assigned a dorm room",
                            "connectListeners", getClass());
                } else {
                    MasterLogger.clientInfo(clientController.getId(), "Student was not able to get a dorm room",
                            "connectListeners", getClass());
                }
            }
        });
    }
}