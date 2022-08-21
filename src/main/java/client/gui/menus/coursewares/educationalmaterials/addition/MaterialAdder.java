package client.gui.menus.coursewares.educationalmaterials.addition;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.coursewares.educationalmaterials.view.ProfessorMaterialsView;
import client.gui.menus.main.MainMenu;
import client.locallogic.menus.coursewares.ItemGenerator;
import client.locallogic.menus.messaging.MediaFileParser;
import shareables.models.pojos.coursewares.EducationalItem;
import shareables.models.pojos.media.MediaFile;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class MaterialAdder extends DynamicPanelTemplate {
    protected String courseId;
    private String materialTitle;
    protected ArrayList<EducationalItem> educationalItems;
    protected DefaultListModel<String> listModel;
    protected JList<String> graphicalList;
    private JScrollPane scrollPane;
    private JButton addTextItemButton;
    private JButton addMediaItemButton;
    protected JButton saveButton;
    private JButton goBackButton;
    private ItemGenerator itemGenerator;
    private MediaFileParser mediaFileParser;

    public MaterialAdder(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String courseId,
                         String materialTitle) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.courseId = courseId;
        this.materialTitle = materialTitle;
        configIdentifier = ConfigFileIdentifier.GUI_MATERIAL_ADDER;
        itemGenerator = new ItemGenerator();
        mediaFileParser = new MediaFileParser();
        educationalItems = new ArrayList<>();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void updatePanel() {
    }

    private void updateGraphicalList() {
        EducationalItem lastEducationalItem = educationalItems.get(educationalItems.size() - 1);
        listModel.addElement(lastEducationalItem.toString());
    }

    @Override
    protected void initializeComponents() {
        listModel = new DefaultListModel<>();
        graphicalList = new JList<>(listModel);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(graphicalList);

        addTextItemButton = new JButton(ConfigManager.getString(configIdentifier, "addTextItemButtonM"));
        addMediaItemButton = new JButton(ConfigManager.getString(configIdentifier, "addMediaItemButtonM"));
        saveButton = new JButton(ConfigManager.getString(configIdentifier, "saveButtonM"));
        goBackButton = new JButton(ConfigManager.getString(configIdentifier, "goBackButtonM"));
    }

    @Override
    protected void alignComponents() {
        graphicalList.setLayoutOrientation(JList.VERTICAL);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);

        addTextItemButton.setBounds(ConfigManager.getInt(configIdentifier, "addTextItemButtonX"),
                ConfigManager.getInt(configIdentifier, "addTextItemButtonY"),
                ConfigManager.getInt(configIdentifier, "addTextItemButtonW"),
                ConfigManager.getInt(configIdentifier, "addTextItemButtonH"));
        add(addTextItemButton);
        addMediaItemButton.setBounds(ConfigManager.getInt(configIdentifier, "addMediaItemButtonX"),
                ConfigManager.getInt(configIdentifier, "addMediaItemButtonY"),
                ConfigManager.getInt(configIdentifier, "addMediaItemButtonW"),
                ConfigManager.getInt(configIdentifier, "addMediaItemButtonH"));
        add(addMediaItemButton);
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
        addTextItemButton.addActionListener(actionEvent -> {
            int maxNumberOfItems = ConfigManager.getInt(ConfigFileIdentifier.CONSTANTS, "maxNumberOfItems");

            if (educationalItems.size() >= maxNumberOfItems) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                        "exceededMaxNumberOfItems");
                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners", getClass());
                return;
            }

            String text = JOptionPane.showInputDialog(mainFrame, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                    "textItemPrompt"));
            if (text == null) return;

            EducationalItem educationalItem = itemGenerator.generateTextItem(text);
            educationalItems.add(educationalItem);

            updateGraphicalList();
        });

        addMediaItemButton.addActionListener(actionEvent -> {
            int maxNumberOfItems = ConfigManager.getInt(ConfigFileIdentifier.CONSTANTS, "maxNumberOfItems");

            if (educationalItems.size() >= maxNumberOfItems) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                        "exceededMaxNumberOfItems");
                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners", getClass());
                return;
            }

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

            EducationalItem educationalItem = itemGenerator.generateMediaItem(convertedMediaFile);
            educationalItems.add(educationalItem);

            updateGraphicalList();
        });

        saveButton.addActionListener(actionEvent -> {
            Response response = clientController.saveEducationalMaterialItems(courseId, materialTitle, educationalItems);
            if (response == null) return;

            if (response.getResponseStatus() == ResponseStatus.OK) {
                MasterLogger.clientInfo(clientController.getId(), "Added educational material",
                        "connectListeners", getClass());
            }
        });

        goBackButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Went back to materials view",
                    "connectListeners", getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(new ProfessorMaterialsView(mainFrame, mainMenu, offlineModeDTO, courseId));
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

        if (mediaFileParser.isFileTooLargeInReducedWay(chosenFile)) {
            String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS, "fileTooLarge");

            JOptionPane.showMessageDialog(mainFrame, errorMessage);
            MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners",
                    getClass());

            return null;
        }

        return mediaFileParser.convertFileToMediaFile(chosenFile);
    }
}