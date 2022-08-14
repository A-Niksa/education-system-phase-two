package client.gui.menus.services;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.OfflinePanel;
import client.gui.menus.main.MainMenu;
import client.locallogic.menus.services.ExamsListSorter;
import shareables.network.DTOs.CourseDTO;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ProfessorExamsList extends DynamicPanelTemplate implements OfflinePanel {
    private ArrayList<CourseDTO> courseDTOs;
    private DefaultTableModel tableModel;
    private JTable examsTable;
    private String[] columns;
    private String[][] data;

    public ProfessorExamsList(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO,
                              boolean isOnline) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.isOnline = isOnline;
        configIdentifier = ConfigFileIdentifier.GUI_EXAMS_LIST;
        updateCourseDTOs();
        initializeColumns();
        setTableData();
        drawPanel();
        startPingingIfOnline(offlineModeDTO.getId(), this);
    }

    private void updateCourseDTOs() {
        courseDTOs = (ArrayList<CourseDTO>) offlineModeDTO.getCourseDTOs();
    }

    private void initializeColumns() {
        columns = new String[2];
        columns[0] = ConfigManager.getString(configIdentifier, "courseNameColumn");
        columns[1] = ConfigManager.getString(configIdentifier, "examDateAndTimeColumn");
    }

    private void setTableData() {
        ArrayList<CourseDTO> sortedCourseDTOs = ExamsListSorter.getSortedCourseDTOs(courseDTOs);
        CourseDTO courseDTO;
        data = new String[sortedCourseDTOs.size()][];
        for (int i = 0; i < sortedCourseDTOs.size(); i++) {
            courseDTO = sortedCourseDTOs.get(i);
            data[i] = new String[]{courseDTO.getCourseName(), courseDTO.fetchFormattedExamDate()};
        }
    }

    @Override
    protected void initializeComponents() {
        tableModel = new DefaultTableModel(data, columns);
        examsTable = new JTable(tableModel);
    }

    @Override
    protected void alignComponents() {
        examsTable.setRowHeight(ConfigManager.getInt(configIdentifier, "tableRowHeight"));
        JScrollPane scrollPane = new JScrollPane(examsTable);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);
    }

    @Override
    protected void connectListeners() {
    }

    @Override
    protected void updatePanel() {
        updateCourseDTOs();
        setTableData();
        tableModel.setDataVector(data, columns);
    }

    @Override
    public void disableOnlineComponents() {
        stopPanelLoop();
    }

    @Override
    public void enableOnlineComponents() {
        restartPanelLoop();
    }
}
