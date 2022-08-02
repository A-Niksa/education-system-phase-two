package shareables.models.pojos.academicrequests;

import server.database.datasets.DatasetIdentifier;
import shareables.models.idgeneration.SequentialIdGenerator;
import shareables.models.pojos.users.professors.Professor;

public class MinorRequest extends AcademicRequest {
    private static SequentialIdGenerator sequentialIdGenerator;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator(DatasetIdentifier.MINOR_REQUESTS);
    }

    private Professor originDeputy; // deputy of the student's department
    private Professor targetDeputy; // deputy of the student's desired department
    private AcademicRequestStatus academicRequestStatusAtOrigin;
    private AcademicRequestStatus academicRequestStatusAtTarget;

    public MinorRequest() {
        super(AcademicRequestIdentifier.MINOR);
        initializeId(sequentialIdGenerator);
        academicRequestStatusAtOrigin = AcademicRequestStatus.SUBMITTED; // default value
        academicRequestStatusAtTarget = AcademicRequestStatus.SUBMITTED; // default value
    }

    public Professor getOriginDeputy() {
        return originDeputy;
    }

    public void setOriginDeputy(Professor originDeputy) {
        this.originDeputy = originDeputy;
    }

    public Professor getTargetDeputy() {
        return targetDeputy;
    }

    public void setTargetDeputy(Professor targetDeputy) {
        this.targetDeputy = targetDeputy;
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
