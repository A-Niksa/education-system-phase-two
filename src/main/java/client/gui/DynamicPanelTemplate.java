package client.gui;

import client.gui.menus.main.*;
import client.locallogic.localdatabase.management.QueuedMessagesManager;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.network.pinging.Loop;
import shareables.network.responses.Response;
import shareables.network.responses.ResponseStatus;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import javax.swing.*;

public abstract class DynamicPanelTemplate extends PanelTemplate {
    public static final double PINGING_FPS;
    static {
        PINGING_FPS = ConfigManager.getDouble(ConfigFileIdentifier.CONSTANTS, "pingingFps");
    }

    protected boolean isOnline;
    protected Runnable pingingTask;
    protected Loop panelLoop;
    protected OfflineModeDTO offlineModeDTO;
    protected QueuedMessagesManager queuedMessagesManager;

    public DynamicPanelTemplate(MainFrame mainFrame, MainMenu mainMenu, OfflineModeDTO offlineModeDTO) {
        super(mainFrame, mainMenu);
        this.offlineModeDTO = offlineModeDTO;
        queuedMessagesManager = new QueuedMessagesManager(clientController, offlineModeDTO.getId());
        isOnline = true; // default value
    }

    protected void startPinging(String userId) {
        initializePingingTask(userId);
        initializePanelLoop();
        startPanelLoop();
    }

    protected void startPingingIfOnline(String userId, OfflinePanel offlinePanel) {
        initializePingingTask(userId);
        initializePanelLoop();
        if (isOnline) {
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
                queuedMessagesManager.sendQueuedMessages();
            } else {
                notifyClientOfConnectionLoss();
                goToMainMenu();
            }
        };
    }

    private void notifyClientOfConnectionLoss() {
        JOptionPane.showMessageDialog(mainFrame, ConfigManager.getString(ConfigFileIdentifier.TEXTS,
                "connectionLoss"));
    }

    private void goToMainMenu() {
        if (this instanceof OfflinePanel) facilitateChangingPanel((OfflinePanel) this);
        else stopPanelLoop();

        MainMenu newMainMenu;
        switch (mainMenu.getMainMenuType()) {
            case PROFESSOR:
                newMainMenu = new ProfessorMenu(mainFrame, offlineModeDTO, isOnline);
                break;
            case STUDENT:
                newMainMenu = new StudentMenu(mainFrame, offlineModeDTO, isOnline);
                break;
            case MR_MOHSENI:
                newMainMenu = new MrMohseniMenu(mainFrame, offlineModeDTO, isOnline);
                break;
            case ADMIN:
                newMainMenu = new AdminMenu(mainFrame, offlineModeDTO, isOnline);
                break;
            default:
                newMainMenu = null; // added for explicitness
        }
        mainFrame.setCurrentPanel(newMainMenu);
        MasterLogger.clientInfo(clientController.getId(), "Returned to the main menu",
                "connectListeners", getClass());
        mainMenu = newMainMenu;
    }

    private void updateOfflineModeDTO(String userId) {
        Response response = clientController.getOfflineModeDTO(userId);
        if (response == null) return;
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

    public void stopPanelLoop() {
        panelLoop.stop();
    }

    protected void restartPanelLoop() {
        panelLoop.restart();
    }

    protected void facilitateChangingPanel(OfflinePanel offlinePanel) {
        offlinePanel.removeOfflineComponents((JPanel) offlinePanel);
        stopPanelLoop();
    }

    protected void staticallyRemoveOfflineComponents() {
        OfflinePanel.OFFLINE_COMPONENTS.forEach(this::remove);
        OfflinePanel.OFFLINE_COMPONENTS.clear();
        this.repaint();
        this.validate();
    }

    @Override
    protected void connectTemplateListeners() {
        mainMenuButton.addActionListener(actionEvent -> goToMainMenu());
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}