package server.database.datamodels.academicrequests;

import org.hibernate.annotations.DiscriminatorOptions;
import server.database.datamodels.users.professors.Professor;
import server.database.datamodels.users.students.Student;
import server.database.idgeneration.IdGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@DiscriminatorOptions(force = true)
@Table(name = "AcademicRequests")
public abstract class AcademicRequest {
    @Id
    protected String id;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "requestingStudent_id")
    protected Student requestingStudent;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "receivingProfessor_id")
    protected Professor receivingProfessor;
    @Column
    protected AcademicRequestStatus academicRequestStatus;
    @Column
    protected Date requestDate;

    public AcademicRequest() {
        academicRequestStatus = AcademicRequestStatus.SUBMITTED; // default value
        requestDate = new Date(); // date at the time of request construction
        initializeId();
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

    public Student getRequestingStudent() {
        return requestingStudent;
    }

    public void setRequestingStudent(Student requestingStudent) {
        this.requestingStudent = requestingStudent;
    }

    public Professor getReceivingProfessor() {
        return receivingProfessor;
    }

    public void setReceivingProfessor(Professor receivingProfessor) {
        this.receivingProfessor = receivingProfessor;
    }

    public AcademicRequestStatus getRequestStatus() {
        return academicRequestStatus;
    }

    public void setRequestStatus(AcademicRequestStatus academicRequestStatus) {
        this.academicRequestStatus = academicRequestStatus;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }
}