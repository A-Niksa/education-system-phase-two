package shareables.models.pojos.coursewares;

import java.util.ArrayList;
import java.util.List;

public class CoursewareManager {
    private String courseId;
    private List<EducationalMaterial> educationalMaterials;
    private List<Homework> homeworks;

    public CoursewareManager() {
        educationalMaterials = new ArrayList<>();
        homeworks = new ArrayList<>();
    }

    public CoursewareManager(String courseId) {
        this();
        this.courseId = courseId;
    }

    public void addToEducationalMaterials(EducationalMaterial educationalMaterial) {
        educationalMaterials.add(educationalMaterial);
    }

    public void removeFromEducationalMaterials(String educationalMaterialId) {
        educationalMaterials.removeIf(e -> e.getId().equals(educationalMaterialId));
    }

    public void addToHomeworks(Homework homework) {
        homeworks.add(homework);
    }

    public void removeFromHomeworks(String homeworkId) {
        homeworks.removeIf(e -> e.getId().equals(homeworkId));
    }

    public String getCourseId() {
        return courseId;
    }

    public List<EducationalMaterial> getEducationalMaterials() {
        return educationalMaterials;
    }

    public List<Homework> getHomeworks() {
        return homeworks;
    }
}
