package client.gui.utils;

import client.gui.MainFrame;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;

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
}
