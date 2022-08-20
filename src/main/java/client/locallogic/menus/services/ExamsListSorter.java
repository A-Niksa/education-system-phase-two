package client.locallogic.menus.services;

import shareables.network.DTOs.generalmodels.CourseDTO;

import java.util.ArrayList;

public class ExamsListSorter {
    private static ExamDateComparator examDateComparator;
    static {
        examDateComparator = new ExamDateComparator();
    }

    public static ArrayList<CourseDTO> getSortedCourseDTOs(ArrayList<CourseDTO> courseDTOs) {
        courseDTOs.sort(examDateComparator);
        return courseDTOs;
    }
}
