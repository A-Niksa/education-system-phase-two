package client.gui.menus.enrolment.viewing;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.menus.main.MainMenu;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.DTOs.generalmodels.ProfessorDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ProfessorsListView extends DynamicPanelTemplate {
    private ArrayList<ProfessorDTO> professorDTOs;
    private DefaultTableModel tableModel;
    private JTable professorsTable;
    private String[] columns;
    private String[][] data;

    public ProfessorsListView(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        configIdentifier = ConfigFileIdentifier.GUI_LIST_VIEW;
        updateProfessorDTOs();
        initializeColumns();
        setTableData();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    @Override
    protected void updatePanel() {
        updateProfessorDTOs();
        setTableData();
        tableModel.setDataVector(data, columns);
    }

    private void updateProfessorDTOs() {
//        clientController.getProfessorDTOs(); // duplicating request to avoid receiving lossy data
        Response response = clientController.getProfessorDTOs();
        if (response == null) return;
        professorDTOs = (ArrayList<ProfessorDTO>) response.get("professorDTOs");
    }

    private void initializeColumns() {
        columns = new String[5];
        columns[0] = ConfigManager.getString(configIdentifier, "professorIdCol");
        columns[1] = ConfigManager.getString(configIdentifier, "nameAndSurnameCol");
        columns[2] = ConfigManager.getString(configIdentifier, "academicLevelCol");
        columns[3] = ConfigManager.getString(configIdentifier, "officeNumberCol");
        columns[4] = ConfigManager.getString(configIdentifier, "emailAddressCol");
    }

    private void setTableData() {
        data = new String[professorDTOs.size()][];
        ProfessorDTO professorDTO;
        for (int i = 0; i < professorDTOs.size(); i++) {
            professorDTO = professorDTOs.get(i);
            data[i] = new String[]{professorDTO.getId(),
                    professorDTO.getName(),
                    professorDTO.getAcademicLevel().toString(),
                    professorDTO.getOfficeNumber() + "",
                    professorDTO.getEmailAddress()};
        }
    }

    @Override
    protected void initializeComponents() {
        tableModel = new DefaultTableModel(data, columns);
        professorsTable = new JTable(tableModel);
    }

    @Override
    protected void alignComponents() {
        professorsTable.setRowHeight(ConfigManager.getInt(configIdentifier, "tableRowH"));
        JScrollPane scrollPane = new JScrollPane(professorsTable);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "professorScrollPaneX"),
                ConfigManager.getInt(configIdentifier, "professorScrollPaneY"),
                ConfigManager.getInt(configIdentifier, "professorScrollPaneW"),
                ConfigManager.getInt(configIdentifier, "professorScrollPaneH"));
        add(scrollPane);
    }

    @Override
    protected void connectListeners() {
    }
}
