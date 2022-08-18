package server.network.clienthandling.logicutils.addition;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.enrolment.CourseEditingUtils;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import server.network.clienthandling.logicutils.unitselection.acquisition.errorutils.SelectionErrorUtils;
import shareables.models.idgeneration.Identifiable;
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
import java.util.Objects;

public class CourseAdditionUtils {
    public static String addCourseAndReturnId(DatabaseManager databaseManager, Request request) {
        String departmentId = (String) request.get("departmentId");
        TermIdentifier termIdentifier = (TermIdentifier) request.get("termIdentifier");
        String courseId = (String) request.get("courseId");
        int groupNumber;
        if (courseId.equals("")) {
            groupNumber = 1;
        } else {
            groupNumber = getNumberOfCurrentCoursesWithId(databaseManager, courseId, termIdentifier) + 1;
        }

        Course course = new Course(departmentId, termIdentifier, groupNumber);

        String givenCourseId = (String) request.get("courseId");
        if (!givenCourseId.equals("")) {
            course.setId(givenCourseId + termIdentifier + "0" + groupNumber);
        }

        List<String> prerequisiteIds = Arrays.asList((String[]) request.get("prerequisiteIds"));
        List<String> corequisiteIds = Arrays.asList((String[]) request.get("corequisiteIds"));

        List<String> teachingProfessorIds = CourseEditingUtils.getDepartmentProfessorIdsByNames(databaseManager,
                (String[]) request.get("teachingProfessorNames"), (String) request.get("departmentId"));
        List<String> teachingAssistantIds = Arrays.asList((String[]) request.get("teachingAssistantIds"));

        course.setPrerequisiteIds(prerequisiteIds);
        course.setCorequisiteIds(corequisiteIds);
        makeCorequisitenessBiderectional(databaseManager, corequisiteIds, course);

        course.setTeachingProfessorIds(teachingProfessorIds);
        course.setTeachingAssistantIds(teachingAssistantIds);
        course.setCourseName((String) request.get("courseName") + " (G: " + groupNumber + ")");
        course.setDegreeLevel((DegreeLevel) request.get("degreeLevel"));
        course.setNumberOfCredits((int) request.get("numberOfCredits"));
        course.setCourseCapacity((int) request.get("courseCapacity"));
        course.setWeeklyClassTimes((ArrayList<WeekTime>) request.get("weeklyClassTimes"));
        course.setExamDate((LocalDateTime) request.get("examDate"));
        course.setTermIdentifier((TermIdentifier) request.get("termIdentifier"));
        course.setTheologyCourse((boolean) request.get("isTheologyCourse"));

        course.setGroupNumber(
                getNumberOfCurrentCoursesWithId(databaseManager, course.getId(), course.getTermIdentifier()) + 1
        );

        databaseManager.save(DatasetIdentifier.COURSES, course);

        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, (String) request.get("departmentId"));
        department.addToCourseIDs(course.getId());

        return course.getId();
    }

    private static void makeCorequisitenessBiderectional(DatabaseManager databaseManager, List<String> corequisiteIds,
                                                         Course course) {
        String pureCourseId = SelectionErrorUtils.getCourseIdBarTermAndGroupIdentifiers(course.getId());

        corequisiteIds.stream()
                .flatMap(id -> IdentifiableFetchingUtils.getCoursesWithPureId(databaseManager, id).stream())
                .filter(Objects::nonNull)
                .forEach(corequisiteCourse -> {
                    corequisiteCourse.addToCorequisiteIds(pureCourseId);
                });
    }

    private static int getNumberOfCurrentCoursesWithId(DatabaseManager databaseManager, String courseId,
                                                       TermIdentifier termIdentifier) {
        List<Identifiable> allCourses = databaseManager.getIdentifiables(DatasetIdentifier.COURSES);
        String termIdentifierString = termIdentifier.toString();
        String processedCourseId = courseId + termIdentifier;
        return (int) allCourses.stream()
                .map(identifiable -> (Course) identifiable)
                .filter(course -> {
                    return getCourseIdBarGroupIdentifier(course.getId()).equals(processedCourseId);
                })
                .count();
        // note that since the full course id includes the term identifier, there's no need for filtering the term id too
    }

    public static String getCourseIdBarGroupIdentifier(String courseId) {
        String[] partitionedCourseId = courseId.split("0");
        String groupId = partitionedCourseId[partitionedCourseId.length - 1];

        int courseIdLength = courseId.length();
        int groupIdLength = groupId.length();

        return courseId.substring(0, courseIdLength - groupIdLength - 1); // -1 removes the "0" as well
    }
}
