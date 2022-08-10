package client.gui.menus.services.requests.submission;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import client.gui.utils.ErrorUtils;
import client.locallogic.general.DatePickerConfigurationTool;
import client.locallogic.services.LocalDateTimeFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;
import shareables.utils.timing.formatting.DateLabelFormatter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Properties;

public class DefenseSubmission extends DynamicPanelTemplate {
    private Student student;
    private LocalDateTime defenseTime;
    private UtilDateModel dateModel;
    private Properties properties;
    private JDatePanelImpl datePanel;
    private JDatePickerImpl datePicker;
    private JTextField hourPicker;
    private JTextField minutePicker;
    private JButton setDefenseSlot;
    private JSeparator separator;
    private JLabel defenseSlotPrompt;
    private JLabel defenseSlotInformation;

    public DefenseSubmission(MainFrame mainFrame, MainMenu mainMenu, User user, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        student = (Student) user;
        configIdentifier = ConfigFileIdentifier.GUI_DEFENSE_SUBMISSION;
        updateDefenseTime();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    private void updateDefenseTime() {
        Response response = clientController.getDefenseTime(offlineModeDTO.getId());
        if (response == null) return;
        defenseTime = (LocalDateTime) response.get("defenseTime");
    }

    @Override
    protected void initializeComponents() {
        dateModel = new UtilDateModel();
        properties = new Properties();
        properties.put("text.today", ConfigManager.getString(configIdentifier, "propertiesTextTodayM"));
        properties.put("text.month", ConfigManager.getString(configIdentifier, "propertiesTextMonthM"));
        properties.put("text.year", ConfigManager.getString(configIdentifier, "propertiesTextYearM"));
        datePanel = new JDatePanelImpl(dateModel, properties);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        hourPicker = new JTextField(ConfigManager.getString(configIdentifier, "hourPickerM"));
        minutePicker = new JTextField(ConfigManager.getString(configIdentifier, "minutePickerM"));
        setDefenseSlot = new JButton(ConfigManager.getString(configIdentifier, "setDefenseSlotM"));
        separator = new JSeparator();
        defenseSlotPrompt = new JLabel();
        defenseSlotInformation = new JLabel();
    }

    @Override
    protected void alignComponents() {
        DatePickerConfigurationTool.configureInitialDateOfDatePicker(dateModel);
        datePicker.setBounds(ConfigManager.getInt(configIdentifier, "datePickerX"),
                ConfigManager.getInt(configIdentifier, "datePickerY"),
                ConfigManager.getInt(configIdentifier, "datePickerW"),
                ConfigManager.getInt(configIdentifier, "datePickerH"));
        add(datePicker);

        hourPicker.setBounds(ConfigManager.getInt(configIdentifier, "hourPickerX"),
                ConfigManager.getInt(configIdentifier, "hourPickerY"),
                ConfigManager.getInt(configIdentifier, "hourPickerW"),
                ConfigManager.getInt(configIdentifier, "hourPickerH"));
        add(hourPicker);
        minutePicker.setBounds(ConfigManager.getInt(configIdentifier, "minutePickerX"),
                ConfigManager.getInt(configIdentifier, "minutePickerY"),
                ConfigManager.getInt(configIdentifier, "minutePickerW"),
                ConfigManager.getInt(configIdentifier, "minutePickerH"));
        add(minutePicker);
        setDefenseSlot.setBounds(ConfigManager.getInt(configIdentifier, "setDefenseSlotX"),
                ConfigManager.getInt(configIdentifier, "setDefenseSlotY"),
                ConfigManager.getInt(configIdentifier, "setDefenseSlotW"),
                ConfigManager.getInt(configIdentifier, "setDefenseSlotH"));
        add(setDefenseSlot);

        separator.setOrientation(SwingConstants.VERTICAL);
        separator.setBackground(Color.BLACK);
        separator.setForeground(Color.BLACK);
        separator.setBounds(ConfigManager.getInt(configIdentifier, "separatorX"),
                ConfigManager.getInt(configIdentifier, "separatorY"),
                ConfigManager.getInt(configIdentifier, "separatorW"),
                ConfigManager.getInt(configIdentifier, "separatorH"));
        add(separator);

        defenseSlotPrompt.setBounds(ConfigManager.getInt(configIdentifier, "defenseSlotPromptX"),
                ConfigManager.getInt(configIdentifier, "defenseSlotPromptY"),
                ConfigManager.getInt(configIdentifier, "defenseSlotPromptW"),
                ConfigManager.getInt(configIdentifier, "defenseSlotPromptH"));
        add(defenseSlotPrompt);
        defenseSlotInformation.setBounds(ConfigManager.getInt(configIdentifier, "defenseSlotInformationX"),
                ConfigManager.getInt(configIdentifier, "defenseSlotInformationY"),
                ConfigManager.getInt(configIdentifier, "defenseSlotInformationW"),
                ConfigManager.getInt(configIdentifier, "defenseSlotInformationH"));
        defenseSlotInformation.setFont(new Font("", Font.BOLD,
                ConfigManager.getInt(configIdentifier, "defenseSlotInformationFontSize")));
        add(defenseSlotInformation);
        setDefenseSlotLabels();
    }

    private void setDefenseSlotLabels() {
        if (defenseTime != null) {
            defenseSlotPrompt.setText(ConfigManager.getString(configIdentifier, "appointedDefenseSlotPromptM"));
            String defenseTimeText = LocalDateTimeFormatter.formatExtensively(defenseTime);
            defenseSlotInformation.setText(defenseTimeText);
        } else {
            defenseSlotPrompt.setText("");
            defenseSlotInformation.setText("");
        }
    }

    @Override
    protected void connectListeners() {
        setDefenseSlot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Date selectedDate = dateModel.getValue();
                String selectedHourString = hourPicker.getText();
                int selectedHour;
                if (selectedHourString.equals(ConfigManager.getString(configIdentifier, "hourPickerM"))) {
                    selectedHour = 0;
                } else {
                    selectedHour = Integer.parseInt(selectedHourString);
                }
                String selectedMinuteString = minutePicker.getText();
                int selectedMinute;
                if (selectedMinuteString.equals(ConfigManager.getString(configIdentifier, "minutePickerM"))) {
                    selectedMinute = 0;
                } else {
                    selectedMinute = Integer.parseInt(selectedMinuteString);
                }

                MasterLogger.clientInfo(clientController.getId(), "Submitted thesis defense time",
                        "connectListeners", getClass());
                Response response = clientController.askForDefenseTime(student.getId(), selectedDate, selectedHour,
                        selectedMinute);
                if (response == null) return;
                if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                    MasterLogger.clientError(clientController.getId(), response.getErrorMessage(),
                            "connectListeners", getClass());
                } else {
                    MasterLogger.clientInfo(clientController.getId(), "Defense time has been successfully set",
                            "connectListeners", getClass());
                    updateDefenseTime();
                    setDefenseSlotLabels();
                }
            }
        });
    }

    @Override
    protected void updatePanel() {
    }
}
