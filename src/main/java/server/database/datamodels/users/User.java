package server.database.datamodels.users;

import server.database.datamodels.abstractions.Department;
import server.database.datamodels.media.Picture;
import shareables.utils.images.ImageIdentifier;

import javax.persistence.*;

@Entity
@Table(name = "Users") // added "s" to avoid conflict with reserved words
public abstract class User {
    @Id
    protected String id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "department_id")
    protected Department department;
    @OneToOne(cascade = CascadeType.PERSIST) // TODO: changing pfp through gui?
    @JoinColumn(name = "image_id")
    protected Picture profilePicture;
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

    public User() {
        // TODO: setting default profile picture
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
}
