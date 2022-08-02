package server.network.clienthandling.logicutils;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.abstractions.TermIdentifier;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.network.requests.Request;
import shareables.utils.timing.timekeeping.WeekTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CourseAdditionUtils {
    public static String addCourseAndReturnId(DatabaseManager databaseManager, Request request) {
        Course course = new Course((String) request.get("departmentId"), (TermIdentifier) request.get("termIdentifier"));
        List<Professor> teachingProfessors = IdentifiableEditingUtils.getProfessorsByNames(databaseManager,
                (String[]) request.get("teachingProfessorNames"), (String) request.get("departmentId"));
        course.setTeachingProfessors(teachingProfessors);
        course.setCourseName((String) request.get("courseName"));
        course.setDegreeLevel((DegreeLevel) request.get("degreeLevel"));
        course.setNumberOfCredits((int) request.get("numberOfCredits"));
        course.setWeeklyClassTimes((ArrayList<WeekTime>) request.get("weeklyClassTimes"));
        course.setExamDate((LocalDateTime) request.get("examDate"));
        course.setTermIdentifier((TermIdentifier) request.get("termIdentifier"));
        databaseManager.save(DatasetIdentifier.COURSES, course);
        return course.getId();
    }
}