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
    private List<Professor> teachingProfessors;
    private List<Student> TAs;
    private List<Student> students;
    private List<WeekTime> weeklyClassTimes;
    private LocalDateTime examDate;
    private TermIdentifier termIdentifier; // such as "20182" or "20201"
    private int numberOfCredits;
    private boolean isActive; // shows whether the course is being currently taught
    private DegreeLevel degreeLevel;

    public Course() {
    }

    public Course(String departmentId, TermIdentifier termIdentifier) {
        this.departmentId = departmentId;
        this.termIdentifier = termIdentifier;
        isActive = termIdentifier.courseIsActive(); // will be active if the term id matches the current semester
        teachingProfessors = new ArrayList<>();
        TAs = new ArrayList<>();
        students = new ArrayList<>();
        weeklyClassTimes = new ArrayList<>();
        initializeId();
    }

    public void addToWeeklyClassTimes(WeekTime weekTime) {
        weeklyClassTimes.add(weekTime);
    }

    public void removeFromWeeklyClassTimes(WeekTime weekTime) {
        weeklyClassTimes.removeIf(e -> e.equals(weekTime));
    }

    public void addToTeachingProfessors(Professor professor) {
        teachingProfessors.add(professor);
    }

    public void removeFromTeachingProfessors(String professorId) {
        teachingProfessors.removeIf(e -> e.getId().equals(professorId));
    }

    public void addToTAs(Student student) {
        TAs.add(student);
    }

    public void removeFromTAs(String studentId) {
        TAs.removeIf(e -> e.getId().equals(studentId));
    }

    public void addToStudents(Student student) {
        students.add(student);
    }

    public void removeFromStudents(String studentId) {
        students.removeIf(e -> e.getId().equals(studentId));
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

    public List<Professor> getTeachingProfessors() {
        return teachingProfessors;
    }

    public void setTeachingProfessors(List<Professor> teachingProfessors) {
        this.teachingProfessors = teachingProfessors;
    }

    public List<Student> getTAs() {
        return TAs;
    }

    public void setTAs(List<Student> TAs) {
        this.TAs = TAs;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
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
}