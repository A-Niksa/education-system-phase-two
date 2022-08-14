package client.gui.menus.main;

import client.gui.MainFrame;
import client.gui.utils.ImageParsingUtils;
import client.locallogic.menus.main.DateStringFormatter;
import shareables.network.DTOs.offlinemode.OfflineModeDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import javax.swing.*;

public abstract class SpecialUserMenu extends MainMenu {
    public SpecialUserMenu(MainFrame mainFrame, String username, MainMenuType mainMenuType, OfflineModeDTO offlineModeDTO,
                           boolean isOnline) {
        super(mainFrame, username, mainMenuType, offlineModeDTO, isOnline);
        configIdentifier = ConfigFileIdentifier.GUI_SPECIAL_USERS_MAIN;
        reAlignSpecialComponents();
        connectMessengerListener();
    }

    public SpecialUserMenu(MainFrame mainFrame, MainMenuType mainMenuType, OfflineModeDTO offlineModeDTO, boolean isOnline) {
        super(mainFrame, mainMenuType, offlineModeDTO, isOnline);
        configIdentifier = ConfigFileIdentifier.GUI_SPECIAL_USERS_MAIN;
        reAlignSpecialComponents();
        connectMessengerListener();
    }

    private void reAlignSpecialComponents() {
        this.remove(messengerButton);
        this.remove(notificationsButton);

        messengerButton.setBounds(ConfigManager.getInt(configIdentifier, "messengerButtonX"),
                ConfigManager.getInt(configIdentifier, "messengerButtonY"),
                ConfigManager.getInt(configIdentifier, "messengerButtonW"),
                ConfigManager.getInt(configIdentifier, "messengerButtonH"));
        add(messengerButton);
    }

    @Override
    protected void updatePanel() {
        lastLoginTime.setText(lastLoginTimePrompt + DateStringFormatter.format(offlineModeDTO.getLastLogin()));
        nameLabel.setText(offlineModeDTO.getName());
        emailAddressLabel.setText(offlineModeDTO.getEmailAddress());
        ImageIcon profilePictureIcon = ImageParsingUtils.convertPictureToScaledImageIcon(offlineModeDTO.getProfilePicture(),
                ConfigManager.getInt(ConfigFileIdentifier.GUI_MAIN, "profilePictureW"),
                ConfigManager.getInt(ConfigFileIdentifier.GUI_MAIN, "profilePictureH"));
        profilePicture.setIcon(profilePictureIcon);
    }

    private void initializeComponents() {
        lastLoginTimePrompt = ConfigManager.getString(configIdentifier, "lastLoginTimeMessage");
        lastLoginTime = new JLabel(lastLoginTimePrompt + DateStringFormatter.format(offlineModeDTO.getLastLogin()));
        ImageIcon profilePictureIcon = ImageParsingUtils.convertPictureToScaledImageIcon(offlineModeDTO.getProfilePicture(),
                ConfigManager.getInt(configIdentifier, "profilePictureW"),
                ConfigManager.getInt(configIdentifier, "profilePictureH"));
        profilePicture = new JLabel(profilePictureIcon);
        nameLabel = new JLabel(offlineModeDTO.getName());
        emailAddressLabel = new JLabel(offlineModeDTO.getEmailAddress());
        logOutButton = new JButton(ConfigManager.getString(configIdentifier, "logOutButtonMessage"));
        notificationsButton = new JButton(ConfigManager.getString(configIdentifier, "notificationsButtonM"));
        messengerButton = new JButton(ConfigManager.getString(configIdentifier, "messengerButtonM"));
    }

    private void alignComponents() {
        profilePicture.setBounds(ConfigManager.getInt(configIdentifier, "profilePictureX"),
                ConfigManager.getInt(configIdentifier, "profilePictureY"),
                ConfigManager.getInt(configIdentifier, "profilePictureW"),
                ConfigManager.getInt(configIdentifier, "profilePictureH"));
        add(profilePicture);
        nameLabel.setBounds(ConfigManager.getInt(configIdentifier, "nameLabelX"),
                ConfigManager.getInt(configIdentifier, "nameLabelY"),
                ConfigManager.getInt(configIdentifier, "nameLabelW"),
                ConfigManager.getInt(configIdentifier, "nameLabelH"));
        add(nameLabel);
        emailAddressLabel.setBounds(ConfigManager.getInt(configIdentifier, "emailAddressLabelX"),
                ConfigManager.getInt(configIdentifier, "emailAddressLabelY"),
                ConfigManager.getInt(configIdentifier, "emailAddressLabelW"),
                ConfigManager.getInt(configIdentifier, "emailAddressLabelH"));
        add(emailAddressLabel);
        logOutButton.setBounds(ConfigManager.getInt(configIdentifier, "logOutButtonX"),
                ConfigManager.getInt(configIdentifier, "logOutButtonY"),
                ConfigManager.getInt(configIdentifier, "logOutButtonW"),
                ConfigManager.getInt(configIdentifier, "logOutButtonH"));
        add(logOutButton);
        lastLoginTime.setBounds(ConfigManager.getInt(configIdentifier, "lastLoginTimeX"),
                ConfigManager.getInt(configIdentifier, "lastLoginTimeY"),
                ConfigManager.getInt(configIdentifier, "lastLoginTimeW"),
                ConfigManager.getInt(configIdentifier, "lastLoginTimeH"));
        add(lastLoginTime);
        notificationsButton.setBounds(ConfigManager.getInt(configIdentifier, "notificationsButtonX"),
                ConfigManager.getInt(configIdentifier, "notificationsButtonY"),
                ConfigManager.getInt(configIdentifier, "notificationsButtonW"),
                ConfigManager.getInt(configIdentifier, "notificationsButtonH"));
        add(notificationsButton);
        messengerButton.setBounds(ConfigManager.getInt(configIdentifier, "messengerButtonX"),
                ConfigManager.getInt(configIdentifier, "messengerButtonY"),
                ConfigManager.getInt(configIdentifier, "messengerButtonW"),
                ConfigManager.getInt(configIdentifier, "messengerButtonH"));
        add(messengerButton);
    }

    protected abstract void connectMessengerListener();
}
