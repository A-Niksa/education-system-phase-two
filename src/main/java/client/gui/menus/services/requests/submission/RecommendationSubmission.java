package client.gui.menus.services.requests.submission;

import client.gui.DynamicPanelTemplate;
import client.gui.MainFrame;
import client.gui.PanelTemplate;
import client.gui.menus.main.MainMenu;
import client.gui.utils.ErrorUtils;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.OfflineModeDTO;
import shareables.network.DTOs.ProfessorDTO;
import shareables.network.pinging.Loop;
import shareables.network.responses.Response;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RecommendationSubmission extends DynamicPanelTemplate {
    private Student student;
    private ArrayList<ProfessorDTO> professorDTOs;
    private JTextField recommendingProfessorId;
    private JButton submitRequest;
    private DefaultTableModel tableModel;
    private JTable professorsTable;
    private String[] columns;
    private String[][] data;
    private JSeparator separator;
    private JLabel currentRecommendationsPrompt;
    private ArrayList<JLabel> currentRecommendations;
    private RecommendationDisplayer recommendationDisplayer;

    public RecommendationSubmission(MainFrame mainFrame, MainMenu mainMenu, User user, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu, offlineModeDTO);
        student = (Student) user;
        configIdentifier = ConfigFileIdentifier.GUI_RECOMMENDATION_SUBMISSION;
        updateProfessorDTOs();
        initializeColumns();
        setTableData();
        drawPanel();
        startPinging(offlineModeDTO.getId());
    }

    private void initializeColumns() {
        columns = new String[4];
        columns[0] = ConfigManager.getString(configIdentifier, "professorIdCol");
        columns[1] = ConfigManager.getString(configIdentifier, "nameCol");
        columns[2] = ConfigManager.getString(configIdentifier, "academicLevelCol");
        columns[3] = ConfigManager.getString(configIdentifier, "emailAddressCol");
    }

    private void updateProfessorDTOs() {
        clientController.getProfessorDTOs(); // to rule out incomplete data
        Response response = clientController.getProfessorDTOs();
        if (response == null) return;
        professorDTOs = (ArrayList<ProfessorDTO>) response.get("professorDTOs");
    }

    private void setTableData() {
        data = new String[professorDTOs.size()][];
        ProfessorDTO professorDTO;
        for (int i = 0; i < professorDTOs.size(); i++) {
            professorDTO = professorDTOs.get(i);
            data[i] = new String[]{professorDTO.getId(),
                    professorDTO.getName(),
                    professorDTO.getAcademicLevel().toString(),
                    professorDTO.getEmailAddress()};
        }
    }

    @Override
    protected void initializeComponents() {
        recommendingProfessorId = new JTextField(ConfigManager.getString(configIdentifier, "recommendingProfessorIdM"));
        submitRequest = new JButton(ConfigManager.getString(configIdentifier, "submitRequestM"));
        tableModel = new DefaultTableModel(data, columns);
        professorsTable = new JTable(tableModel);
        separator = new JSeparator();
        currentRecommendationsPrompt = new JLabel(
                ConfigManager.getString(configIdentifier, "currentRecommendationsPromptM"));
        currentRecommendations = new ArrayList<>();
        recommendationDisplayer = new RecommendationDisplayer(this, currentRecommendations, offlineModeDTO, clientController);
    }

    @Override
    protected void alignComponents() {
        professorsTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(professorsTable);
        scrollPane.setBounds(ConfigManager.getInt(configIdentifier, "scrollPaneX"),
                ConfigManager.getInt(configIdentifier, "scrollPaneY"),
                ConfigManager.getInt(configIdentifier, "scrollPaneW"),
                ConfigManager.getInt(configIdentifier, "scrollPaneH"));
        add(scrollPane);

        recommendingProfessorId.setBounds(ConfigManager.getInt(configIdentifier, "recommendingProfessorIdX"),
                ConfigManager.getInt(configIdentifier, "recommendingProfessorIdY"),
                ConfigManager.getInt(configIdentifier, "recommendingProfessorIdW"),
                ConfigManager.getInt(configIdentifier, "recommendingProfessorIdH"));
        add(recommendingProfessorId);
        submitRequest.setBounds(ConfigManager.getInt(configIdentifier, "submitRequestX"),
                ConfigManager.getInt(configIdentifier, "submitRequestY"),
                ConfigManager.getInt(configIdentifier, "submitRequestW"),
                ConfigManager.getInt(configIdentifier, "submitRequestH"));
        add(submitRequest);

        separator.setOrientation(SwingConstants.VERTICAL);
        separator.setBackground(Color.BLACK);
        separator.setForeground(Color.BLACK);
        separator.setBounds(ConfigManager.getInt(configIdentifier, "separatorX"),
                ConfigManager.getInt(configIdentifier, "separatorY"),
                ConfigManager.getInt(configIdentifier, "separatorW"),
                ConfigManager.getInt(configIdentifier, "separatorH"));
        add(separator);

        currentRecommendationsPrompt.setBounds(ConfigManager.getInt(configIdentifier, "currentRecommendationsPromptX"),
                ConfigManager.getInt(configIdentifier, "currentRecommendationsPromptY"),
                ConfigManager.getInt(configIdentifier, "currentRecommendationsPromptW"),
                ConfigManager.getInt(configIdentifier, "currentRecommendationsPromptH"));
        add(currentRecommendationsPrompt);
        recommendationDisplayer.displayRecommendations();
    }

    // TODO: bug: list of all professors won't be displayed completely if i close the program and run it again

    @Override
    protected void connectListeners() {
        submitRequest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String selectedProfessorId = recommendingProfessorId.getText();
                Response response = clientController.askForRecommendation(offlineModeDTO.getId(), selectedProfessorId);
                if (response == null) return;
                if (ErrorUtils.showErrorDialogIfNecessary(mainFrame, response)) {
                    MasterLogger.clientError(clientController.getId(), response.getErrorMessage(),
                            "connectListeners", getClass());
                } else {
                    MasterLogger.clientInfo(clientController.getId(), "Recommendation letter request submitted",
                            "connectListeners", getClass());
                    JOptionPane.showMessageDialog(mainFrame,
                            ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                                    "recommendationRequestSuccessfullySubmitted"));
                }
            }
        });
    }

    @Override
    protected void updatePanel() {
        updateProfessorDTOs();
        setTableData();
        // TODO: sorting the profs list perhaps (by id)?
        tableModel.setDataVector(data, columns);
        currentRecommendations.forEach(this::remove);
        recommendationDisplayer.displayRecommendations();
    }
}
