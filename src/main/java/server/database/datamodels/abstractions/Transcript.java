package server.database.datamodels.abstractions;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name = "TRANSCRIPTS")
public class Transcript {
    @Id
    private String id; // same as studentId
    @ManyToMany(cascade = CascadeType.PERSIST)
    private ArrayList<Course> takenCourses; // includes failed courses as well
}
