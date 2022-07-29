package server.database.datamodels.academicrequests;

import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorOptions(force = true)
public class DefenseRequest extends AcademicRequest {
    @Column
    private Date requestedDefenseDate;

    public Date getRequestedDefenseDate() {
        return requestedDefenseDate;
    }

    public void setRequestedDefenseDate(Date requestedDefenseDate) {
        this.requestedDefenseDate = requestedDefenseDate;
    }
}
