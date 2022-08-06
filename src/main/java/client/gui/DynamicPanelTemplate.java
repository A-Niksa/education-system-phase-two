package client.gui;

import client.gui.menus.main.MainMenu;
import shareables.network.DTOs.OfflineModeDTO;
import shareables.network.pinging.Loop;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class DynamicPanelTemplate extends PanelTemplate {
    public static final double PINGING_FPS;
    static {
        PINGING_FPS = ConfigManager.getDouble(ConfigFileIdentifier.CONSTANTS, "pingingFps");
    }

    protected boolean isOnline;
    protected Runnable pingingTask;
    protected Loop panelLoop;
    protected OfflineModeDTO offlineModeDTO;

    public DynamicPanelTemplate(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu);
        this.offlineModeDTO = offlineModeDTO;
    }

    protected void startPinging(String userId) {
        initializePingingTask(userId);
        initializePanelLoop();
        startPanelLoop();
    }

    protected void startPingingIfOnline(String userId, OfflinePanel offlinePanel) {
        if (clientController.isClientOnline()) {
            initializePingingTask(userId);
            initializePanelLoop();
            startPanelLoop();
        } else {
            offlinePanel.goOffline(mainFrame, this, clientController);
        }
    }

    private void checkIfClientIsOnline() {
        isOnline = clientController.isClientOnline();
    }

    private void initializePingingTask(String userId) {
        pingingTask = () -> {
            checkIfClientIsOnline();
            if (isOnline) {
                updateOfflineModeDTO(userId);
                updatePanel();
            } else {
                notifyClientOfConnectionLoss();
                goToMainMenu();
                mainMenu.goOffline(mainFrame, this, clientController);
            }
        };
    }

    private void notifyClientOfConnectionLoss() {
        JOptionPane.showMessageDialog(mainFrame, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "connectionLoss"));
    }

    private void goToMainMenu() {
        stopPanelLoop();
        mainMenu.setOfflineModeDTO(offlineModeDTO);
        mainMenu.drawPanel();
        mainFrame.setCurrentPanel(mainMenu);
    }

    private void updateOfflineModeDTO(String userId) {
        Response response = clientController.getOfflineModeDTO(userId);
        if (response.getResponseStatus() == ResponseStatus.OK) {
            offlineModeDTO = (OfflineModeDTO) response.get("offlineModeDTO");
        }
    }

    private void initializePanelLoop() {
        panelLoop = new Loop(PINGING_FPS, pingingTask);
    }

    protected abstract void updatePanel();

    protected void startPanelLoop() {
        panelLoop.start();
    }

    protected void stopPanelLoop() {
        panelLoop.stop();
    }

    @Override
    protected void connectTemplateListeners() {
        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MasterLogger.clientInfo(clientController.getId(), "Returned to the main menu",
                        "connectListeners", getClass());
                stopPanelLoop();
                mainMenu.setOfflineModeDTO(offlineModeDTO);
                mainFrame.setCurrentPanel(mainMenu);
                // TODO: new mainMenu?
            }
        });
    }
}