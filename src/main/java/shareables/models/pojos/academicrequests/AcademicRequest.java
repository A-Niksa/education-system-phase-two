package shareables.models.pojos.academicrequests;

import shareables.models.idgeneration.IdentifiableWithTime;
import shareables.models.idgeneration.SequentialIdGenerator;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.Student;

public abstract class AcademicRequest extends IdentifiableWithTime {
    private static SequentialIdGenerator sequentialIdGenerator;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator();
    }

    protected AcademicRequestIdentifier requestIdentifier;
    protected Student requestingStudent;
    protected Professor receivingProfessor;
    protected AcademicRequestStatus academicRequestStatus;

    public AcademicRequest(AcademicRequestIdentifier requestIdentifier) {
        this.requestIdentifier = requestIdentifier;
        academicRequestStatus = AcademicRequestStatus.SUBMITTED; // default value
        initializeId();
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
}