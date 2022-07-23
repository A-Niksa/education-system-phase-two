package server.database.datamodels.users;

import server.database.datamodels.abstractions.Department;

import javax.persistence.*;

@Entity(name = "Student")
@Table(name = "STUDENTS")
public class Student {
    @Id
    private String id;
    private static int sequentialId = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advising_professor_id", nullable = false)
    private Professor advisingProfessor;

    private String firstName;
    private String lastName;
    private String nationalId;
    private String phoneNumber;
    private String emailAddress;
    private DegreeLevel degreeLevel;
    private StudentStatus studentStatus;
    private int yearOfEntry;
    private double GPA;
    // TODO: adding student image

    public static int getSequentialId() {
        return sequentialId;
    }

    public static void incrementSequentialId() {
        sequentialId++;
    }

    public String getId() {
        return id;
    }

    public Department getDepartment() {
        return department;
    }

    public Professor getAdvisingProfessor() {
        return advisingProfessor;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNationalId() {
        return nationalId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public DegreeLevel getDegreeLevel() {
        return degreeLevel;
    }

    public StudentStatus getStudentStatus() {
        return studentStatus;
    }

    public int getYearOfEntry() {
        return yearOfEntry;
    }

    public double getGPA() {
        return GPA;
    }
}
