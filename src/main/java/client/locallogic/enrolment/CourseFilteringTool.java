package client.locallogic.enrolment;

import shareables.network.DTOs.CourseDTO;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CourseFilteringTool {
    public static ArrayList<CourseDTO> getFilteredCourseDTOs(FilterKey filterKey, String filterTarget,
                                                             ArrayList<CourseDTO> allCourseDTOs) {
        ArrayList<CourseDTO> filteredCourseDTOs = new ArrayList<>();
        switch (filterKey) {
            case COURSE_ID:
                filteredCourseDTOs = getIdFilteredCourseDTOs(filterTarget, allCourseDTOs);
                break;
            case NUMBER_OF_CREDITS:
                filteredCourseDTOs = getCreditFilteredCourseDTOs(Integer.parseInt(filterTarget), allCourseDTOs);
                break;
            case COURSE_LEVEL:
                filteredCourseDTOs = getLevelFilteredCourseDTOs(filterTarget, allCourseDTOs);
                break;
        }
        return filteredCourseDTOs;
    }

    private static ArrayList<CourseDTO> getIdFilteredCourseDTOs(String courseId, ArrayList<CourseDTO> allCourseDTOs) {
        return allCourseDTOs.stream()
                .filter(e -> e.getId().equals(courseId))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static ArrayList<CourseDTO> getCreditFilteredCourseDTOs(int numberOfCredits, ArrayList<CourseDTO> allCourseDTOs) {
        return allCourseDTOs.stream()
                .filter(e -> e.getNumberOfCredits() == numberOfCredits)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static ArrayList<CourseDTO> getLevelFilteredCourseDTOs(String courseLevel, ArrayList<CourseDTO> allCourseDTOs) {
        return allCourseDTOs.stream()
                .filter(e -> e.getCourseLevel()
                        .toString()
                        .equals(courseLevel))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
