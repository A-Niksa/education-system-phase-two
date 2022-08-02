package client.gui.menus.services;

import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.main.MainMenu;
import client.gui.utils.EnumArrayUtils;
import client.locallogic.services.WeeklyScheduleParser;
import shareables.models.pojos.users.professors.Professor;
import shareables.network.DTOs.CourseDTO;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.timing.timekeeping.Weekday;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ProfessorWeeklySchedule extends PanelTemplate {
    private Professor professor;
    private ArrayList<CourseDTO> courseDTOs;
    private ArrayList<JPanel> weekdayPanels;
    private ArrayList<JTable> weekdayTables;
    private String[] weekdays;
    private String[] columns;
    private JTabbedPane tabbedPane;

    public ProfessorWeeklySchedule(MainFrame mainFrame, MainMenu mainMenu, Professor professor) {
        super(mainFrame, mainMenu);
        this.professor = professor;
        configIdentifier = ConfigFileIdentifier.GUI_WEEKLY_SCHEDULE;
        initializeCourseDTOs();
        weekdays = EnumArrayUtils.initializeWeekdays();
        initializeColumnsArray();
        drawPanel();
    }

    private void initializeCourseDTOs() {
        Response response = clientController.getProfessorCourseDTOs(professor.getId());
        courseDTOs = (ArrayList<CourseDTO>) response.get("courseDTOs");
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
        weekdayTables = new ArrayList<>();
        for (Weekday weekday : Weekday.values()) {
            weekdayPanels.add(new JPanel());
            weekdayTables.add(new JTable(getTableData(weekday), columns));
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
}
