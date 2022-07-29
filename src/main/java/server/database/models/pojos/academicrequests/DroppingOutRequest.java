package server.database.models.pojos.academicrequests;

import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.Entity;

@Entity
@DiscriminatorOptions(force = true)
public class DroppingOutRequest extends AcademicRequest {
}
