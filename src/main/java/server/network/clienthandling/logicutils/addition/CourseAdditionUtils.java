package server.network.clienthandling.logicutils.addition;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.enrolment.CourseEditingUtils;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.abstractions.TermIdentifier;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.network.requests.Request;
import shareables.utils.timing.timekeeping.WeekTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourseAdditionUtils {
    public static String addCourseAndReturnId(DatabaseManager databaseManager, Request request) {
        Course course = new Course((String) request.get("departmentId"), (TermIdentifier) request.get("termIdentifier"));

        List<String> prerequisiteIds = Arrays.asList((String[]) request.get("prerequisiteIds"));
        List<String> corequisiteIds = Arrays.asList((String[]) request.get("corequisiteIds"));

        List<String> teachingProfessorIds = CourseEditingUtils.getDepartmentProfessorIdsByNames(databaseManager,
                (String[]) request.get("teachingProfessorNames"), (String) request.get("departmentId"));
        List<String> teachingAssistantIds = Arrays.asList((String[]) request.get("teachingAssistantIds"));

        course.setPrerequisiteIds(prerequisiteIds);
        course.setCorequisiteIds(corequisiteIds);
        course.setTeachingProfessorIds(teachingProfessorIds);
        course.setTeachingAssistantIds(teachingAssistantIds);
        course.setCourseName((String) request.get("courseName"));
        course.setDegreeLevel((DegreeLevel) request.get("degreeLevel"));
        course.setNumberOfCredits((int) request.get("numberOfCredits"));
        course.setCourseCapacity((int) request.get("courseCapacity"));
        course.setWeeklyClassTimes((ArrayList<WeekTime>) request.get("weeklyClassTimes"));
        course.setExamDate((LocalDateTime) request.get("examDate"));
        course.setTermIdentifier((TermIdentifier) request.get("termIdentifier"));

        String givenCourseId = (String) request.get("courseId");
        if (!givenCourseId.equals("")) {
            course.setId(givenCourseId);
        }

        databaseManager.save(DatasetIdentifier.COURSES, course);

        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, (String) request.get("departmentId"));
        department.addToCourseIDs(course.getId());

        return course.getId();
    }
}
