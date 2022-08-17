package server.network.clienthandling.logicutils.unitselection.pinning;

import server.network.clienthandling.logicutils.unitselection.acquisition.CourseAcquisitionUtils;
import shareables.models.pojos.unitselection.StudentSelectionLog;
import shareables.models.pojos.unitselection.UnitSelectionSession;

public class FavoriteCoursesUtils {
    public static void pinCourseToFavorites(String courseId, String studentId, UnitSelectionSession unitSelectionSession) {
        StudentSelectionLog studentSelectionLog = CourseAcquisitionUtils.getStudentSelectionLog(studentId,
                unitSelectionSession);

        studentSelectionLog.addToFavoriteCourseIds(courseId);
    }

    public static void unpinCourseFromFavorites(String courseId, String studentId, UnitSelectionSession unitSelectionSession) {
        StudentSelectionLog studentSelectionLog = CourseAcquisitionUtils.getStudentSelectionLog(studentId,
                unitSelectionSession);

        studentSelectionLog.removeFromFavoriteCourseIds(courseId);
    }
}
