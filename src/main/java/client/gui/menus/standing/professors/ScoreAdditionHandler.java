package client.gui.menus.standing.professors;

import client.gui.MainFrame;
import client.locallogic.standing.ScoreFormatUtils;
import shareables.network.DTOs.CourseScoreDTO;
import shareables.network.DTOs.OfflineModeDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class ScoreAdditionHandler implements ActionListener {
    private MainFrame mainFrame;
    private TemporaryStandingManager temporaryStandingManager;
    private CourseScoreDTO courseScoreDTO;
    private int clientControllerId;
    private ConfigFileIdentifier configIdentifier;
    private HashMap<String, Double> studentIdTemporaryScoreMap;

    public ScoreAdditionHandler(MainFrame mainFrame, TemporaryStandingManager temporaryStandingManager,
                                CourseScoreDTO courseScoreDTO, int clientControllerId, ConfigFileIdentifier configIdentifier,
                                HashMap<String, Double> studentIdTemporaryScoreMap) {
        this.mainFrame = mainFrame;
        this.temporaryStandingManager = temporaryStandingManager;
        this.courseScoreDTO = courseScoreDTO;
        this.clientControllerId = clientControllerId;
        this.configIdentifier = configIdentifier;
        this.studentIdTemporaryScoreMap = studentIdTemporaryScoreMap;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        MasterLogger.clientInfo(clientControllerId, "Opened the temporary scoring option panel",
                "actionPerformed", getClass());

        String scoreText = JOptionPane.showInputDialog(mainFrame,
                ConfigManager.getString(configIdentifier, "scoreTextM"));
        if (scoreText == null) {
            return;
        }

        Double score;
        try {
            score = Double.valueOf(scoreText);
        } catch (NumberFormatException e) {
            String fatalMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                    "wrongDoubleNumberFormat");
            MasterLogger.clientFatal(clientControllerId, fatalMessage, "actionPerformed",
                    getClass());
            JOptionPane.showMessageDialog(mainFrame, fatalMessage);
            return;
        }

        if (!ScoreFormatUtils.isInValidRange(score)) {
            String errorMessage = ConfigManager.getString(ConfigFileIdentifier.TEXTS, "invalidScoreRange");
            MasterLogger.clientError(clientControllerId, errorMessage, "actionPerformed", getClass());
            JOptionPane.showMessageDialog(mainFrame, errorMessage);
            return;
        }

        Double roundedScore = ScoreFormatUtils.roundToTheNearestQuarter(score);
        studentIdTemporaryScoreMap.put(courseScoreDTO.getStudentId(), roundedScore);
        MasterLogger.clientInfo(clientControllerId, "Drafted a temporary score for the selected student",
                "actionPerformed", getClass());
        temporaryStandingManager.updateTable();
    }
}
