package client.gui.menus.services.requests.management;

import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.main.MainMenu;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.RequestDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import javax.swing.*;
import java.util.ArrayList;

public abstract class RequestManager extends PanelTemplate {
    Professor professor;
    protected JTable requestDTOsTable;
    protected String[] columns;
    protected String[][] data;
    protected ArrayList<RequestDTO> requestDTOs;
    protected ArrayList<JButton> approveButtonsList;
    protected ArrayList<JButton> declineButtonsList;

    public RequestManager(MainFrame mainFrame, MainMenu mainMenu, Professor professor) {
        super(mainFrame, mainMenu);
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
        approveButtonsList = new ArrayList<>();
        declineButtonsList = new ArrayList<>();

        requestDTOsTable = new JTable(data, columns);

        for (int i = 0; i < requestDTOs.size(); i++) {
            JButton approveButton = new JButton(ConfigManager.getString(configIdentifier, "approveButtonM"));
            approveButtonsList.add(approveButton);
            JButton declineButton = new JButton(ConfigManager.getString(configIdentifier, "declineButtonM"));
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

        int currentX = ConfigManager.getInt(configIdentifier, "startingX");
        int currentY = ConfigManager.getInt(configIdentifier, "startingY");
        int buttonWidth = ConfigManager.getInt(configIdentifier, "buttonW");
        int buttonHeight = ConfigManager.getInt(configIdentifier, "buttonH");
        int incrementOfX = ConfigManager.getInt(configIdentifier, "incX");
        int incrementOfY = ConfigManager.getInt(configIdentifier, "incY");
        JButton approveButton, declineButton;
        for (int i = 0; i < requestDTOs.size(); i++) {
            approveButton = approveButtonsList.get(i);
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
            setApproveListener(i);
            setDeclineListener(i);
        }
    }

    protected abstract void setApproveListener(int index);

    protected abstract void setDeclineListener(int index);
}
