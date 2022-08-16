package client.locallogic.menus.unitselection;

import shareables.network.DTOs.unitselection.CourseThumbnailDTO;

import java.util.ArrayList;

public class CourseSelectionUtils {
    public static CourseThumbnailDTO getCourseThumbnailDTOWithId(String courseId,
                                                                 ArrayList<CourseThumbnailDTO> departmentCourseThumbnailDTOs) {
        return departmentCourseThumbnailDTOs.stream()
                .filter(thumbnailDTO -> thumbnailDTO.getCourseId().equals(courseId))
                .findAny().orElse(null);
    }
}
