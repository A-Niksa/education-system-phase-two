package backend.database.builders.users;

import backend.database.datamodels.abstractions.Department;
import backend.database.datamodels.users.DegreeLevel;
import backend.database.datamodels.users.Professor;
import backend.database.datamodels.users.Student;
import backend.database.datamodels.users.StudentStatus;

public class StudentBuilder {
    public String firstName;
    public String lastName;
    public String nationalId;
    public String phoneNumber;
    public String emailAddress;
    public Department department;
    public Professor advisingProfessor;
    public DegreeLevel degreeLevel;
    public StudentStatus studentStatus;
    public int yearOfEntry;
    public double GPA;

    public static StudentBuilder student() {
        return new StudentBuilder();
    }

    public StudentBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public StudentBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public StudentBuilder withNationalId(String nationalId) {
        this.nationalId = nationalId;
        return this;
    }

    public StudentBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public StudentBuilder withEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public StudentBuilder withDepartment(Department department) {
        this.department= department;
        return this;
    }

    public StudentBuilder withAdvisingProfessor(Professor advisingProfessor) {
        this.advisingProfessor = advisingProfessor;
        return this;
    }

    public StudentBuilder withDegreeIdentifier(DegreeLevel degreeLevel) {
        this.degreeLevel = degreeLevel;
        return this;
    }

    public StudentBuilder withStudentStatus(StudentStatus studentStatus) {
        this.studentStatus = studentStatus;
        return this;
    }

    public StudentBuilder withYearOfEntry(int yearOfEntry) {
        this.yearOfEntry = yearOfEntry;
        return this;
    }

    public StudentBuilder withGPA(double GPA) {
        this.GPA = GPA;
        return this;
    }

    public Student build() {
        return new Student(this);
    }
}
