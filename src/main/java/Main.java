import server.database.management.DatabaseManager;
import test.Course;
import test.Score;
import test.Transcript;

import java.util.LinkedHashMap;

public class Main {
    public static void main(String[] args) {
        DatabaseManager manager = new DatabaseManager();

        Course firstCourse = new Course();
        firstCourse.setId(213);
        Course secondCourse = new Course();
        secondCourse.setId(110);
        Transcript transcript = new Transcript();
        LinkedHashMap<Course, Score> map = new LinkedHashMap<>();
        Score firstScore = new Score();
        firstScore.setScore(19);
        Score secondScore = new Score();
        secondScore.setScore(20);
        map.put(firstCourse, firstScore);
        map.put(secondCourse, secondScore);
        transcript.setMap(map);
        manager.save(firstCourse);
        manager.save(secondCourse);
        manager.save(transcript);
        manager.save(firstScore);
        manager.save(secondScore);
    }
}
