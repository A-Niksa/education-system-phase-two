package client.gui.menus.messaging.conversationstarters;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import client.gui.menus.messaging.messengerviews.StudentMessengerView;
import client.gui.utils.ErrorUtils;
import client.locallogic.messaging.ContactManager;
import shareables.models.pojos.users.UserIdentifier;
import shareables.network.DTOs.messaging.ContactProfileDTO;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public abstract class ConversationStarter extends DynamicPanelTemplate {
    private JButton startConversationWithEveryoneButton;
    private JButton startConversationButton;
    private JButton goBackButton;
    private DefaultTableModel tableModel;
    private JTable contactsTable;
    private JScrollPane scrollPane;
    private String[][] data;
    private String[] columns;
    protected ArrayList<ContactProfileDTO> contactProfileDTOs;
    private String[] contactProfileTexts;
    private ContactManager contactManager;

    public ConversationStarter(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        configIdentifier = ConfigFileIdentifier.GUI_CONVERSATION_STARTER;
        contactProfileDTOs = new ArrayList<>();
        contactManager = new ContactManager();
        updateContactProfileDTOs();
        updateContactProfileTexts();
        initializeColumns();
        setTableData();
        drawPanel();
    }

    private void initializeColumns() {
        columns = new String[3];
        columns[0] = ConfigManager.getString(configIdentifier, "idCol");
        columns[1] = ConfigManager.getString(configIdentifier, "nameCol");
        columns[2] = ConfigManager.getString(configIdentifier, "descriptionCol");
    }

    private void setTableData() {
        data = new String[contactProfileDTOs.size()][];
        ContactProfileDTO contactProfileDTO;
        for (int i = 0; i < contactProfileDTOs.size(); i++) {
            contactProfileDTO = contactProfileDTOs.get(i);
            data[i] = new String[]{contactProfileDTO.getContactId(),
                    contactProfileDTO.getContactName(),
                    contactProfileDTO.getContactIdentifier().toString()};
        }
    }

    @Override
    protected void updatePanel() {
        updateContactProfileDTOs();
        updateContactProfileTexts();
        setTableData();
        tableModel.setDataVector(data, columns);
    }

    private void updateContactProfileTexts() {
        contactProfileTexts = new String[contactProfileDTOs.size()];
        for (int i = 0; i < contactProfileDTOs.size(); i++) {
            contactProfileTexts[i] = contactProfileDTOs.get(i).toString();
        }
    }

    protected abstract void updateContactProfileDTOs();

    @Override
    protected void initializeComponents() {
        startConversationWithEveryoneButton = new JButton(ConfigManager.getString(configIdentifier,
                "startConversationWithEveryoneButtonM"));
        startConversationButton = new JButton(ConfigManager.getString(configIdentifier, "startConversationButtonM"));
        tableModel = new DefaultTableModel(data, columns);
        contactsTable = new JTable(tableModel);
        scrollPane = new JScrollPane(contactsTable);

        goBackButton = new JButton(ConfigManager.getString(configIdentifier, "goBackButtonM"));
    }

    @Override
    protected void alignComponents() {
        startConversationWithEveryoneButton.setBounds(
                ConfigManager.getInt(configIdentifier, "startConversationWithEveryoneButtonX"),
                ConfigManager.getInt(configIdentifier, "startConversationWithEveryoneButtonY"),
                ConfigManager.getInt(configIdentifier, "startConversationWithEveryoneButtonW"),
                ConfigManager.getInt(configIdentifier, "startConversationWithEveryoneButtonH"));
        add(startConversationWithEveryoneButton);
        startConversationButton.setBounds(ConfigManager.getInt(configIdentifier, "startConversationButtonX"),
                ConfigManager.getInt(configIdentifier, "startConversationButtonY"),
                ConfigManager.getInt(configIdentifier, "startConversationButtonW"),
                ConfigManager.getInt(configIdentifier, "startConversationButtonH"));
        add(startConversationButton);

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);

        goBackButton.setBounds(ConfigManager.getInt(configIdentifier, "goBackButtonX"),
                ConfigManager.getInt(configIdentifier, "goBackButtonY"),
                ConfigManager.getInt(configIdentifier, "goBackButtonW"),
                ConfigManager.getInt(configIdentifier, "goBackButtonH"));
        add(goBackButton);
    }

    @Override
    protected void connectListeners() {
        startConversationButton.addActionListener(actionEvent -> {
            String chosenContactIdsString = JOptionPane.showInputDialog(mainFrame,
                    ConfigManager.getString(ConfigFileIdentifier.TEXTS, "chooseContactIds"));
            if (chosenContactIdsString == null) return;

            ArrayList<String> chosenContactIds = new ArrayList<>(
                    List.of(
                            chosenContactIdsString.split(", ")
                    )
            );

            Response response = clientController.checkIfContactIdsExist(chosenContactIds);
            if (response == null) return;
            if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                MasterLogger.clientError(clientController.getId(), response.getErrorMessage(), "connectListeners",
                        getClass());
                return;
            }

            response = clientController.sendMessageNotificationsIfNecessary(chosenContactIds, contactProfileDTOs,
                    offlineModeDTO.getId());
            if (response == null) return;

            if (response.getUnsolicitedMessage() != null) {
                JOptionPane.showMessageDialog(mainFrame, response.getUnsolicitedMessage());
                MasterLogger.clientInfo(clientController.getId(), response.getUnsolicitedMessage(),
                        "connectListeners", getClass());
                return;
            }

            MasterLogger.clientInfo(clientController.getId(), "Opted to start a conversation",
                    "connectListeners", getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(new ConversationStartingRoom(mainFrame, mainMenu, offlineModeDTO,
                    chosenContactIds));
        });

        startConversationWithEveryoneButton.addActionListener(actionEvent -> {
            ArrayList<String> allContactIds = contactManager.getAllContactIds(contactProfileDTOs);
            if (allContactIds.isEmpty()) {
                String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,  "youHaveNoContacts");

                JOptionPane.showMessageDialog(mainFrame, errorMessage);
                MasterLogger.clientError(clientController.getId(), errorMessage, "connectListeners", getClass());
                return;
            }

            MasterLogger.clientInfo(clientController.getId(), "Opted to start a conversation with everyone",
                    "connectListeners", getClass());
            stopPanelLoop();
            mainFrame.setCurrentPanel(new ConversationStartingRoom(mainFrame, mainMenu, offlineModeDTO,
                    allContactIds));
        });

        goBackButton.addActionListener(actionEvent -> {
            MasterLogger.clientInfo(clientController.getId(), "Went back to messenger view",
                    "connectListeners", getClass());
            stopPanelLoop();
            if (offlineModeDTO.getUserIdentifier() == UserIdentifier.STUDENT) {
                mainFrame.setCurrentPanel(new StudentMessengerView(mainFrame, mainMenu, offlineModeDTO));
            } // TODO: add to this
        });

    }
}
