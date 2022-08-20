package client.gui.menus.searching;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import client.locallogic.menus.messaging.ThumbnailIdParser;
import shareables.network.DTOs.generalmodels.StudentDTO;
import shareables.network.DTOs.messaging.ContactProfileDTO;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

import static shareables.network.responses.ResponseStatus.OK;

public class MrMohseniSearcher extends DynamicPanelTemplate {
    private JTextField studentIdField;
    private JButton infoButton;
    protected DefaultListModel<String> listModel;
    protected JList<String> graphicalList;
    protected JScrollPane scrollPane;
    private ArrayList<ContactProfileDTO> contactProfileDTOs;
    private String[] contactProfileTexts;
    private String selectedStudentId;

    public MrMohseniSearcher(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        configIdentifier = ConfigFileIdentifier.GUI_MR_MOHSENI_SEARCHER;
        selectedStudentId = "";
        contactProfileDTOs = new ArrayList<>();
        updateContactProfileDTOs();
        updateContactProfileTexts();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void updatePanel() {
        selectedStudentId = studentIdField.getText();
        updateContactProfileDTOs();
        String[] previousContactProfileTexts = Arrays.copyOf(contactProfileTexts, contactProfileTexts.length);
        updateContactProfileTexts();
        Arrays.stream(previousContactProfileTexts)
                .filter(e -> !arrayContains(contactProfileTexts, e))
                .forEach(e -> listModel.removeElement(e));
        Arrays.stream(contactProfileTexts)
                .filter(e -> !arrayContains(previousContactProfileTexts, e))
                .forEach(e -> listModel.addElement(e));
    }

    private boolean arrayContains(String[] array, String targetElement) {
        return Arrays.stream(array).anyMatch(e -> e.equals(targetElement));
    }

    private void updateContactProfileDTOs() {
        Response response;
        if (selectedStudentId.equals("")) {
            response = clientController.getAllStudentContactProfileDTOs();
        } else {
            response = clientController.getFilteredStudentContactProfileDTOs(selectedStudentId);
        }

        if (response == null) return;
        contactProfileDTOs = (ArrayList<ContactProfileDTO>) response.get("contactProfileDTOs");
    }

    private void updateContactProfileTexts() {
        contactProfileTexts = new String[contactProfileDTOs.size()];
        for (int i = 0; i < contactProfileDTOs.size(); i++) {
            contactProfileTexts[i] = contactProfileDTOs.get(i).toString();
        }
    }

    @Override
    protected void initializeComponents() {
        studentIdField = new JTextField();
        infoButton = new JButton(ConfigManager.getString(configIdentifier, "infoButtonM"));
        listModel = new DefaultListModel<>();
        Arrays.stream(contactProfileTexts).forEach(e -> listModel.addElement(e));
        graphicalList = new JList<>(listModel);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(graphicalList);
    }

    @Override
    protected void alignComponents() {
        studentIdField.setBounds(ConfigManager.getInt(configIdentifier, "studentIdFieldX"),
                ConfigManager.getInt(configIdentifier, "studentIdFieldY"),
                ConfigManager.getInt(configIdentifier, "studentIdFieldW"),
                ConfigManager.getInt(configIdentifier, "studentIdFieldH"));
        add(studentIdField);
        infoButton.setBounds(ConfigManager.getInt(configIdentifier, "infoButtonX"),
                ConfigManager.getInt(configIdentifier, "infoButtonY"),
                ConfigManager.getInt(configIdentifier, "infoButtonW"),
                ConfigManager.getInt(configIdentifier, "infoButtonH"));
        add(infoButton);

        graphicalList.setLayoutOrientation(JList.VERTICAL);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);
    }

    @Override
    protected void connectListeners() {
        infoButton.addActionListener(actionEvent -> {
            if (graphicalList.getSelectedIndex() == -1) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                        "noStudentHasBeenSelected");
                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners", getClass());
                return;
            }

            String selectedListItem = graphicalList.getSelectedValue();
            selectedStudentId = ThumbnailIdParser.getIdFromThumbnailText(selectedListItem, " - ");

            Response response = clientController.getStudentDTO(selectedStudentId);
            if (response == null) return;

            if (response.getResponseStatus() == OK) {
                StudentDTO studentDTO = (StudentDTO) response.get("studentDTO");
                if (studentDTO == null) return;

                stopPanelLoop();
                mainFrame.setCurrentPanel(new StudentProfileView(mainFrame, mainMenu, offlineModeDTO, studentDTO));
            }
        });
    }
}
