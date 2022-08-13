package shareables.models.pojos.users;

import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.media.Picture;
import shareables.models.pojos.messaging.Messenger;
import shareables.models.pojos.notifications.NotificationsManager;

import java.time.LocalDateTime;

public abstract class User extends Identifiable {
    protected String departmentId;
    protected Picture profilePicture;
    protected Messenger messenger;
    protected NotificationsManager notificationsManager;
    protected UserIdentifier userIdentifier;
    protected String nationalId;
    protected String firstName;
    protected String lastName;
    protected String phoneNumber;
    protected String emailAddress;
    protected String password;
    protected LocalDateTime lastLogin;

    public User() {
        profilePicture = new Picture(); // sets the default profile picture
    }

    public User(UserIdentifier userIdentifier) {
        this.userIdentifier = userIdentifier;
        profilePicture = new Picture(true); // sets the default profile picture
    }

    protected void initializeMessenger(String id) {
        messenger = new Messenger(id);
    }

    protected void initializeNotificationsManager(String id) {
        notificationsManager = new NotificationsManager(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String fetchName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Picture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Picture profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserIdentifier getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(UserIdentifier userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public void updateLastLogin() {
        lastLogin = LocalDateTime.now();
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public void setMessenger(Messenger messenger) {
        this.messenger = messenger;
    }

    public NotificationsManager getNotificationsManager() {
        return notificationsManager;
    }

    public void setNotificationsManager(NotificationsManager notificationsManager) {
        this.notificationsManager = notificationsManager;
    }
}
