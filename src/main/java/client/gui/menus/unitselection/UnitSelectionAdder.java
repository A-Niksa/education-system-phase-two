package client.gui.menus.unitselection;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import client.gui.utils.EnumArrayUtils;
import client.gui.utils.ErrorUtils;
import client.locallogic.menus.addition.BlueprintGenerator;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.blueprints.Blueprint;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;
import shareables.utils.timing.formatting.DateLabelFormatter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class UnitSelectionAdder extends DynamicPanelTemplate {
    private JButton scheduleUnitSelectionUnitButton;
    private JTextField yearOfEntryField;
    private String[] degreeLevels;
    private JComboBox<String> degreeLevelsBox;
    private JLabel startsAtLabel;
    private JTextField startsAtHour;
    private JTextField startsAtMinute;
    private JLabel endsAtLabel;
    private JTextField endsAtHour;
    private JTextField endsAtMinute;
    private Properties datePickingProperties;
    private UtilDateModel startsAtDateModel;
    private JDatePanelImpl startsAtDatePanel;
    private JDatePickerImpl startsAtDatePicker;
    private UtilDateModel endsAtDateModel;
    private JDatePanelImpl endsAtDatePanel;
    private JDatePickerImpl endsAtDatePicker;
    private ArrayList<JComponent> panelComponents;

    public UnitSelectionAdder(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        configIdentifier = ConfigFileIdentifier.GUI_UNIT_SELECTION_ADDER;
        degreeLevels = EnumArrayUtils.initializeDegreeLevels();
        panelComponents = new ArrayList<>();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void updatePanel() {
    }

    @Override
    protected void initializeComponents() {
        yearOfEntryField = new JTextField(ConfigManager.getString(configIdentifier, "yearOfEntryFieldM"));
        degreeLevelsBox = new JComboBox<>(degreeLevels);
        panelComponents.add(yearOfEntryField);
        panelComponents.add(degreeLevelsBox);


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


        scheduleUnitSelectionUnitButton = new JButton(ConfigManager.getString(configIdentifier,
                "scheduleUnitSelectionUnitButtonM"));
    }

    @Override
    protected void alignComponents() {
        int currentX = ConfigManager.getInt(configIdentifier, "startingX");
        int currentY = ConfigManager.getInt(configIdentifier, "startingY");
        int incrementOfY = ConfigManager.getInt(configIdentifier, "incY");
        int smallerIncrementOfY = ConfigManager.getInt(configIdentifier, "smallerIncY");
        int componentWidth = ConfigManager.getInt(configIdentifier, "componentW");
        int componentHeight = ConfigManager.getInt(configIdentifier, "componentH");

        for (JComponent component : panelComponents) {
            component.setBounds(currentX, currentY, componentWidth, componentHeight);
            add(component);
            currentY += incrementOfY;
        }

        currentY += smallerIncrementOfY;
        scheduleUnitSelectionUnitButton.setBounds(currentX, currentY, componentWidth, componentHeight);
        add(scheduleUnitSelectionUnitButton);
    }

    @Override
    protected void connectListeners() {
        scheduleUnitSelectionUnitButton.addActionListener(actionEvent -> {
            int yearOfEntry = Integer.parseInt(yearOfEntryField.getText());
            String degreeLevelString = (String) degreeLevelsBox.getSelectedItem();
            int startingHour = Integer.parseInt(startsAtHour.getText());
            int startingMinute = Integer.parseInt(startsAtMinute.getText());
            Date startingDate = startsAtDateModel.getValue();
            int endingHour = Integer.parseInt(endsAtHour.getText());
            int endingMinute = Integer.parseInt(endsAtMinute.getText());
            Date endingDate = endsAtDateModel.getValue();

            Blueprint unitSelectionBlueprint = BlueprintGenerator.generateUnitSelectionBlueprint(yearOfEntry,
                    degreeLevelString, startingHour, startingMinute, startingDate, endingHour, endingMinute, endingDate,
                    offlineModeDTO.getDepartmentId());
            Response response = clientController.addUnitSelectionSession(unitSelectionBlueprint);
            if (response == null) return;

            if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                MasterLogger.clientError(clientController.getId(), response.getErrorMessage(),
                        "connectListeners", getClass());
            } else {
                JOptionPane.showMessageDialog(mainFrame, response.getUnsolicitedMessage());
                MasterLogger.clientError(clientController.getId(), response.getUnsolicitedMessage(),
                        "connectListeners", getClass());
            }
        });
    }
}
