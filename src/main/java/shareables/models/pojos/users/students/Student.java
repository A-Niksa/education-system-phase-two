package shareables.models.pojos.users.students;

import shareables.models.idgeneration.IdGenerator;
import shareables.models.idgeneration.SequentialIdGenerator;
import shareables.models.pojos.abstractions.Transcript;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.UserIdentifier;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private static SequentialIdGenerator sequentialIdGenerator;

    static {
        sequentialIdGenerator = getNewSequentialIdGenerator();
    }

    private String advisingProfessorId;
    private Transcript transcript;
    private List<String> currentCoursesIds;
    private List<String> assistingCoursesIds; // list of courses where the student is a TA
    private DegreeLevel degreeLevel;
    private StudentStatus studentStatus;
    private int yearOfEntry;

    public Student() {
    }

    public Student(int yearOfEntry, DegreeLevel degreeLevel, String departmentId) {
        super(UserIdentifier.STUDENT);
        this.yearOfEntry = yearOfEntry;
        this.degreeLevel = degreeLevel;
        this.departmentId = departmentId;
        transcript = new Transcript();
        currentCoursesIds = new ArrayList<>();
        assistingCoursesIds = new ArrayList<>();
        studentStatus = StudentStatus.CURRENTLY_STUDYING; // default value
        initializeId();
        initializeMessenger(id);
    }

    public void addToCurrentCoursesIds(String courseId) {
        currentCoursesIds.add(courseId);
    }

    public void removeFromCurrentCourses(String courseId) {
        currentCoursesIds.removeIf(e -> e.equals(courseId));
    }

    public void addToAssistingCoursesIds(String courseId) {
        assistingCoursesIds.add(courseId);
    }

    public void removeFromAssistingCourses(String courseId) {
        assistingCoursesIds.removeIf(e -> e.equals(courseId));
    }

    @Override
    protected void initializeId() { // should only be called after the necessary constructor fields are filled (non-null)
        id = idGenerator.nextId(this, sequentialIdGenerator);
    }

    public double calculateGPA() {
        return transcript.calculateGPA();
    }

    public Transcript getTranscript() {
        return transcript;
    }

    public void setTranscript(Transcript transcript) {
        this.transcript = transcript;
    }

    public DegreeLevel getDegreeLevel() {
        return degreeLevel;
    }

    public void setDegreeLevel(DegreeLevel degreeLevel) {
        this.degreeLevel = degreeLevel;
    }

    public StudentStatus getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(StudentStatus studentStatus) {
        this.studentStatus = studentStatus;
    }

    public void setYearOfEntry(int yearOfEntry) {
        this.yearOfEntry = yearOfEntry;
    }

    public String getAdvisingProfessorId() {
        return advisingProfessorId;
    }

    public void setAdvisingProfessorId(String advisingProfessorId) {
        this.advisingProfessorId = advisingProfessorId;
    }

    public List<String> getCurrentCoursesIds() {
        return currentCoursesIds;
    }

    public void setCurrentCoursesIds(List<String> currentCoursesIds) {
        this.currentCoursesIds = currentCoursesIds;
    }

    public List<String> getAssistingCoursesIds() {
        return assistingCoursesIds;
    }

    public void setAssistingCoursesIds(List<String> assistingCoursesIds) {
        this.assistingCoursesIds = assistingCoursesIds;
    }

    public int getYearOfEntry() {
        return yearOfEntry;
    }

    // TODO: should be removed
    @Override
    public String toString() {
        return "Student{" +
                "advisingProfessorId='" + advisingProfessorId + '\'' +
                ", transcript=" + transcript +
                ", currentCoursesIds=" + currentCoursesIds +
                ", assistingCoursesIds=" + assistingCoursesIds +
                ", degreeLevel=" + degreeLevel +
                ", studentStatus=" + studentStatus +
                ", yearOfEntry=" + yearOfEntry +
                '}';
    }
}