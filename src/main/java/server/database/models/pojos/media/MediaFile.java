package server.database.models.pojos.media;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MediaFiles")
public class MediaFile {
    @Id
    private String id;
    // TODO
}
