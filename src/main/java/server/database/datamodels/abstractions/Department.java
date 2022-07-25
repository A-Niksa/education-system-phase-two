package server.database.datamodels.abstractions;

import server.database.datamodels.users.professors.Professor;
import server.database.datamodels.users.students.Student;
import server.database.idgeneration.IdGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Departments")
public class Department {
    @Id
    private String id;
    @OneToMany(mappedBy = "department", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Professor> professors;
    @OneToMany(mappedBy = "department", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Student> students;
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "dean_id")
    private Professor dean;
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "deputy_id")
    private Professor deputy;
    @Column
    private DepartmentName departmentName;

    public Department() {
        professors = new ArrayList<>();
        students = new ArrayList<>();
    }

    public void addToProfessors(Professor professor) {
        professors.add(professor);
    }

    public void removeFromProfessors(Professor professor) {
        professors.removeIf(e -> e.getId().equals(professor.getId()));
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(List<Professor> professors) {
        this.professors = professors;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public Professor getDean() {
        return dean;
    }

    public void setDean(Professor dean) {
        this.dean = dean;
    }

    public Professor getDeputy() {
        return deputy;
    }

    public void setDeputy(Professor deputy) {
        this.deputy = deputy;
    }

    public DepartmentName getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(DepartmentName departmentName) {
        this.departmentName = departmentName;
    }
}
