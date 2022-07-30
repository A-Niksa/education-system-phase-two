package shareables.models.pojos.academicrequests;

import java.util.Date;

public class DefenseRequest extends AcademicRequest {
    private Date requestedDefenseDate;

    public DefenseRequest() {
        super(AcademicRequestIdentifier.DEFENSE);
    }

    public Date getRequestedDefenseDate() {
        return requestedDefenseDate;
    }

    public void setRequestedDefenseDate(Date requestedDefenseDate) {
        this.requestedDefenseDate = requestedDefenseDate;
    }
}
