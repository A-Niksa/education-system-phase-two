package client.gui.menus.coursewares.educationalmaterials.display;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.coursewares.educationalmaterials.listview.ProfessorMaterialsView;
import client.gui.menus.coursewares.educationalmaterials.listview.StudentMaterialsView;
import client.gui.menus.main.MainMenu;
import client.locallogic.menus.messaging.DownloadManager;
import client.locallogic.menus.messaging.ThumbnailParser;
import shareables.models.pojos.coursewares.EducationalItem;
import shareables.models.pojos.media.MediaFile;
import shareables.models.pojos.users.UserIdentifier;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MaterialDisplay extends DynamicPanelTemplate {
    private String courseId;
    private String materialId;
    private JButton viewButton;
    private JButton goBackButton;
    private ArrayList<EducationalItem> educationalItems;
    private String[] educationalItemTexts;
    private DefaultListModel<String> listModel;
    private JList<String> graphicalList;
    private JScrollPane scrollPane;
    private DownloadManager downloadManager;

    public MaterialDisplay(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String courseId,
                           String materialId) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.courseId = courseId;
        this.materialId = materialId;
        configIdentifier = ConfigFileIdentifier.GUI_MATERIAL_DISPLAY;
        downloadManager = new DownloadManager();
        updateEducationalItems();
        updateEducationalItemTexts();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    private void updateEducationalItemTexts() {
        educationalItemTexts = new String[educationalItems.size()];
        for (int i = 0; i < educationalItems.size(); i++) {
            educationalItemTexts[i] = educationalItems.get(i).toExtensiveString();
        }
    }

    private void updateEducationalItems() {
        Response response = clientController.getCourseMaterialEducationalItems(courseId, materialId);
        if (response == null) return;
        educationalItems = (ArrayList<EducationalItem>) response.get("educationalItems");
    }

    @Override
    protected void updatePanel() {
        updateEducationalItems();
        String[] previousEducationalItemTexts = Arrays.copyOf(educationalItemTexts, educationalItemTexts.length);
        updateEducationalItemTexts();
        Arrays.stream(previousEducationalItemTexts)
                .filter(e -> !arrayContains(educationalItemTexts, e))
                .forEach(e -> listModel.removeElement(e));
        Arrays.stream(educationalItemTexts)
                .filter(e -> !arrayContains(previousEducationalItemTexts, e))
                .forEach(e -> listModel.add(0, e));
    }

    private boolean arrayContains(String[] array, String targetElement) {
        return Arrays.stream(array).anyMatch(e -> e.equals(targetElement));
    }

    @Override
    protected void initializeComponents() {
        listModel = new DefaultListModel<>();
        Arrays.stream(educationalItemTexts).forEach(e -> listModel.addElement(e));
        graphicalList = new JList<>(listModel);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(graphicalList);

        viewButton = new JButton(ConfigManager.getString(configIdentifier, "viewButtonM"));
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

        viewButton.setBounds(ConfigManager.getInt(configIdentifier, "viewButtonX"),
                ConfigManager.getInt(configIdentifier, "viewButtonY"),
                ConfigManager.getInt(configIdentifier, "viewButtonW"),
                ConfigManager.getInt(configIdentifier, "viewButtonH"));
        add(viewButton);

        goBackButton.setBounds(ConfigManager.getInt(configIdentifier, "goBackButtonX"),
                ConfigManager.getInt(configIdentifier, "goBackButtonY"),
                ConfigManager.getInt(configIdentifier, "goBackButtonW"),
                ConfigManager.getInt(configIdentifier, "goBackButtonH"));
        add(goBackButton);
    }

    @Override
    protected void connectListeners() {
        viewButton.addActionListener(actionEvent -> {
            int selectedIndex = graphicalList.getSelectedIndex();
            if (selectedIndex == -1) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                        "noItemHasBeenSelected");
                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners", getClass());
                return;

            }
            String selectedListItem = graphicalList.getSelectedValue();
            String itemTypeString = ThumbnailParser.getItemTypeString(selectedListItem, " - ");

            if (itemTypeString.equals("Text")) {
                showTextItem(selectedIndex);
            } else if (itemTypeString.equals("Media File")) {
                showMediaItemDownloadLocation(selectedIndex);
            }
        });

        goBackButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Went back to materials view",
                    "connectListeners", getClass());
            stopPanelLoop();
            if (offlineModeDTO.getUserIdentifier() == UserIdentifier.STUDENT) {
                mainFrame.setCurrentPanel(new StudentMaterialsView(mainFrame, mainMenu, offlineModeDTO, courseId));
            } else if (offlineModeDTO.getUserIdentifier() == UserIdentifier.PROFESSOR) {
                mainFrame.setCurrentPanel(new ProfessorMaterialsView(mainFrame, mainMenu, offlineModeDTO, courseId));
            }
        });
    }

    private void showMediaItemDownloadLocation(int selectedIndex) {
        EducationalItem educationalItem = educationalItems.get(selectedIndex);

        Response response = clientController.downloadMediaFromEducationalMaterial(courseId, materialId, educationalItem.getId());
        if (response == null) return;

        if (response.getResponseStatus() == ResponseStatus.OK) {
            String downloadedFilePrompt = downloadManager.saveMediaToLocalDownloads((MediaFile) response.get("mediaFile"));
            JOptionPane.showMessageDialog(mainFrame, downloadedFilePrompt);
            MasterLogger.clientInfo(clientController.getId(), downloadedFilePrompt, "connectListeners",
                    getClass());
        }
    }

    private void showTextItem(int selectedIndex) {
        EducationalItem educationalItem = educationalItems.get(selectedIndex);

        JTextArea textArea = new JTextArea(educationalItem.getText());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);

        JOptionPane.showMessageDialog(mainFrame, textArea);
    }


}
