package server.network.clienthandling.logicutils.coursewares;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.comparators.MaterialThumbnailDTOComparator;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.coursewares.EducationalMaterial;
import shareables.network.DTOs.coursewares.MaterialThumbnailDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MaterialThumbnailUtils {
    private static MaterialThumbnailDTOComparator materialThumbnailDTOComparator;
    static {
        materialThumbnailDTOComparator = new MaterialThumbnailDTOComparator();
    }

    public static List<MaterialThumbnailDTO> getCourseMaterialThumbnailDTOs(DatabaseManager databaseManager, String courseId) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);

        return course.getCoursewareManager().getEducationalMaterials().stream()
                .map(MaterialThumbnailUtils::initializeMaterialThumbnailDTO)
                .sorted(materialThumbnailDTOComparator)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static MaterialThumbnailDTO initializeMaterialThumbnailDTO(EducationalMaterial educationalMaterial) {
        MaterialThumbnailDTO materialThumbnailDTO = new MaterialThumbnailDTO();
        materialThumbnailDTO.setId(educationalMaterial.getId());
        materialThumbnailDTO.setTitle(educationalMaterial.getTitle());
        materialThumbnailDTO.setUploadDate(educationalMaterial.getDate());
        return materialThumbnailDTO;
    }
}
