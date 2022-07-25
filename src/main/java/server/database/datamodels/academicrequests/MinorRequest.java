package server.database.datamodels.academicrequests;

import server.database.datamodels.users.professors.Professor;

import javax.persistence.*;

@Entity
public class MinorRequest extends AcademicRequest {
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "originDeputy_id")
    private Professor originDeputy; // deputy of the student's department
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "target_deputy_id")
    private Professor targetDeputy; // deputy of the student's desired department
    @Column
    private AcademicRequestStatus academicRequestStatusAtOrigin;
    @Column
    private AcademicRequestStatus academicRequestStatusAtTarget;

    public MinorRequest() {
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
