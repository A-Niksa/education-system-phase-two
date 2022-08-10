package server.network.clienthandling.logicutils.standing;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.comparators.CourseScoreDTOComparator;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.abstractions.Score;
import shareables.models.pojos.abstractions.Transcript;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.CourseScoreDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class StandingManagementUtils {
    private static CourseScoreDTOComparator courseScoreDTOComparator = new CourseScoreDTOComparator();

    public static String[] getProfessorActiveCourseNames(DatabaseManager databaseManager, String professorId) {
        List<Identifiable> courses = databaseManager.getIdentifiables(DatasetIdentifier.COURSES);
        List<String> activeCourseNamesList = new ArrayList<>();
        courses.stream()
                .map(e -> (Course) e)
                .filter(e -> e.getTeachingProfessorIds().contains(professorId))
                .forEach(e -> {
                    String courseName = e.getCourseName();
                    activeCourseNamesList.add(courseName);
                });
        Collections.sort(activeCourseNamesList);
        return activeCourseNamesList.toArray(new String[0]);
    }

    public static List<CourseScoreDTO> getCourseScoreDTOsForCourse(DatabaseManager databaseManager, String departmentId,
                                                                   String courseName) {
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        List<String> departmentCourseIds = department.getCourseIds();
        List<CourseScoreDTO> courseScoreDTOs = new ArrayList<>();
        departmentCourseIds.stream()
                .map(e -> IdentifiableFetchingUtils.getCourse(databaseManager, e))
                .filter(e -> e.getCourseName().equals(courseName))
                .forEach(e -> {
                    courseScoreDTOs.addAll(extractCourseScoreDTOsFromCourseTranscripts(e, databaseManager));
                });
        courseScoreDTOs.sort(courseScoreDTOComparator);
        return courseScoreDTOs;
    }

    private static List<CourseScoreDTO> extractCourseScoreDTOsFromCourseTranscripts(Course course,
                                                                                    DatabaseManager databaseManager) {
        List<String> courseStudentIds = course.getStudentIds();
        List<CourseScoreDTO> extractedCourseScoreDTOs = new ArrayList<>();
        courseStudentIds.stream()
                .map(e -> IdentifiableFetchingUtils.getStudent(databaseManager, e))
                .forEach(e -> {
                    CourseScoreDTO courseScoreDTO = new CourseScoreDTO();
                    courseScoreDTO.setStudentId(e.getId());
                    courseScoreDTO.setStudentName(e.fetchName());
                    courseScoreDTO.setCourseId(course.getId());
                    courseScoreDTO.setCourseName(course.getCourseName());
                    Score studentScoreInCourse = getStudentScoreInCourse(e, course.getId());
                    if (studentScoreInCourse != null) {
                        courseScoreDTO.setFinalized(studentScoreInCourse.isFinalized());
                        courseScoreDTO.setScore(studentScoreInCourse.getScore());
                        courseScoreDTO.setStudentProtest(studentScoreInCourse.getStudentProtest());
                        courseScoreDTO.setProfessorResponse(studentScoreInCourse.getProfessorResponse());
                    } else {
                        courseScoreDTO.setScore(-1.0);
                        courseScoreDTO.setFinalized(false);
                    }
                    extractedCourseScoreDTOs.add(courseScoreDTO);
                });
        extractedCourseScoreDTOs.sort(courseScoreDTOComparator);
        return extractedCourseScoreDTOs;
    }

    private static Score getStudentScoreInCourse(Student student, String courseId) {
        Map<String, Score> courseIdScoreMap = student.getTranscript().getCourseIdScoreMap();
        return courseIdScoreMap.get(courseId);
    }

    public static void respondToProtest(DatabaseManager databaseManager, String courseId, String protestingStudentId,
                                        String responseToProtest) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);
        Student protestingStudent = course.getStudentIds().stream()
                .map(e -> IdentifiableFetchingUtils.getStudent(databaseManager, e))
                .filter(e -> e.getId().equals(protestingStudentId))
                .findAny().orElse(null);
        Map<String, Score> courseIdScoreMap = protestingStudent.getTranscript().getCourseIdScoreMap();
        courseIdScoreMap.entrySet().stream()
                .filter(e -> e.getKey().equals(courseId))
                .forEach(e -> {
                    e.getValue().setProfessorResponse(responseToProtest);
                });
    }

    public static void saveTemporaryScores(DatabaseManager databaseManager, String departmentId, String courseName,
                                           Map<String, Double> temporaryScoresMap) {
        Course course = getCourse(databaseManager, departmentId, courseName);
        List<String> courseStudentIds = course.getStudentIds();
        courseStudentIds.stream()
                .map(e -> IdentifiableFetchingUtils.getStudent(databaseManager, e))
                .forEach(e -> {
                    Score score = new Score(false, temporaryScoresMap.get(e.getId()));
                    e.getTranscript().put(course.getId(), score);
                });
    }

    public static Course getCourse(DatabaseManager databaseManager, String departmentId, String courseName) {
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        List<String> departmentCourseIds = department.getCourseIds();
        return departmentCourseIds.stream()
                .map(e -> IdentifiableFetchingUtils.getCourse(databaseManager, e))
                .filter(e -> e.getCourseName().equals(courseName))
                .findAny().orElse(null);
    }

    public static boolean allStudentsHaveBeenTemporaryScores(DatabaseManager databaseManager, String departmentId,
                                                             String courseName) {
        Course course = getCourse(databaseManager, departmentId, courseName);
        int numberOfCourseStudents = course.getStudentIds().size();
        int numberOfStudentsWithTemporaryScores = getNumberOfStudentsWithTemporaryScores(databaseManager, course);
        return numberOfCourseStudents == numberOfStudentsWithTemporaryScores;
    }

    private static int getNumberOfStudentsWithTemporaryScores(DatabaseManager databaseManager, Course course) {
        return (int) getCourseScoreEntriesStream(databaseManager, course)
                .count(); // casting is permissible since the number of students is of course less than Integer.MAX_VALUE
    }

    public static void finalizeScores(DatabaseManager databaseManager, String departmentId, String courseName) {
        Course course = getCourse(databaseManager, departmentId, courseName);
        getCourseScoreEntriesStream(databaseManager, course)
                .forEach(e -> e.getValue().setFinalized(true));
        List<String> courseStudentIds = course.getStudentIds();
        updateCourseStudentGPAs(databaseManager, courseStudentIds);
    }

    private static void updateCourseStudentGPAs(DatabaseManager databaseManager, List<String> courseStudentIds) {
        courseStudentIds.stream()
                .map(e -> IdentifiableFetchingUtils.getStudent(databaseManager, e))
                .forEach(e -> {
                    Transcript transcript = e.getTranscript();
                    Map<String, Score> courseIdScoreMap = transcript.getCourseIdScoreMap();
                    transcript.setGPA(CalculationOfGPAUtils.calculateGPA(databaseManager, courseIdScoreMap));
                });
    }

    private static Stream<Map.Entry<String, Score>> getCourseScoreEntriesStream(DatabaseManager databaseManager, Course course) {
        List<String> courseStudentIds = course.getStudentIds();
        return courseStudentIds.stream()
                .map(e -> IdentifiableFetchingUtils.getStudent(databaseManager, e))
                .map(Student::getTranscript)
                .flatMap(e -> e.getCourseIdScoreMap().entrySet().stream())
                .filter(e -> e.getKey().equals(course.getId()));
    }
}
