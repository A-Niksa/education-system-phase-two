package server.network.clienthandling.logicutils.coursewares;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.coursewares.CoursewareManager;
import shareables.models.pojos.coursewares.EducationalItem;
import shareables.models.pojos.coursewares.EducationalMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MaterialItemUtils {
    public static void addEducationalMaterialItems(DatabaseManager databaseManager, String courseId, String materialTitle,
                                                   List<EducationalItem> educationalItems) {
        EducationalMaterial educationalMaterial = getNewEducationalMaterial(materialTitle, educationalItems);

        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);
        course.getCoursewareManager().addToEducationalMaterials(educationalMaterial);
    }

    public static void editEducationalMaterialItems(DatabaseManager databaseManager, String courseId, String materialId,
                                                    List<EducationalItem> educationalItems) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);

        EducationalMaterial educationalMaterial = getEducationalMaterial(course.getCoursewareManager(), materialId);
        educationalMaterial.setEducationalItems(educationalItems);
    }

    private static EducationalMaterial getEducationalMaterial(CoursewareManager coursewareManager, String materialId) {
        return coursewareManager.getEducationalMaterials().stream()
                .filter(material -> material.getId().equals(materialId))
                .findAny().orElse(null);
    }

    private static EducationalMaterial getNewEducationalMaterial(String materialTitle, List<EducationalItem> educationalItems) {
        EducationalMaterial educationalMaterial = new EducationalMaterial();
        educationalMaterial.setTitle(materialTitle);
        educationalMaterial.setEducationalItems(educationalItems);
        return educationalMaterial;
    }

    public static List<EducationalItem> getEducationalItems(DatabaseManager databaseManager, String courseId, String materialId) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);
        List<EducationalMaterial> educationalMaterials = course.getCoursewareManager().getEducationalMaterials();
        return educationalMaterials.stream()
                .filter(material -> material.getId().equals(materialId))
                .flatMap(material -> material.getEducationalItems().stream())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void removeCourseEducationalMaterial(DatabaseManager databaseManager, String courseId, String materialId) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);
        course.getCoursewareManager().removeFromEducationalMaterials(materialId);
    }

    public static void removeAllCourseEducationalMaterials(DatabaseManager databaseManager, String courseId) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);
        course.getCoursewareManager().getEducationalMaterials().clear();
    }
}
