package shareables.models.pojos.abstractions;

import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.Student;

import java.util.ArrayList;
import java.util.List;

public class Department extends Identifiable {
    private String id;
    private List<Professor> professors;
    private List<Student> students;
    private Professor dean;
    private Professor deputy;
    private DepartmentName departmentName;

    public Department() {

    }

    public Department(DepartmentName departmentName) {
        this.departmentName = departmentName;
        professors = new ArrayList<>();
        students = new ArrayList<>();
        initializeId();
    }

    public void addToProfessors(Professor professor) {
        professors.add(professor);
    }

    public void removeFromProfessors(String professorId) {
        professors.removeIf(e -> e.getId().equals(professorId));
    }

    public void addToStudents(Student student) {
        students.add(student);
    }

    public void removeFromStudents(String studentId) {
        students.removeIf(e -> e.getId().equals(studentId));
    }

    @Override
    protected void initializeId() {
        id = idGenerator.nextId(this);
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
