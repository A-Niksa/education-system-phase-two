package server.network.clienthandling.logicutils.coursewares;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.coursewares.EducationalItem;
import shareables.models.pojos.coursewares.EducationalMaterial;
import shareables.models.pojos.media.MediaFile;

public class CoursewareDownloadUtils {
    public static MediaFile getMediaFileFromEducationalMaterial(DatabaseManager databaseManager, String courseId,
                                                                String materialId, String itemId) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);

        EducationalMaterial educationalMaterial = MaterialItemUtils.getEducationalMaterial(course.getCoursewareManager(),
                materialId);
        EducationalItem educationalItem = getEducationalItem(educationalMaterial, itemId);

        return educationalItem.getMediaFile();
    }

    private static EducationalItem getEducationalItem(EducationalMaterial educationalMaterial, String itemId) {
        return educationalMaterial.getEducationalItems().stream()
                .filter(material -> material.getId().equals(itemId))
                .findAny().orElse(null);
    }
}
