package server.database.models.pojos.users;

import org.hibernate.annotations.DiscriminatorOptions;
import server.database.models.pojos.abstractions.Department;
import server.database.models.pojos.media.Picture;

import javax.persistence.*;
import java.util.Date;

@Entity
@DiscriminatorOptions(force = true)
@Table(name = "Users") // added "s" to avoid conflict with reserved words
public abstract class User {
    @Id
    protected String id;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    protected Department department;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER) // TODO: changing pfp through gui?
    @JoinColumn(name = "image_id")
    protected Picture profilePicture;
    @Column
    protected UserIdentifier userIdentifier;
    @Column
    protected String nationalId;
    @Column
    protected String firstName;
    @Column
    protected String lastName;
    @Column
    protected String phoneNumber;
    @Column
    protected String emailAddress;
    @Column
    protected String password;
    @Column
    protected Date lastLogin;

    public User() {
        // TODO: setting default profile picture
    }

    public User(UserIdentifier userIdentifier) {
        // TODO: setting default profile picture
        this.userIdentifier = userIdentifier;
    }

    protected abstract void initializeId();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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
}
