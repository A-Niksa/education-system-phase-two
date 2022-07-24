package server.database.datamodels.requests;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
public class DefenseRequest extends Request {
    @Column
    private Date requestedDefenseDate;

    public Date getRequestedDefenseDate() {
        return requestedDefenseDate;
    }

    public void setRequestedDefenseDate(Date requestedDefenseDate) {
        this.requestedDefenseDate = requestedDefenseDate;
    }
}
