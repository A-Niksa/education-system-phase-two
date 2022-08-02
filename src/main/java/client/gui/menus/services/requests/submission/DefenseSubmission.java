package client.gui.menus.services.requests.submission;

import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.main.MainMenu;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.students.Student;

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
        drawPanel();
    }

    @Override
    protected void initializeComponents() {
        dateModel = new UtilDateModel();
        properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");
        datePanel = new JDatePanelImpl(dateModel, properties);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        hourPicker = new JTextField("Hour...");
        minutePicker = new JTextField("Minute...");
        setDefenseSlot = new JButton("Set Defense Slot");
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
        datePicker.setBounds(100, 230, 300, 30);
        add(datePicker);

        hourPicker.setBounds(100, 278, 145, 30);
        add(hourPicker);
        minutePicker.setBounds(255, 278, 145, 30);
        add(minutePicker);
        setDefenseSlot.setBounds(100, 325, 300, 35);
        add(setDefenseSlot);

        separator.setOrientation(SwingConstants.VERTICAL);
        separator.setBackground(Color.BLACK);
        separator.setForeground(Color.BLACK);
        separator.setBounds(490, 175, 15, 250);
        add(separator);

        defenseSlotPrompt.setBounds(505, 175, 300, 40);
        add(defenseSlotPrompt);
        defenseSlotInformation.setBounds(612, 280, 350, 60);
        defenseSlotInformation.setFont(new Font("", Font.BOLD, 19));
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
