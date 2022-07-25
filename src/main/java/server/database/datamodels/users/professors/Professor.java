package server.database.datamodels.users.professors;

import server.database.datamodels.requests.Request;
import server.database.datamodels.users.students.Student;
import server.database.datamodels.users.User;
import server.database.idgeneration.IdGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Professor extends User {
    private static int sequentialId = 0;

    @OneToMany(mappedBy = "advisingProfessor", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Student> studentsUnderAdvice;
    // TODO:
//    @OneToMany(mappedBy = "receivingProfessor", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
//    private List<Request> receivedRequests;
    @Column
    private String roomNumber;
    @Column
    private AcademicLevel academicLevel;
    @Column
    private AcademicRole academicRole;

    public Professor() {
        academicRole = AcademicRole.NORMAL; // default value
        studentsUnderAdvice = new ArrayList<>();
//        receivedRequests = new ArrayList<>();
    }

    public void addToStudentsUnderAdvice(Student student) {
        studentsUnderAdvice.add(student);
    }

    public void removeFromStudentsUnderAdvice(Student student) {
        studentsUnderAdvice.removeIf(e -> e.getId().equals(student.getId()));
    }

//    public void addToReceivedRequests(Request request) {
//        receivedRequests.add(request);
//    }
//
//    public void removeFromReceivedRequests(Request request) {
//        receivedRequests.removeIf(e -> e.getId().equals(request.getId()));
//    }

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

//    public List<Request> getReceivedRequests() {
//        return receivedRequests;
//    }
//
//    public void setReceivedRequests(List<Request> receivedRequests) {
//        this.receivedRequests = receivedRequests;
//    }
}
