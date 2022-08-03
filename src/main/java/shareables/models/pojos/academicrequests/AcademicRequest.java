package shareables.models.pojos.academicrequests;

import shareables.models.idgeneration.IdentifiableWithTime;
import shareables.models.idgeneration.SequentialIdGenerator;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.Student;

public abstract class AcademicRequest extends IdentifiableWithTime {
    protected AcademicRequestIdentifier requestIdentifier;
    protected String requestingStudentId;
    protected String receivingProfessorId;
    protected AcademicRequestStatus academicRequestStatus;

    public AcademicRequest(AcademicRequestIdentifier requestIdentifier) {
        this.requestIdentifier = requestIdentifier;
        academicRequestStatus = AcademicRequestStatus.SUBMITTED; // default value
    }

    public String getRequestingStudentId() {
        return requestingStudentId;
    }

    public void setRequestingStudentId(String requestingStudentId) {
        this.requestingStudentId = requestingStudentId;
    }

    public String getReceivingProfessorId() {
        return receivingProfessorId;
    }

    public void setReceivingProfessorId(String receivingProfessorId) {
        this.receivingProfessorId = receivingProfessorId;
    }

    public AcademicRequestStatus getRequestStatus() {
        return academicRequestStatus;
    }

    public void setRequestStatus(AcademicRequestStatus academicRequestStatus) {
        this.academicRequestStatus = academicRequestStatus;
    }
}