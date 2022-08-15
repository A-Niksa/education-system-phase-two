package shareables.models.pojos.abstractions;

import server.database.datasets.DatasetIdentifier;
import shareables.models.idgeneration.Identifiable;
import shareables.models.idgeneration.SequentialIdGenerator;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.models.pojos.users.students.Student;
import shareables.utils.timing.timekeeping.WeekTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Course extends Identifiable {
    private static SequentialIdGenerator sequentialIdGenerator;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator(DatasetIdentifier.COURSES);
    }

    private String departmentId;
    private String courseName;
    private List<String> teachingProfessorIds;
    private List<String> teachingAssistantIds;
    private List<String> studentIds;
    private List<WeekTime> weeklyClassTimes;
    private LocalDateTime examDate;
    private TermIdentifier termIdentifier; // such as "20182" or "20201"
    private int numberOfCredits;
    private boolean isActive; // shows whether the course is being currently taught
    private DegreeLevel degreeLevel;
    private List<String> prerequisiteIds;
    private List<String> corequisiteIds;
    private int courseCapacity;

    public Course() {
    }

    public Course(String departmentId, TermIdentifier termIdentifier) {
        this.departmentId = departmentId;
        this.termIdentifier = termIdentifier;
        isActive = termIdentifier.courseIsActive(); // will be active if the term id matches the current semester
        teachingProfessorIds = new ArrayList<>();
        teachingAssistantIds = new ArrayList<>();
        studentIds = new ArrayList<>();
        weeklyClassTimes = new ArrayList<>();
        prerequisiteIds = new ArrayList<>();
        corequisiteIds = new ArrayList<>();
        initializeId();
    }

    public void addToWeeklyClassTimes(WeekTime weekTime) {
        weeklyClassTimes.add(weekTime);
    }

    public void removeFromWeeklyClassTimes(WeekTime weekTime) {
        weeklyClassTimes.removeIf(e -> e.equals(weekTime));
    }

    public void addToTeachingProfessorIds(String professorId) {
        teachingProfessorIds.add(professorId);
    }

    public void removeFromTeachingProfessorIds(String professorId) {
        teachingProfessorIds.removeIf(e -> e.equals(professorId));
    }

    public void addToTeachingAssistantIds(String studentId) {
        teachingAssistantIds.add(studentId);
    }

    public void removeFromTeachingAssistantIds(String studentId) {
        teachingAssistantIds.removeIf(e -> e.equals(studentId));
    }

    public void addToStudentIds(String studentId) {
        studentIds.add(studentId);
    }

    public void removeFromStudentIds(String studentId) {
        studentIds.removeIf(e -> e.equals(studentId));
    }

    @Override
    protected void initializeId() {
        id = idGenerator.nextId(this, sequentialIdGenerator);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getTeachingProfessorIds() {
        return teachingProfessorIds;
    }

    public void setTeachingProfessorIds(List<String> teachingProfessorIds) {
        this.teachingProfessorIds = teachingProfessorIds;
    }

    public List<String> getTeachingAssistantIds() {
        return teachingAssistantIds;
    }

    public void setTeachingAssistantIds(List<String> teachingAssistantIds) {
        this.teachingAssistantIds = teachingAssistantIds;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }

    public int getNumberOfCredits() {
        return numberOfCredits;
    }

    public void setNumberOfCredits(int numberOfCredits) {
        this.numberOfCredits = numberOfCredits;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public TermIdentifier getTermIdentifier() {
        return termIdentifier;
    }

    public void setTermIdentifier(TermIdentifier termIdentifier) {
        this.termIdentifier = termIdentifier;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public LocalDateTime getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDateTime examDate) {
        this.examDate = examDate;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<WeekTime> getWeeklyClassTimes() {
        return weeklyClassTimes;
    }

    public DegreeLevel getDegreeLevel() {
        return degreeLevel;
    }

    public void setDegreeLevel(DegreeLevel degreeLevel) {
        this.degreeLevel = degreeLevel;
    }

    public void setWeeklyClassTimes(List<WeekTime> weeklyClassTimes) {
        this.weeklyClassTimes = weeklyClassTimes;
    }

    public List<String> getPrerequisiteIds() {
        return prerequisiteIds;
    }

    public void setPrerequisiteIds(List<String> prerequisiteIds) {
        this.prerequisiteIds = prerequisiteIds;
    }

    public List<String> getCorequisiteIds() {
        return corequisiteIds;
    }

    public void setCorequisiteIds(List<String> corequisiteIds) {
        this.corequisiteIds = corequisiteIds;
    }

    public int getCourseCapacity() {
        return courseCapacity;
    }

    public void setCourseCapacity(int courseCapacity) {
        this.courseCapacity = courseCapacity;
    }
}