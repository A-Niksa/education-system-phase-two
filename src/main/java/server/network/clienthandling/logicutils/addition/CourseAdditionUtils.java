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
import java.util.List;

public class CourseAdditionUtils {
    public static String addCourseAndReturnId(DatabaseManager databaseManager, Request request) {
        Course course = new Course((String) request.get("departmentId"), (TermIdentifier) request.get("termIdentifier"));
        List<String> teachingProfessorIds = CourseEditingUtils.getProfessorIdsByNames(databaseManager,
                (String[]) request.get("teachingProfessorNames"), (String) request.get("departmentId"));
        course.setTeachingProfessorIds(teachingProfessorIds);
        course.setCourseName((String) request.get("courseName"));
        course.setDegreeLevel((DegreeLevel) request.get("degreeLevel"));
        course.setNumberOfCredits((int) request.get("numberOfCredits"));
        course.setWeeklyClassTimes((ArrayList<WeekTime>) request.get("weeklyClassTimes"));
        course.setExamDate((LocalDateTime) request.get("examDate"));
        course.setTermIdentifier((TermIdentifier) request.get("termIdentifier"));
        databaseManager.save(DatasetIdentifier.COURSES, course);

        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, (String) request.get("departmentId"));
        department.addToCourseIDs(course.getId());

        return course.getId();
    }
}
