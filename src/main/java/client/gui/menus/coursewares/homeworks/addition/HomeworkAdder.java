package client.gui.menus.coursewares.homeworks.addition;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import client.gui.utils.EnumArrayUtils;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.timing.formatting.DateLabelFormatter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Properties;

public class HomeworkAdder extends DynamicPanelTemplate {
    private String courseId;
    private String homeworkTitle;
    private String[] submissionTypes;
    private JComboBox<String> submissionTypesBox;
    private JLabel startsAtLabel;
    private JTextField startsAtHour;
    private JTextField startsAtMinute;
    private JLabel endsAtLabel;
    private JTextField endsAtHour;
    private JTextField endsAtMinute;
    private JLabel sharplyEndsAtLabel;
    private JTextField sharplyEndsAtHour;
    private JTextField sharplyEndsAtMinute;
    private Properties datePickingProperties;
    private UtilDateModel startsAtDateModel;
    private JDatePanelImpl startsAtDatePanel;
    private JDatePickerImpl startsAtDatePicker;
    private UtilDateModel endsAtDateModel;
    private JDatePanelImpl endsAtDatePanel;
    private JDatePickerImpl endsAtDatePicker;
    private UtilDateModel sharplyEndsAtDateModel;
    private JDatePanelImpl sharplyEndsAtDatePanel;
    private JDatePickerImpl sharplyEndsAtDatePicker;
    private JTextArea descriptionTextArea;
    private JButton choosePDFFileButton;
    private JButton goBackButton;
    private ArrayList<JComponent> panelComponents;

    public HomeworkAdder(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String courseId,
                         String homeworkTitle) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.courseId = courseId;
        this.homeworkTitle = homeworkTitle;
        configIdentifier = ConfigFileIdentifier.GUI_HOMEWORK_ADDER;
        submissionTypes = EnumArrayUtils.initializeSubmissionTypes();
        panelComponents = new ArrayList<>();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void updatePanel() {
        submissionTypesBox = new JComboBox<>(submissionTypes);
        panelComponents.add(submissionTypesBox);


        datePickingProperties = new Properties();
        datePickingProperties.put("text.today", ConfigManager.getString(configIdentifier, "propertiesTextTodayM"));
        datePickingProperties.put("text.month", ConfigManager.getString(configIdentifier, "propertiesTextMonthM"));
        datePickingProperties.put("text.year", ConfigManager.getString(configIdentifier, "propertiesTextYearM"));


        startsAtLabel = new JLabel(ConfigManager.getString(configIdentifier, "startsAtLabelM"));
        startsAtHour = new JTextField(ConfigManager.getString(configIdentifier, "hourM"));
        startsAtMinute = new JTextField(ConfigManager.getString(configIdentifier, "minuteM"));
        panelComponents.add(startsAtLabel);
        panelComponents.add(startsAtHour);
        panelComponents.add(startsAtMinute);

        startsAtDateModel = new UtilDateModel();
        startsAtDatePanel = new JDatePanelImpl(startsAtDateModel, datePickingProperties);
        startsAtDatePicker = new JDatePickerImpl(startsAtDatePanel, new DateLabelFormatter());
        panelComponents.add(startsAtDatePicker);


        endsAtLabel = new JLabel(ConfigManager.getString(configIdentifier, "endsAtLabelM"));
        endsAtHour = new JTextField(ConfigManager.getString(configIdentifier, "hourM"));
        endsAtMinute = new JTextField(ConfigManager.getString(configIdentifier, "minuteM"));
        panelComponents.add(endsAtLabel);
        panelComponents.add(endsAtHour);
        panelComponents.add(endsAtMinute);

        endsAtDateModel = new UtilDateModel();
        endsAtDatePanel = new JDatePanelImpl(endsAtDateModel, datePickingProperties);
        endsAtDatePicker = new JDatePickerImpl(endsAtDatePanel, new DateLabelFormatter());
        panelComponents.add(endsAtDatePicker);

        sharplyEndsAtLabel = new JLabel(ConfigManager.getString(configIdentifier, "sharplyEndsAtLabelM"));
        sharplyEndsAtHour = new JTextField(ConfigManager.getString(configIdentifier, "hourM"));
        sharplyEndsAtMinute = new JTextField(ConfigManager.getString(configIdentifier, "minuteM"));
        panelComponents.add(sharplyEndsAtLabel);
        panelComponents.add(sharplyEndsAtHour);
        panelComponents.add(sharplyEndsAtMinute);

        sharplyEndsAtDateModel = new UtilDateModel();
        sharplyEndsAtDatePanel = new JDatePanelImpl(sharplyEndsAtDateModel, datePickingProperties);
        sharplyEndsAtDatePicker = new JDatePickerImpl(sharplyEndsAtDatePanel, new DateLabelFormatter());
        panelComponents.add(sharplyEndsAtDatePicker);


    }

    @Override
    protected void initializeComponents() {

    }

    @Override
    protected void alignComponents() {

    }

    @Override
    protected void connectListeners() {

    }
}
