package shareables.models.pojos.users;

import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.media.Picture;
import shareables.models.pojos.messaging.Messenger;

import java.util.Date;

public abstract class User extends Identifiable {
    protected String departmentId;
    protected Picture profilePicture;
    protected Messenger messenger;
    protected UserIdentifier userIdentifier;
    protected String nationalId;
    protected String firstName;
    protected String lastName;
    protected String phoneNumber;
    protected String emailAddress;
    protected String password;
    protected Date lastLogin;

    public User() {
        // TODO: setting default profile picture
    }

    public User(UserIdentifier userIdentifier) {
        // TODO: setting default profile picture
        this.userIdentifier = userIdentifier;
    }

    protected void initializeMessenger(String id) {
        messenger = new Messenger(id);
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

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public void setMessenger(Messenger messenger) {
        this.messenger = messenger;
    }
}
