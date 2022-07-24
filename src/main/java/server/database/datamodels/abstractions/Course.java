package server.database.datamodels.abstractions;

import server.database.datamodels.users.professors.Professor;
import server.database.datamodels.users.students.Student;
import server.database.idgeneration.IdGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Courses")
public class Course {
    private static int sequentialId = 0;

    @Id
    private String id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "department_id")
    private Department department;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "teaching_professor_id")
    private List<Professor> teachingProfessors;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "TA_id")
    private List<Student> TAs;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "Courses_Students")
    private List<Student> students;
    @Column
    private int numberOfCredits;
    @Column
    private boolean isActive; // shows whether the course is being currently taught

    public Course() {
        isActive = false; // default value
        teachingProfessors = new ArrayList<>();
        TAs = new ArrayList<>();
        students = new ArrayList<>();
    }

    public void addToTeachingProfessors(Professor professor) {
        teachingProfessors.add(professor);
    }

    public void removeFromTeachingProfessors(Professor professor) {
        teachingProfessors.removeIf(e -> e.getId().equals(professor.getId()));
    }

    public void addToTAs(Student student) {
        TAs.add(student);
    }

    public void removeFromTAs(Student student) {
        TAs.removeIf(e -> e.getId().equals(student.getId()));
    }

    public void addToStudents(Student student) {
        students.add(student);
    }

    public void removeFromStudents(Student student) {
        students.removeIf(e -> e.getId().equals(student.getId()));
    }

    public void initializeId() {
        id = IdGenerator.generateId(this);
    }

    public static int getSequentialId() {
        return sequentialId;
    }

    public static void incrementSequentialId() {
        sequentialId++;
    }

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

    public List<Professor> getTeachingProfessors() {
        return teachingProfessors;
    }

    public void setTeachingProfessors(List<Professor> teachingProfessors) {
        this.teachingProfessors = teachingProfessors;
    }

    public List<Student> getTAs() {
        return TAs;
    }

    public void setTAs(List<Student> TAs) {
        this.TAs = TAs;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public int getNumberOfCredits() {
        return numberOfCredits;
    }

    public void setNumberOfCredits(int numberOfCredits) {
        this.numberOfCredits = numberOfCredits;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}