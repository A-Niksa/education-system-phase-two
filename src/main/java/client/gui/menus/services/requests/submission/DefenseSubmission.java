package client.gui.menus.services.requests.submission;

import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.main.MainMenu;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.students.Student;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.timing.formatting.DateLabelFormatter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Properties;

public class DefenseSubmission extends PanelTemplate {
    private Student student;
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

    public DefenseSubmission(MainFrame mainFrame, MainMenu mainMenu, User user) {
        super(mainFrame, mainMenu);
        student = (Student) user;
        configIdentifier = ConfigFileIdentifier.GUI_DEFENSE_SUBMISSION;
        drawPanel();
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

    private void setDatePicker() {
        String currentDate = TimeManager.getDate();
        int year = Integer.parseInt(currentDate.substring(0, 4));
        int month = Integer.parseInt(currentDate.substring(5, 7)) - 1;
        int day = Integer.parseInt(currentDate.substring(8, 10));
        dateModel.setDate(year, month, day);
        dateModel.setSelected(true);
    }

    @Override
    protected void alignComponents() {
        setDatePicker();
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
        if (student.getDefenseTime() != null) {
            defenseSlotPrompt.setText("Appointed Defense Slot:");
            String defenseTimeText = student.getDefenseTimeString();
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
                String selectedHour = hourPicker.getText();
                if (selectedHour.equals("Hour...")) {
                    selectedHour = "00";
                }
                String selectedMinute = minutePicker.getText();
                if (selectedMinute.equals("Minute...")) {
                    selectedMinute = "00";
                }

                DefenseRequest request = new DefenseRequest(student, selectedDate, selectedHour, selectedMinute);
                MasterLogger.info("submitted thesis defense time", getClass());

                LocalDateTime defenseTime = request.getLocalDateTimeOfDefense();
                if (defenseTime.compareTo(LocalDateTime.now()) < 0) {
                    MasterLogger.error("thesis time selected to be earlier than now", getClass());
                    JOptionPane.showMessageDialog(mainFrame,
                            "Invalid time. You cannot choose a time earlier than now.");
                    request.nullifyDefenseTime();
                    return;
                }

                MasterLogger.info("thesis defense time has been successfully set", getClass());
                setDefenseSlotLabels();
            }
        });
    }
}
