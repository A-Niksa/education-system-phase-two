package server.database.datamodels.users;

import server.database.datamodels.abstractions.Department;
import server.database.datamodels.abstractions.Transcript;

import javax.persistence.*;

@Entity
@Table(name = "STUDENTS")
public class Student {
    private static int sequentialId = 0;

    @Id
    private String id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "advising_professor_id", nullable = false)
    private Professor advisingProfessor;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinTable(name = "STUDENTS_TRANSCRIPTS")
    private Transcript transcript;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String nationalId;
    @Column
    private String phoneNumber;
    @Column
    private String emailAddress;
    @Column
    private DegreeLevel degreeLevel;
    @Column
    private StudentStatus studentStatus;
    @Column
    private int yearOfEntry;
    @Column
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
