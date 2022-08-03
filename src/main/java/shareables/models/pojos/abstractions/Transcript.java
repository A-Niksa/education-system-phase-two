package shareables.models.pojos.abstractions;

import java.util.LinkedHashMap;
import java.util.Map;

public class Transcript {
    private Map<String, Score> courseIdScoreMap; // includes failed courses as well

    public Transcript() {
        courseIdScoreMap = new LinkedHashMap<>();
    }

    public void put(String courseId, Score score) {
        courseIdScoreMap.put(courseId, score);
    }

    public void remove(String courseId) {
        courseIdScoreMap.entrySet().removeIf(e -> e.getKey().equals(courseId));
    }

    public double fetchGPA() {
        double GPA = -1.0; // -1.0 is equivalent to N/A
        return GPA;
        // TODO: calculating GPA
    }

    public String fetchGPAString() {
        double GPA = fetchGPA();
        return GPA == -1.0 ? "N/A" : String.valueOf(GPA);
    }

    public Map<String, Score> getCourseIdScoreMap() {
        return courseIdScoreMap;
    }

    public void setCourseIdScoreMap(Map<String, Score> courseIdScoreMap) {
        this.courseIdScoreMap = courseIdScoreMap;
    }
}