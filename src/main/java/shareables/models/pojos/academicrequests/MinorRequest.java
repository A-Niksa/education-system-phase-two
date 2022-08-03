package shareables.models.pojos.academicrequests;

import server.database.datasets.DatasetIdentifier;
import shareables.models.idgeneration.SequentialIdGenerator;

public class MinorRequest extends AcademicRequest {
    private static SequentialIdGenerator sequentialIdGenerator;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator(DatasetIdentifier.MINOR_REQUESTS);
    }

    private String originDepartmentId; // deputy of the student's department
    private String targetDepartmentId; // deputy of the student's desired department
    private AcademicRequestStatus academicRequestStatusAtOrigin;
    private AcademicRequestStatus academicRequestStatusAtTarget;

    public MinorRequest() {
        super(AcademicRequestIdentifier.MINOR);
        initializeId(sequentialIdGenerator);
        academicRequestStatusAtOrigin = AcademicRequestStatus.SUBMITTED; // default value
        academicRequestStatusAtTarget = AcademicRequestStatus.SUBMITTED; // default value
    }

    public String getOriginDepartmentId() {
        return originDepartmentId;
    }

    public void setOriginDepartmentId(String originDepartmentId) {
        this.originDepartmentId = originDepartmentId;
    }

    public String getTargetDepartmentId() {
        return targetDepartmentId;
    }

    public void setTargetDepartmentId(String targetDepartmentId) {
        this.targetDepartmentId = targetDepartmentId;
    }

    public AcademicRequestStatus getRequestStatusAtOrigin() {
        return academicRequestStatusAtOrigin;
    }

    public void setRequestStatusAtOrigin(AcademicRequestStatus academicRequestStatusAtOrigin) {
        this.academicRequestStatusAtOrigin = academicRequestStatusAtOrigin;
    }

    public AcademicRequestStatus getRequestStatusAtTarget() {
        return academicRequestStatusAtTarget;
    }

    public void setRequestStatusAtTarget(AcademicRequestStatus academicRequestStatusAtTarget) {
        this.academicRequestStatusAtTarget = academicRequestStatusAtTarget;
    }
}
