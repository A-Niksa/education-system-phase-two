package server.database.datamodels.abstractions;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Table(name = "Transcripts")
public class Transcript {
    @Id
    private String id; // same as studentId
    @ManyToMany(cascade = CascadeType.PERSIST)
    @MapKeyJoinColumn(name="course_id")
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "score"))
    private Map<Course, Score> courseScoreMap; // includes failed courses as well

    public Transcript() {
        courseScoreMap = new LinkedHashMap<>();
    }

    public void put(Course course, Score score) {
        courseScoreMap.put(course, score);
    }

    public void remove(Course course) {
        courseScoreMap.entrySet().removeIf(e -> e.getKey().getId().equals(course.getId()));
    }

    public double getGPA() {
        double GPA = -1.0; // -1.0 is equivalent to N/A
        return GPA;
        // TODO: calculating GPA
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<Course, Score> getCourseScoreMap() {
        return courseScoreMap;
    }

    public void setCourseScoreMap(Map<Course, Score> courseScoreMap) {
        this.courseScoreMap = courseScoreMap;
    }
}