package test;

import javax.persistence.*;
import java.util.Map;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Transcript extends ParentTranscript {
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(inverseJoinColumns=@JoinColumn(name="score"))
    @MapKeyJoinColumn(name="course_id")
    private Map<Course, Score> map;

    public Map<Course, Score> getMap() {
        return map;
    }

    public void setMap(Map<Course, Score> map) {
        this.map = map;
    }
}
