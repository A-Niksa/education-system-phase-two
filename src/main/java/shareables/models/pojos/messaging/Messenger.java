package shareables.models.pojos.messaging;

import javax.persistence.*;

@Entity
@Table(name = "Messengers")
public class Messenger { // note that this class is a pojo and has no direct messaging capability
    @Id
    private String id;
}
