package client.gui.menus.coursewares.educationalmaterials.addition;

import client.gui.MainFrame;
import client.gui.menus.coursewares.educationalmaterials.listview.ProfessorMaterialsView;
import client.gui.menus.coursewares.educationalmaterials.listview.StudentMaterialsView;
import client.gui.menus.main.MainMenu;
import shareables.models.pojos.coursewares.EducationalItem;
import shareables.models.pojos.users.UserIdentifier;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.util.ArrayList;

public class MaterialEditor extends MaterialAdder {
    private String materialId;
    private JButton removeItemButton;
    private boolean isTeachingAssistant;

    public MaterialEditor(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO, String courseId,
                          String materialId) {
        super(mainFrame, mainMenu, offlineModeDTO, courseId, "");
        this.materialId = materialId;
        initializeTeachingAssistanceStatus();
        initializeItemsList();
        addRemovalFeature();
        connectNewSaveListener();
        connectNewBackListenerIfNecessary();
    }

    private void initializeTeachingAssistanceStatus() {
        if (offlineModeDTO.getUserIdentifier() == UserIdentifier.PROFESSOR) {
            isTeachingAssistant = false;
        } else {
            Response response = clientController.getTeachingAssistanceStatus(courseId, offlineModeDTO.getId());
            if (response == null) return;
            isTeachingAssistant = (boolean) response.get("isTeachingAssistant");
        }
    }

    private void connectNewSaveListener() {
        saveButton.removeActionListener(saveButton.getActionListeners()[0]);

        saveButton.addActionListener(actionEvent -> {
            Response response = clientController.editEducationalMaterialItems(courseId, materialId, educationalItems);
            if (response == null) return;

            if (response.getResponseStatus() == ResponseStatus.OK) {
                MasterLogger.clientInfo(clientController.getId(), "Edited educational material",
                        "connectListeners", getClass());
            }
        });
    }



    private void connectNewBackListenerIfNecessary() {
        if (!isTeachingAssistant) return;

        goBackButton.removeActionListener(goBackButton.getActionListeners()[0]);

        goBackButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Went back to materials listview",
                    "connectListeners", getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(new StudentMaterialsView(mainFrame, mainMenu, offlineModeDTO, courseId));
        });
    }

    private void initializeItemsList() {
        updateEducationalItems();
        educationalItems.forEach(e -> listModel.addElement(e.toString()));
    }

    private void updateEducationalItems() {
        Response response = clientController.getCourseMaterialEducationalItems(courseId, materialId);
        if (response == null) return;
        educationalItems = (ArrayList<EducationalItem>) response.get("educationalItems");
    }

    private void addRemovalFeature() {
        initializeRemoveButton();
        alignRemoveButton();
        connectRemovalListener();
    }

    private void initializeRemoveButton() {
        removeItemButton = new JButton(ConfigManager.getString(configIdentifier, "removeItemButtonM"));
    }

    private void alignRemoveButton() {
        removeItemButton.setBounds(ConfigManager.getInt(configIdentifier, "removeItemButtonX"),
                ConfigManager.getInt(configIdentifier, "removeItemButtonY"),
                ConfigManager.getInt(configIdentifier, "removeItemButtonW"),
                ConfigManager.getInt(configIdentifier, "removeItemButtonH"));
        add(removeItemButton);
    }

    private void connectRemovalListener() {
        removeItemButton.addActionListener(actionEvent -> {
            int selectedIndex = graphicalList.getSelectedIndex();
            if (selectedIndex == -1) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                        "noItemHasBeenSelected");
                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners", getClass());
                return;
            }

            listModel.remove(selectedIndex);
            educationalItems.remove(selectedIndex);
        });
    }
}
