package shareables.models.pojos.users.professors;

import shareables.models.idgeneration.SequentialIdGenerator;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.UserIdentifier;
import shareables.models.pojos.users.students.Student;

import java.util.ArrayList;
import java.util.List;

public class Professor extends User {
    private static SequentialIdGenerator sequentialIdGenerator;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator();
    }

    private List<Student> studentsUnderAdvice;
    private String officeNumber;
    private AcademicLevel academicLevel;
    private AcademicRole academicRole;

    public Professor() {
    }

    public Professor(AcademicRole academicRole, AcademicLevel academicLevel, String departmentId) {
        super(UserIdentifier.PROFESSOR);
        this.academicRole = academicRole;
        this.academicLevel = academicLevel;
        this.departmentId = departmentId;
        studentsUnderAdvice = new ArrayList<>();
        initializeId();
        initializeMessenger(id);
    }

    public void addToStudentsUnderAdvice(Student student) {
        studentsUnderAdvice.add(student);
    }

    public void removeFromStudentsUnderAdvice(String studentId) {
        studentsUnderAdvice.removeIf(e -> e.getId().equals(studentId));
    }

    @Override
    protected void initializeId() {
        id = idGenerator.nextId(this, sequentialIdGenerator);
    }

    public List<Student> getStudentsUnderAdvice() {
        return studentsUnderAdvice;
    }

    public void setStudentsUnderAdvice(List<Student> studentsUnderAdvice) {
        this.studentsUnderAdvice = studentsUnderAdvice;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    public AcademicLevel getAcademicLevel() {
        return academicLevel;
    }

    public void setAcademicLevel(AcademicLevel academicLevel) {
        this.academicLevel = academicLevel;
    }

    public AcademicRole getAcademicRole() {
        return academicRole;
    }

    public void setAcademicRole(AcademicRole academicRole) {
        this.academicRole = academicRole;
    }
}
