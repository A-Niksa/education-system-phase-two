package client.locallogic.services;

import shareables.network.DTOs.CourseDTO;

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
