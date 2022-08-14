package shareables.network.DTOs;

import shareables.models.pojos.abstractions.DepartmentName;
import shareables.models.pojos.media.Picture;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.models.pojos.users.students.StudentStatus;

public class StudentDTO {
    private String id;
    private String nationalId;
    private String name;
    private Picture profilePicture;
    private String phoneNumber;
    private String emailAddress;
    private String GPAString;
    private DepartmentName departmentName;
    private String advisingProfessorName;
    private int yearOfEntry;
    private DegreeLevel degreeLevel;
    private StudentStatus studentStatus;

    public StudentDTO() {
    }

    public Picture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Picture profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getGPAString() {
        return GPAString;
    }

    public void setGPAString(String GPAString) {
        this.GPAString = GPAString;
    }

    public DepartmentName getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(DepartmentName departmentName) {
        this.departmentName = departmentName;
    }

    public String getAdvisingProfessorName() {
        return advisingProfessorName;
    }

    public void setAdvisingProfessorName(String advisingProfessorName) {
        this.advisingProfessorName = advisingProfessorName;
    }

    public int getYearOfEntry() {
        return yearOfEntry;
    }

    public void setYearOfEntry(int yearOfEntry) {
        this.yearOfEntry = yearOfEntry;
    }

    public DegreeLevel getDegreeLevel() {
        return degreeLevel;
    }

    public void setDegreeLevel(DegreeLevel degreeLevel) {
        this.degreeLevel = degreeLevel;
    }

    public StudentStatus getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(StudentStatus studentStatus) {
        this.studentStatus = studentStatus;
    }
}
