package shareables.models.pojos.abstractions;

import java.util.LinkedHashMap;
import java.util.Map;

public class Transcript {
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

    private double fetchGPA() {
        double GPA = -1.0; // -1.0 is equivalent to N/A
        return GPA;
        // TODO: calculating GPA
    }

    public String fetchGPAString() {
        double GPA = fetchGPA();
        return GPA == -1.0 ? "N/A" : String.valueOf(GPA);
    }

    public Map<Course, Score> getCourseScoreMap() {
        return courseScoreMap;
    }

    public void setCourseScoreMap(Map<Course, Score> courseScoreMap) {
        this.courseScoreMap = courseScoreMap;
    }
}