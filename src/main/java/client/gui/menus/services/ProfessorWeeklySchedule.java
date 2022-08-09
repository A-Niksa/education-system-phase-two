package client.gui.menus.services;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.OfflinePanel;
import client.gui.menus.main.MainMenu;
import client.gui.utils.EnumArrayUtils;
import client.locallogic.services.WeeklyScheduleParser;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.CourseDTO;
import shareables.network.DTOs.OfflineModeDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.timing.timekeeping.Weekday;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ProfessorWeeklySchedule extends DynamicPanelTemplate implements OfflinePanel {
    private Professor professor;
    private ArrayList<CourseDTO> courseDTOs;
    private ArrayList<JPanel> weekdayPanels;
    private ArrayList<DefaultTableModel> weekdayTableModels;
    private ArrayList<JTable> weekdayTables;
    private String[] weekdays;
    private String[] columns;
    private JTabbedPane tabbedPane;

    public ProfessorWeeklySchedule(MainFrame mainFrame, MainMenu mainMenu, Professor professor, OfflineModeDTO offlineModeDTO,
                                   boolean isOnline) {
        super(mainFrame, mainMenu, offlineModeDTO);
        this.isOnline = isOnline;
        this.professor = professor;
        configIdentifier = ConfigFileIdentifier.GUI_WEEKLY_SCHEDULE;
        updateCourseDTOs();
        weekdays = EnumArrayUtils.initializeWeekdays();
        initializeColumnsArray();
        drawPanel();
        startPingingIfOnline(offlineModeDTO.getId(), this);
    }

    private void updateCourseDTOs() {
        courseDTOs = (ArrayList<CourseDTO>) offlineModeDTO.getCourseDTOs();
    }

    private void initializeColumnsArray() {
        columns = new String[3];
        columns[0] = ConfigManager.getString(configIdentifier, "courseNameColumn");
        columns[1] = ConfigManager.getString(configIdentifier, "startsFromColumn");
        columns[2] = ConfigManager.getString(configIdentifier, "endsAtColumn");
    }

    private String[][] getTableData(Weekday weekday) {
        ArrayList<CourseDTO> courseDTOsOnWeekday = WeeklyScheduleParser.getCourseDTOsOnWeekday(courseDTOs, weekday);
        String[][] data = new String[courseDTOsOnWeekday.size()][];
        CourseDTO courseDTO;
        for (int i = 0; i < courseDTOsOnWeekday.size(); i++) {
            courseDTO = courseDTOsOnWeekday.get(i);
            data[i] = new String[] {courseDTO.getCourseName(),
                    courseDTO.getWeeklyClassTimes().get(0).getStartTime().toString(),
                    courseDTO.getWeeklyClassTimes().get(0).getEndTime().toString()};
        }
        return data;
    }

    @Override
    protected void initializeComponents() {
        weekdayPanels = new ArrayList<>();
        weekdayTableModels = new ArrayList<>();
        weekdayTables = new ArrayList<>();
        for (Weekday weekday : Weekday.values()) {
            weekdayPanels.add(new JPanel());
            weekdayTableModels.add(new DefaultTableModel(getTableData(weekday), columns));
            weekdayTables.add(new JTable(
                    weekdayTableModels.get(weekdayTableModels.size() - 1)
            ));
        }
        tabbedPane = new JTabbedPane();
    }

    private void alignTable(JTable table, JPanel panel) {
        table.setRowHeight(ConfigManager.getInt(configIdentifier, "tableRowHeight"));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.setLayout(new BorderLayout(ConfigManager.getInt(configIdentifier, "tableHGap"),
                ConfigManager.getInt(configIdentifier, "tableVGap")));
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    protected void alignComponents() {
        tabbedPane.setBounds(ConfigManager.getInt(configIdentifier, "tabbedPaneX"),
                ConfigManager.getInt(configIdentifier, "tabbedPaneY"),
                ConfigManager.getInt(configIdentifier, "tabbedPaneW"),
                ConfigManager.getInt(configIdentifier, "tabbedPaneH"));
        String headerName;
        for (int i = 0; i < weekdayPanels.size(); i++) {
            headerName = weekdays[i];
            tabbedPane.add(headerName, weekdayPanels.get(i));
            alignTable(weekdayTables.get(i), weekdayPanels.get(i));
        }
        add(tabbedPane);
    }

    @Override
    protected void connectListeners() {
    }

    @Override
    protected void updatePanel() {
        updateCourseDTOs();
        for (Weekday weekday : Weekday.values()) {
            weekdayTableModels.get(weekday.getIndex())
                    .setDataVector(getTableData(weekday), columns);
        }
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
