package server.database.datamodels.requests;

import server.database.datamodels.users.professors.Professor;

import javax.persistence.*;

@Entity
public class MinorRequest extends Request {
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "origin_deputy_id")
    private Professor originDeputy;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_deputy_id")
    private Professor targetDeputy;
    @Column
    private RequestStatus requestStatusAtOrigin;
    @Column
    private RequestStatus requestStatusAtTarget;

    public MinorRequest() {
        requestStatusAtOrigin = RequestStatus.SUBMITTED; // default value
        requestStatusAtTarget = RequestStatus.SUBMITTED; // default value
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

    public RequestStatus getRequestStatusAtOrigin() {
        return requestStatusAtOrigin;
    }

    public void setRequestStatusAtOrigin(RequestStatus requestStatusAtOrigin) {
        this.requestStatusAtOrigin = requestStatusAtOrigin;
    }

    public RequestStatus getRequestStatusAtTarget() {
        return requestStatusAtTarget;
    }

    public void setRequestStatusAtTarget(RequestStatus requestStatusAtTarget) {
        this.requestStatusAtTarget = requestStatusAtTarget;
    }
}
