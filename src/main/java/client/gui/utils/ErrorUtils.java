package client.gui.utils;

import client.gui.MainFrame;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;

public class ErrorUtils {
    public static boolean showErrorDialogIfNecessary(MainFrame mainFrame, Response response) {
        // returns true if there's an error
        if (response.getResponseStatus() == ResponseStatus.ERROR) {
            JOptionPane.showMessageDialog(mainFrame, response.getErrorMessage());
            return true;
        }
        return false;
    }

    public static boolean showNoSelectedCourseErrorDialogIfNecessary(MainFrame mainFrame, String selectedCourseName,
                                                                     String errorMessage) {
        if (selectedCourseName == null) {
            JOptionPane.showMessageDialog(mainFrame, errorMessage);
            return true;
        }
        return false;
    }
}
