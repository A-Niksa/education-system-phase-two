package server.database.models.pojos.users.professors;

import org.hibernate.annotations.DiscriminatorOptions;
import server.database.models.pojos.users.User;
import server.database.models.pojos.users.UserIdentifier;
import server.database.models.pojos.academicrequests.AcademicRequest;
import server.database.models.pojos.users.students.Student;
import server.database.models.idgeneration.IdGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorOptions(force = true)
public class Professor extends User {
    private static int sequentialId = 0;

    @OneToMany(mappedBy = "advisingProfessor", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Student> studentsUnderAdvice;
    @OneToMany(mappedBy = "receivingProfessor", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<AcademicRequest> receivedRequests;
    @Column
    private String roomNumber;
    @Column
    private AcademicLevel academicLevel;
    @Column
    private AcademicRole academicRole;

    public Professor() {
        super(UserIdentifier.PROFESSOR);
        academicRole = AcademicRole.NORMAL; // default value
        studentsUnderAdvice = new ArrayList<>();
        receivedRequests = new ArrayList<>();
    }

    public void addToStudentsUnderAdvice(Student student) {
        studentsUnderAdvice.add(student);
    }

    public void removeFromStudentsUnderAdvice(Student student) {
        studentsUnderAdvice.removeIf(e -> e.getId().equals(student.getId()));
    }

    public void addToReceivedRequests(AcademicRequest request) {
        receivedRequests.add(request);
    }

    public void removeFromReceivedRequests(AcademicRequest request) {
        receivedRequests.removeIf(e -> e.getId().equals(request.getId()));
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

    public List<AcademicRequest> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(List<AcademicRequest> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }
}
