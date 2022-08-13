package shareables.network.DTOs.academicrequests;

import shareables.models.pojos.academicrequests.AcademicRequestStatus;

public class MinorRequestDTO extends RequestDTO {
    private String originDepartmentId;
    private String targetDepartmentId;
    private AcademicRequestStatus academicRequestStatus;

    public MinorRequestDTO() {
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

    public AcademicRequestStatus getAcademicRequestStatus() {
        return academicRequestStatus;
    }

    public void setAcademicRequestStatus(AcademicRequestStatus academicRequestStatus) {
        this.academicRequestStatus = academicRequestStatus;
    }
}
