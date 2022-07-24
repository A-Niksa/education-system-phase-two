package server.database.datamodels.media;

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
