package server.database.datamodels.users;

import server.database.idgeneration.IdGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Professor extends User {
    private static int sequentialId = 0;

    @OneToMany(mappedBy = "advisingProfessor", cascade = CascadeType.PERSIST)
    @JoinColumn(name = "student_under_advice_id")
    private List<Student> studentsUnderAdvice;
    @Column
    private String roomNumber;
    @Column
    private AcademicLevel academicLevel;
    @Column
    private AcademicRole academicRole;

    public Professor() {
        academicRole = AcademicRole.NORMAL; // default value
        studentsUnderAdvice = new ArrayList<>();
    }

    public void addToStudentsUnderAdvice(Student student) {
        studentsUnderAdvice.add(student);
    }

    public void removeFromStudentsUnderAdvice(Student student) {
        studentsUnderAdvice.removeIf(e -> e.getId().equals(student.getId()));
    }

    @Override
    public void initializeId() {
        id = IdGenerator.generateId(this);
    }

    public static int getSequentialId() {
        return sequentialId;
    }

    public static void incrementSequentialId() {
        sequentialId++;
    }

    public List<Student> getStudentsUnderAdvice() {
        return studentsUnderAdvice;
    }

    public void setStudentsUnderAdvice(List<Student> studentsUnderAdvice) {
        this.studentsUnderAdvice = studentsUnderAdvice;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
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
