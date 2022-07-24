package test;

import javax.persistence.*;

@Entity
public abstract class ParentTranscript {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    long id;
}
