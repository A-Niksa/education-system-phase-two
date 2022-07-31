package shareables.models.pojos.abstractions;

import shareables.models.idgeneration.Identifiable;
import shareables.models.idgeneration.SequentialIdGenerator;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.Student;

import java.util.ArrayList;
import java.util.List;

public class Course extends Identifiable {
    private static SequentialIdGenerator sequentialIdGenerator;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator();
    }

    private String departmentId;
    private List<Professor> teachingProfessors;
    private List<Student> TAs;
    private List<Student> students;
    private String termIdentifier; // such as "20182" or "20201"
    private int numberOfCredits;
    private boolean isActive; // shows whether the course is being currently taught

    public Course() {

    }

    public Course(String departmentId) {
        this.departmentId = departmentId;
        isActive = false; // default value
        teachingProfessors = new ArrayList<>();
        TAs = new ArrayList<>();
        students = new ArrayList<>();
        initializeId();
    }

    public void addToTeachingProfessors(Professor professor) {
        teachingProfessors.add(professor);
    }

    public void removeFromTeachingProfessors(String professorId) {
        teachingProfessors.removeIf(e -> e.getId().equals(professorId));
    }

    public void addToTAs(Student student) {
        TAs.add(student);
    }

    public void removeFromTAs(String studentId) {
        TAs.removeIf(e -> e.getId().equals(studentId));
    }

    public void addToStudents(Student student) {
        students.add(student);
    }

    public void removeFromStudents(String studentId) {
        students.removeIf(e -> e.getId().equals(studentId));
    }

    @Override
    protected void initializeId() {
        id = idGenerator.nextId(this, sequentialIdGenerator);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTermIdentifier() {
        return termIdentifier;
    }

    public void setTermIdentifier(String termIdentifier) {
        this.termIdentifier = termIdentifier;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
}