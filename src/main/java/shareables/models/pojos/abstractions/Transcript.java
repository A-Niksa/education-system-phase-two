package shareables.models.pojos.abstractions;

import java.util.LinkedHashMap;
import java.util.Map;

public class Transcript {
    private Map<String, Score> courseIdScoreMap; // includes failed courses as well
    private double GPA;

    public Transcript() {
        courseIdScoreMap = new LinkedHashMap<>();
        GPA = -1.0; // -1.0 is equivalent to N/A
    }

    public void put(String courseId, Score score) {
        courseIdScoreMap.put(courseId, score);
    }

    public void remove(String courseId) {
        courseIdScoreMap.entrySet().removeIf(e -> e.getKey().equals(courseId));
    }

    public double getGPA() {
        return GPA;
        // TODO: calculating GPA
    }

    public void setGPA(double GPA) {
        this.GPA = GPA;
    }

    public String fetchGPAString() {
        double GPA = getGPA();
        return GPA == -1.0 ? "N/A" : String.format("%.2f", GPA);
    }

    public Map<String, Score> getCourseIdScoreMap() {
        return courseIdScoreMap;
    }

    public void setCourseIdScoreMap(Map<String, Score> courseIdScoreMap) {
        this.courseIdScoreMap = courseIdScoreMap;
    }
}