package client.gui.menus.standing.deputies;

import client.gui.MainFrame;
import gui.MainFrame;
import logic.menus.standing.CourseStatsMaster;
import logic.models.abstractions.Course;
import utils.database.data.CoursesDB;
import utils.logging.LogIdentifier;
import utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatsViewHandler implements ActionListener {
    private MainFrame mainFrame;
    private TemporaryStandingMaster temporaryStandingMaster;
    private String courseName;
    private String departmentName;

    public StatsViewHandler(MainFrame mainFrame, TemporaryStandingMaster temporaryStandingMaster, String courseName,
                            String departmentName) {
        this.mainFrame = mainFrame;
        this.temporaryStandingMaster = temporaryStandingMaster;
        this.courseName = courseName;
        this.departmentName = departmentName;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Course course = CoursesDB.getCourseWithName(courseName, departmentName);

        if (!CourseStatsMaster.allScoresAreFinalized(course)) {
            MasterLogger.log("cannot open course stats; the course scores have not been finalized",
                    LogIdentifier.ERROR, "actionPerformed", "gui.standing.StatsViewHandler");
            JOptionPane.showMessageDialog(mainFrame, "You cannot open the course statistics. The score of " +
                    "this course have not been finalized.");
            return;
        }

        int numberOfPassingStudents = CourseStatsMaster.getNumberOfPassingStudents(course);
        int numberOfFailingStudents = CourseStatsMaster.getNumberOfFailingStudents(course);
        String coursesAverageScore = CourseStatsMaster.getCoursesAverageScore(course);
        String coursesAverageScoreWithoutFailingStudent =
                CourseStatsMaster.getCoursesAverageScoreWithoutFailingStudents(course);

        String optionPanelDialogMessage = "Number of Passing Students: " + numberOfPassingStudents + "\n" +
                "Number of Failing Students: " + numberOfFailingStudents + "\n" +
                "Average Score of Course: " + coursesAverageScore + "\n" +
                "Average Score (Without Failing Students): " + coursesAverageScoreWithoutFailingStudent;

        JOptionPane.showMessageDialog(mainFrame, optionPanelDialogMessage);
        MasterLogger.log("deputy opened stats of " + courseName, LogIdentifier.INFO,
                "actionPerformed", "gui.standing.StatsViewHandler");
    }
}
