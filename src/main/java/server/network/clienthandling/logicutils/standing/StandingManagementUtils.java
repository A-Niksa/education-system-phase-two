package server.network.clienthandling.logicutils.standing;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class StandingManagementUtils {
    public static String[] getProfessorActiveCourseNames(DatabaseManager databaseManager, String professorId) {
        List<Identifiable> courses = databaseManager.getIdentifiables(DatasetIdentifier.COURSES);
        List<String> activeCourseNamesList = new ArrayList<>();
        courses.stream()
                .map(e -> (Course) e)
                .filter(e -> professorIsInTeachingProfessors(e.getTeachingProfessors(), professorId))
                .forEach(e -> {
                    String courseName = e.getCourseName();
                    activeCourseNamesList.add(courseName);
                });
        return activeCourseNamesList.toArray(new String[0]);
    }

    private static boolean professorIsInTeachingProfessors(List<Professor> teachingProfessors, String professorId) {
        return teachingProfessors.stream()
                .anyMatch(e -> e.getId().equals(professorId));
    }

    public static List<CourseScoreDTO> getCourseScoreDTOsForCourse(DatabaseManager databaseManager, String departmentId,
                                                                   String courseName) {
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        List<Course> departmentCourses = department.getCourses();
        List<CourseScoreDTO> courseScoreDTOs = new ArrayList<>();
        departmentCourses.stream()
                .filter(e -> e.getCourseName().equals(courseName))
                .forEach(e -> {
                    courseScoreDTOs.addAll(extractCourseScoreDTOsFromCourseTranscripts(e));
                });
        return courseScoreDTOs;
    }

    private static List<CourseScoreDTO> extractCourseScoreDTOsFromCourseTranscripts(Course course) {
        List<Student> courseStudents = course.getStudents();
        List<CourseScoreDTO> extractedCourseScoreDTOs = new ArrayList<>();
        courseStudents.forEach(e -> {
                    CourseScoreDTO courseScoreDTO = new CourseScoreDTO();
                    courseScoreDTO.setStudentId(e.getId());
                    courseScoreDTO.setStudentName(e.fetchName());
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
        return extractedCourseScoreDTOs;
    }

    private static Score getStudentScoreInCourse(Student student, String courseId) {
        Map<String, Score> courseIdScoreMap = student.getTranscript().getCourseIdScoreMap();
        return courseIdScoreMap.get(courseId);
    }

    public static void respondToProtest(DatabaseManager databaseManager, String courseId, String protestingStudentId,
                                        String responseToProtest) {
        Course course = IdentifiableFetchingUtils.getCourse(databaseManager, courseId);
        Student protestingStudent = course.getStudents().stream()
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
        List<Student> courseStudents = course.getStudents();
        courseStudents.forEach(e -> {
            Score score = new Score(false, temporaryScoresMap.get(e.getId()));
            e.getTranscript().put(course.getId(), score);
        });
    }

    private static Course getCourse(DatabaseManager databaseManager, String departmentId, String courseName) {
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        List<Course> departmentCourses = department.getCourses();
        return departmentCourses.stream()
                .filter(e -> e.getCourseName().equals(courseName))
                .findAny().orElse(null);
    }

    public static boolean allStudentsHaveBeenTemporaryScores(DatabaseManager databaseManager, String departmentId,
                                                             String courseName) {
        Course course = getCourse(databaseManager, departmentId, courseName);
        int numberOfCourseStudents = course.getStudents().size();
        int numberOfStudentsWithTemporaryScores = getNumberOfStudentsWithTemporaryScores(course);
        return numberOfCourseStudents == numberOfStudentsWithTemporaryScores;
    }

    private static int getNumberOfStudentsWithTemporaryScores(Course course) {
        return (int) getCourseScoreEntriesStream(course)
                .count(); // casting is permissible since the number of students is of course less than Integer.MAX_VALUE
    }

    public static void finalizeScores(DatabaseManager databaseManager, String departmentId, String courseName) {
        Course course = getCourse(databaseManager, departmentId, courseName);
        getCourseScoreEntriesStream(course)
                .forEach(e -> e.getValue().setFinalized(true));
        List<Student> courseStudents = course.getStudents();
        updateCourseStudentGPAs(databaseManager, courseStudents);
    }

    private static void updateCourseStudentGPAs(DatabaseManager databaseManager, List<Student> courseStudents) {
        courseStudents.forEach(e -> {
            Transcript transcript = e.getTranscript();
            Map<String, Score> courseIdScoreMap = transcript.getCourseIdScoreMap();
            transcript.setGPA(CalculationOfGPAUtils.calculateGPA(databaseManager, courseIdScoreMap));
        });
    }

    private static Stream<Map.Entry<String, Score>> getCourseScoreEntriesStream(Course course) {
        List<Student> courseStudents = course.getStudents();
        return courseStudents.stream()
                .map(Student::getTranscript)
                .flatMap(e -> e.getCourseIdScoreMap().entrySet().stream())
                .filter(e -> e.getKey().equals(course.getId())
                        && e.getValue().isFinalized());
    }
}
