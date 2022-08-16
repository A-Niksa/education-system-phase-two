package shareables.models.pojos.unitselection;

import shareables.models.idgeneration.IdentifiableWithTime;

import java.util.ArrayList;
import java.util.List;

public class StudentSelectionLog extends IdentifiableWithTime {
    private String studentId;
    private List<String> favoriteCoursesIds;
    private List<String> acquiredCoursesIds;

    public StudentSelectionLog() {
        favoriteCoursesIds = new ArrayList<>();
        acquiredCoursesIds = new ArrayList<>();
        initializeId();
    }

    public void addToFavoriteCourseIds(String courseId) {
        favoriteCoursesIds.add(courseId);
    }

    public void removeFromFavoriteCourseIds(String courseId) {
        favoriteCoursesIds.removeIf(e -> e.equals(courseId));
    }

    public void addToAcquiredCourseIds(String courseId) {
        acquiredCoursesIds.add(courseId);
    }

    public void removeFromAcquiredCourseIds(String courseId) {
        acquiredCoursesIds.removeIf(e -> e.equals(courseId));
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public List<String> getFavoriteCoursesIds() {
        return favoriteCoursesIds;
    }

    public void setFavoriteCoursesIds(List<String> favoriteCoursesIds) {
        this.favoriteCoursesIds = favoriteCoursesIds;
    }

    public List<String> getAcquiredCoursesIds() {
        return acquiredCoursesIds;
    }

    public void setAcquiredCoursesIds(List<String> acquiredCoursesIds) {
        this.acquiredCoursesIds = acquiredCoursesIds;
    }
}
