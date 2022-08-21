package client.gui.menus.coursewares.homeworks.listview;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.coursewares.coursemenu.CourseMenu;
import client.gui.menus.main.MainMenu;
import shareables.network.DTOs.coursewares.HomeworkThumbnailDTO;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class HomeworksView extends DynamicPanelTemplate {
    protected String courseId;
    protected JButton addButton;
    protected JButton openButton;
    protected DefaultListModel<String> listModel;
    protected JList<String> graphicalList;
    protected JScrollPane scrollPane;
    protected String[] homeworkThumbnailTexts;
    protected ArrayList<HomeworkThumbnailDTO> homeworkThumbnailDTOs;
    protected JButton goBackButton;

    public HomeworksView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String courseId) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.courseId = courseId;
        configIdentifier = ConfigFileIdentifier.GUI_HOMEWORKS_VIEW;
        updateHomeworkThumbnailDTOs();
        updateHomeworkThumbnailTexts();
        drawPanel();
        connectBackListener();
    }

    private void updateHomeworkThumbnailTexts() {
        homeworkThumbnailTexts = new String[homeworkThumbnailDTOs.size()];
        for (int i = 0; i < homeworkThumbnailDTOs.size(); i++) {
            homeworkThumbnailTexts[i] = homeworkThumbnailDTOs.get(i).toString();
        }
    }

    private void updateHomeworkThumbnailDTOs() {
        Response response = clientController.getHomeworkThumbnailDTOs(courseId);
        if (response == null) return;
        homeworkThumbnailDTOs = (ArrayList<HomeworkThumbnailDTO>) response.get("homeworkThumbnailDTOs");
    }
    @Override
    protected void updatePanel() {
        updateHomeworkThumbnailDTOs();
        String[] previousHomeworkThumbnailTexts = Arrays.copyOf(homeworkThumbnailTexts, homeworkThumbnailTexts.length);
        updateHomeworkThumbnailTexts();
        Arrays.stream(previousHomeworkThumbnailTexts)
                .filter(e -> !arrayContains(homeworkThumbnailTexts, e))
                .forEach(e -> listModel.removeElement(e));
        Arrays.stream(homeworkThumbnailTexts)
                .filter(e -> !arrayContains(previousHomeworkThumbnailTexts, e))
                .forEach(e -> listModel.add(0, e));
    }

    private boolean arrayContains(String[] array, String targetElement) {
        return Arrays.stream(array).anyMatch(e -> e.equals(targetElement));
    }

    @Override
    protected void initializeComponents() {
        openButton = new JButton(ConfigManager.getString(configIdentifier, "openButtonM"));

        listModel = new DefaultListModel<>();
        Arrays.stream(homeworkThumbnailTexts).forEach(e -> listModel.addElement(e));
        graphicalList = new JList<>(listModel);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(graphicalList);

        addButton = new JButton(ConfigManager.getString(configIdentifier, "addButtonM"));
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

        addButton.setBounds(ConfigManager.getInt(configIdentifier, "addButtonX"),
                ConfigManager.getInt(configIdentifier, "addButtonY"),
                ConfigManager.getInt(configIdentifier, "addButtonW"),
                ConfigManager.getInt(configIdentifier, "addButtonH"));
        goBackButton.setBounds(ConfigManager.getInt(configIdentifier, "goBackButtonX"),
                ConfigManager.getInt(configIdentifier, "goBackButtonY"),
                ConfigManager.getInt(configIdentifier, "goBackButtonW"),
                ConfigManager.getInt(configIdentifier, "goBackButtonH"));
        add(goBackButton);
    }

    private void connectBackListener() {
        goBackButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Went back to course menu",
                    "connectListeners", getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(new CourseMenu(mainFrame, mainMenu, offlineModeDTO, courseId));
        });
    }
}
