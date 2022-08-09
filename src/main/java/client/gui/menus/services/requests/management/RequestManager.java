package client.gui.menus.services.requests.management;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.OfflineModeDTO;
import shareables.network.DTOs.RequestDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public abstract class RequestManager extends DynamicPanelTemplate {
    protected Professor professor;
    protected DefaultTableModel tableModel;
    protected JTable requestDTOsTable;
    protected String[] columns;
    protected String[][] data;
    protected ArrayList<RequestDTO> requestDTOs;
    protected ArrayList<JButton> acceptButtonsList;
    protected ArrayList<JButton> declineButtonsList;
    private String approveButtonMessage;
    private String declineButtonMessage;

    public RequestManager(MainFrame mainFrame, MainMenu mainMenu, Professor professor, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.professor = professor;
        configIdentifier = ConfigFileIdentifier.GUI_REQUEST_MANAGER;
    }

    protected abstract void initializeColumns();

    protected abstract void setRequestsList();

    protected abstract void setRequestsTableData();

    protected void setTable() {
        setRequestsList();
        setRequestsTableData();
    }

    protected void drawInteractivePanel() {
        setTable();
        drawPanel();
    }

    @Override
    protected void initializeComponents() {
        acceptButtonsList = new ArrayList<>();
        declineButtonsList = new ArrayList<>();

        tableModel = new DefaultTableModel(data, columns);
        requestDTOsTable = new JTable(tableModel);

        approveButtonMessage = ConfigManager.getString(configIdentifier, "approveButtonM");
        declineButtonMessage = ConfigManager.getString(configIdentifier, "declineButtonM");
        initializeButtons();
    }

    protected void initializeButtons() {
        acceptButtonsList.clear();
        declineButtonsList.clear();
        for (int i = 0; i < requestDTOs.size(); i++) {
            JButton approveButton = new JButton(approveButtonMessage);
            acceptButtonsList.add(approveButton);
            JButton declineButton = new JButton(declineButtonMessage);
            declineButtonsList.add(declineButton);
        }
    }

    @Override
    protected void alignComponents() {
        requestDTOsTable.setRowHeight(ConfigManager.getInt(configIdentifier, "requestsTableRowH"));
        JScrollPane scrollPane = new JScrollPane(requestDTOsTable);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);

        alignButtons();
    }

    protected void alignButtons() {
        int currentX = ConfigManager.getInt(configIdentifier, "startingX");
        int currentY = ConfigManager.getInt(configIdentifier, "startingY");
        int buttonWidth = ConfigManager.getInt(configIdentifier, "buttonW");
        int buttonHeight = ConfigManager.getInt(configIdentifier, "buttonH");
        int incrementOfX = ConfigManager.getInt(configIdentifier, "incX");
        int incrementOfY = ConfigManager.getInt(configIdentifier, "incY");
        JButton approveButton, declineButton;
        for (int i = 0; i < requestDTOs.size(); i++) {
            approveButton = acceptButtonsList.get(i);
            declineButton = declineButtonsList.get(i);
            approveButton.setBounds(currentX, currentY, buttonWidth, buttonHeight);
            declineButton.setBounds(currentX + incrementOfX, currentY, buttonWidth, buttonHeight);
            add(approveButton);
            add(declineButton);
            currentY += buttonHeight + incrementOfY;
        }
    }

    @Override
    protected void connectListeners() {
        for (int i = 0; i < requestDTOs.size(); i++) {
            setAcceptListener(i);
            setDeclineListener(i);
        }
    }

    protected abstract void setAcceptListener(int index);

    protected abstract void setDeclineListener(int index);

    @Override
    protected void updatePanel() {
        setRequestsList();
        setRequestsTableData();
        tableModel.setDataVector(data, columns);
        acceptButtonsList.forEach(this::remove);
        declineButtonsList.forEach(this::remove);
        initializeButtons();
        alignButtons();
        connectListeners();
        acceptButtonsList.forEach(Component::repaint);
        declineButtonsList.forEach(Component::repaint);
        acceptButtonsList.forEach(Component::validate);
        declineButtonsList.forEach(Component::validate);
        // TODO: java.lang.ArrayIndexOutOfBoundsException: 0 >= 0
    }
}
