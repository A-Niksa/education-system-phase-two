package client.gui.menus.coursewares.educationalmaterials.listview;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.coursewares.coursemenu.CourseMenu;
import client.gui.menus.coursewares.educationalmaterials.display.MaterialDisplay;
import client.gui.menus.main.MainMenu;
import client.locallogic.menus.messaging.ThumbnailParser;
import shareables.network.DTOs.coursewares.MaterialThumbnailDTO;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class MaterialsView extends DynamicPanelTemplate {
    protected String courseId;
    protected JButton openButton;
    protected DefaultListModel<String> listModel;
    protected JList<String> graphicalList;
    protected JScrollPane scrollPane;
    protected String[] materialThumbnailTexts;
    protected ArrayList<MaterialThumbnailDTO> materialThumbnailDTOs;
    protected JButton removeAllButton;
    protected JButton removeButton;
    protected JButton addButton;
    protected JButton editButton;
    protected JButton goBackButton;

    public MaterialsView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String courseId) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.courseId = courseId;
        configIdentifier = ConfigFileIdentifier.GUI_MATERIALS_VIEW;
        updateMaterialThumbnailDTOs();
        updateMaterialThumbnailTexts();
        drawPanel();
    }

    private void updateMaterialThumbnailTexts() {
        if (materialThumbnailDTOs == null) return;

        materialThumbnailTexts = new String[materialThumbnailDTOs.size()];
        for (int i = 0; i < materialThumbnailDTOs.size(); i++) {
            materialThumbnailTexts[i] = materialThumbnailDTOs.get(i).toString();
        }
    }

    private void updateMaterialThumbnailDTOs() {
        Response response = clientController.getMaterialThumbnailDTOs(courseId);
        if (response == null) return;
        materialThumbnailDTOs = (ArrayList<MaterialThumbnailDTO>) response.get("materialThumbnailDTOs");
    }

    @Override
    protected void updatePanel() {
        updateMaterialThumbnailDTOs();
        String[] previousMaterialThumbnailTexts = Arrays.copyOf(materialThumbnailTexts, materialThumbnailTexts.length);
        updateMaterialThumbnailTexts();
        Arrays.stream(previousMaterialThumbnailTexts)
                .filter(e -> !arrayContains(materialThumbnailTexts, e))
                .forEach(e -> listModel.removeElement(e));
        Arrays.stream(materialThumbnailTexts)
                .filter(e -> !arrayContains(previousMaterialThumbnailTexts, e))
                .forEach(e -> listModel.add(0, e));
    }

    private boolean arrayContains(String[] array, String targetElement) {
        return Arrays.stream(array).anyMatch(e -> e.equals(targetElement));
    }

    @Override
    protected void initializeComponents() {
        openButton = new JButton(ConfigManager.getString(configIdentifier, "openButtonM"));

        listModel = new DefaultListModel<>();
        Arrays.stream(materialThumbnailTexts).forEach(e -> listModel.addElement(e));
        graphicalList = new JList<>(listModel);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(graphicalList);

        removeAllButton = new JButton(ConfigManager.getString(configIdentifier, "removeAllButtonM"));
        removeButton = new JButton(ConfigManager.getString(configIdentifier, "removeButtonM"));
        addButton = new JButton(ConfigManager.getString(configIdentifier, "addButtonM"));
        editButton = new JButton(ConfigManager.getString(configIdentifier, "editButtonM"));
        goBackButton = new JButton(ConfigManager.getString(configIdentifier, "goBackButtonM"));
    }

    @Override
    protected void alignComponents() {
        openButton.setBounds(ConfigManager.getInt(configIdentifier, "openButtonX"),
                ConfigManager.getInt(configIdentifier, "openButtonY"),
                ConfigManager.getInt(configIdentifier, "openButtonW"),
                ConfigManager.getInt(configIdentifier, "openButtonH"));
        add(openButton);

        graphicalList.setLayoutOrientation(JList.VERTICAL);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);

        removeAllButton.setBounds(ConfigManager.getInt(configIdentifier, "removeAllButtonX"),
                ConfigManager.getInt(configIdentifier, "removeAllButtonY"),
                ConfigManager.getInt(configIdentifier, "removeAllButtonW"),
                ConfigManager.getInt(configIdentifier, "removeAllButtonH"));
        removeButton.setBounds(ConfigManager.getInt(configIdentifier, "removeButtonX"),
                ConfigManager.getInt(configIdentifier, "removeButtonY"),
                ConfigManager.getInt(configIdentifier, "removeButtonW"),
                ConfigManager.getInt(configIdentifier, "removeButtonH"));
        addButton.setBounds(ConfigManager.getInt(configIdentifier, "addButtonX"),
                ConfigManager.getInt(configIdentifier, "addButtonY"),
                ConfigManager.getInt(configIdentifier, "addButtonW"),
                ConfigManager.getInt(configIdentifier, "addButtonH"));
        editButton.setBounds(ConfigManager.getInt(configIdentifier, "editButtonX"),
                ConfigManager.getInt(configIdentifier, "editButtonY"),
                ConfigManager.getInt(configIdentifier, "editButtonW"),
                ConfigManager.getInt(configIdentifier, "editButtonH"));
        goBackButton.setBounds(ConfigManager.getInt(configIdentifier, "goBackButtonX"),
                ConfigManager.getInt(configIdentifier, "goBackButtonY"),
                ConfigManager.getInt(configIdentifier, "goBackButtonW"),
                ConfigManager.getInt(configIdentifier, "goBackButtonH"));
        add(goBackButton);
    }

    @Override
    protected void connectListeners() {
        openButton.addActionListener(actionEvent -> {
            if (graphicalList.getSelectedIndex() == -1) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                        "noMaterialHasBeenSelected");
                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners", getClass());
                return;
            }

            String selectedListItem = graphicalList.getSelectedValue();
            String selectedMaterialId = ThumbnailParser.getIdFromThumbnailText(selectedListItem, " - ");

            MasterLogger.clientInfo(clientController.getId(), "Opened material display",
                    "connectListeners", getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(new MaterialDisplay(mainFrame, mainMenu, offlineModeDTO, courseId, selectedMaterialId));
        });

        goBackButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Went back to course menu",
                    "connectListeners", getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(new CourseMenu(mainFrame, mainMenu, offlineModeDTO, courseId));
        });
    }
}
