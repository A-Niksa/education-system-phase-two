package client.gui.menus.coursewares.homeworks.addition;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.coursewares.homeworks.listview.ProfessorHomeworksView;
import client.gui.menus.main.MainMenu;
import client.gui.utils.EnumArrayUtils;
import client.locallogic.menus.addition.BlueprintGenerator;
import client.locallogic.menus.messaging.MediaFileParser;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import shareables.models.pojos.media.MediaFile;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.blueprints.Blueprint;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;
import shareables.utils.timing.formatting.DateLabelFormatter;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
    private JButton saveButton;
    private JButton goBackButton;
    private MediaFile chosenMediaFile;
    private ArrayList<JComponent> panelComponents;
    private MediaFileParser mediaFileParser;

    public HomeworkAdder(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String courseId,
                         String homeworkTitle) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.courseId = courseId;
        this.homeworkTitle = homeworkTitle;
        mediaFileParser = new MediaFileParser();
        configIdentifier = ConfigFileIdentifier.GUI_HOMEWORK_ADDER;
        submissionTypes = EnumArrayUtils.initializeSubmissionTypes();
        panelComponents = new ArrayList<>();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void updatePanel() {

    }

    @Override
    protected void initializeComponents() {
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

        descriptionTextArea = new JTextArea(ConfigManager.getString(configIdentifier, "descriptionTextAreaM"));

        choosePDFFileButton = new JButton(ConfigManager.getString(configIdentifier, "choosePDFFileButtonM"));
        saveButton = new JButton(ConfigManager.getString(configIdentifier, "saveButtonM"));

        goBackButton = new JButton(ConfigManager.getString(configIdentifier, "goBackButtonM"));
    }

    @Override
    protected void alignComponents() {
        int currentX = ConfigManager.getInt(configIdentifier, "startingX");
        int currentY = ConfigManager.getInt(configIdentifier, "startingY");
        int incrementOfY = ConfigManager.getInt(configIdentifier, "incY");
        int componentWidth = ConfigManager.getInt(configIdentifier, "componentW");
        int componentHeight = ConfigManager.getInt(configIdentifier, "componentH");

        for (JComponent component : panelComponents) {
            component.setBounds(currentX, currentY, componentWidth, componentHeight);
            add(component);
            currentY += incrementOfY;
        }

        descriptionTextArea.setBounds(ConfigManager.getInt(configIdentifier, "descriptionTextAreaX"),
                ConfigManager.getInt(configIdentifier, "descriptionTextAreaY"),
                ConfigManager.getInt(configIdentifier, "descriptionTextAreaW"),
                ConfigManager.getInt(configIdentifier, "descriptionTextAreaH"));
        add(descriptionTextArea);

        choosePDFFileButton.setBounds(ConfigManager.getInt(configIdentifier, "choosePDFFileButtonX"),
                ConfigManager.getInt(configIdentifier, "choosePDFFileButtonY"),
                ConfigManager.getInt(configIdentifier, "choosePDFFileButtonW"),
                ConfigManager.getInt(configIdentifier, "choosePDFFileButtonH"));
        add(choosePDFFileButton);
        saveButton.setBounds(ConfigManager.getInt(configIdentifier, "saveButtonX"),
                ConfigManager.getInt(configIdentifier, "saveButtonY"),
                ConfigManager.getInt(configIdentifier, "saveButtonW"),
                ConfigManager.getInt(configIdentifier, "saveButtonH"));
        add(saveButton);
        goBackButton.setBounds(ConfigManager.getInt(configIdentifier, "goBackButtonX"),
                ConfigManager.getInt(configIdentifier, "goBackButtonY"),
                ConfigManager.getInt(configIdentifier, "goBackButtonW"),
                ConfigManager.getInt(configIdentifier, "goBackButtonH"));
        add(goBackButton);
    }

    @Override
    protected void connectListeners() {
        choosePDFFileButton.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));

            int returnedValue = fileChooser.showOpenDialog(mainFrame);
            File chosenFile = null;
            if (returnedValue == JFileChooser.APPROVE_OPTION) {
                chosenFile = fileChooser.getSelectedFile();
                MasterLogger.clientInfo(clientController.getId(), "Media file chosen (Path: " + chosenFile.getPath()
                        + ")", "connectListeners", getClass());
            }

            MediaFile convertedMediaFile = getMediaFile(chosenFile);
            if (convertedMediaFile == null) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS, "fileNotSupported");

                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners",
                        getClass());

                return;
            }

            chosenMediaFile = convertedMediaFile;
        });

        saveButton.addActionListener(actionEvent -> {
            String submissionTypeString = (String) submissionTypesBox.getSelectedItem();
            int startingHour = Integer.parseInt(startsAtHour.getText());
            int startingMinute = Integer.parseInt(startsAtMinute.getText());
            Date startingDate = startsAtDateModel.getValue();
            int endingHour = Integer.parseInt(endsAtHour.getText());
            int endingMinute = Integer.parseInt(endsAtMinute.getText());
            Date endingDate = endsAtDateModel.getValue();
            int sharpEndingHour = Integer.parseInt(sharplyEndsAtHour.getText());
            int sharpEndingMinute = Integer.parseInt(sharplyEndsAtMinute.getText());
            Date sharpEndingDate = sharplyEndsAtDateModel.getValue();
            String description = descriptionTextArea.getText();

            Blueprint homeworkBlueprint = BlueprintGenerator.generateHomeworkBlueprint(submissionTypeString, startingHour,
                    startingMinute, startingDate, endingHour, endingMinute, endingDate, sharpEndingHour, sharpEndingMinute,
                    sharpEndingDate, chosenMediaFile, description, courseId, homeworkTitle);

            Response response = clientController.saveHomework(homeworkBlueprint);
            if (response == null) return;

            if (response.getResponseStatus() == ResponseStatus.OK) {
                MasterLogger.clientInfo(clientController.getId(), "Added homework",
                        "connectListeners", getClass());
            }
        });

        goBackButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Went back to homeworks listview",
                    "connectListeners", getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(new ProfessorHomeworksView(mainFrame, mainMenu, offlineModeDTO, courseId));
        });
    }

    private MediaFile getMediaFile(File chosenFile) {
        if (chosenFile == null) {
            String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS, "noFileHasBeenChosen");

            JOptionPane.showMessageDialog(mainFrame, errorMessage);
            MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners",
                    getClass());

            return null;
        }

        if (mediaFileParser.isFileTooLarge(chosenFile)) {
            String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS, "fileTooLarge");

            JOptionPane.showMessageDialog(mainFrame, errorMessage);
            MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners",
                    getClass());

            return null;
        }

        return mediaFileParser.convertFileToPDFMediaFile(chosenFile);
    }
}