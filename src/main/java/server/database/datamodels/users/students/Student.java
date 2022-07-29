package server.database.datamodels.users.students;

import org.hibernate.annotations.DiscriminatorOptions;
import server.database.datamodels.abstractions.Course;
import server.database.datamodels.abstractions.Transcript;
import server.database.datamodels.academicrequests.AcademicRequest;
import server.database.datamodels.users.User;
import server.database.datamodels.users.UserIdentifier;
import server.database.datamodels.users.professors.Professor;
import server.database.idgeneration.IdGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorOptions(force = true)
public class Student extends User {
    private static int sequentialId = 0;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "advisingProfessor_id")
    private Professor advisingProfessor;
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "Students_Transcripts")
    private Transcript transcript;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "currentCourses_ids")
    private List<Course> currentCourses;
    @OneToMany(mappedBy = "requestingStudent", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<AcademicRequest> sentRequests;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "assistingCourses_id")
    private List<Course> assistingCourses; // list of courses where the student is a TA
    @Column
    private DegreeLevel degreeLevel;
    @Column
    private StudentStatus studentStatus;
    @Column
    private int yearOfEntry;

    public Student() {
        super(UserIdentifier.STUDENT);
        transcript = new Transcript();
        currentCourses = new ArrayList<>();
        sentRequests = new ArrayList<>();
        assistingCourses = new ArrayList<>();
    }

    public void addToCurrentCourses(Course course) {
        currentCourses.add(course);
    }

    public void removeFromCurrentCourses(Course course) {
        currentCourses.removeIf(e -> e.getId().equals(course.getId()));
    }

    public void addToSentRequests(AcademicRequest request) {
        sentRequests.add(request);
    }

    public void removeFromSentRequests(AcademicRequest request) {
        sentRequests.removeIf(e -> e.getId().equals(request.getId()));
    }

    public void addToAssistingCourses(Course course) {
        assistingCourses.add(course);
    }

    public void removeFromAssistingCourses(Course course) {
        assistingCourses.removeIf(e -> e.getId().equals(course.getId()));
    }

    @Override
    public void initializeId() { // should only be called after the fields are filled (non-null)
        id = IdGenerator.generateId(this);
        transcript.setId(id + "T");
    }

    public static int getSequentialId() {
        return sequentialId;
    }

    public static void incrementSequentialId() {
        sequentialId++;
    }

    public Professor getAdvisingProfessor() {
        return advisingProfessor;
    }

    public void setAdvisingProfessor(Professor advisingProfessor) {
        this.advisingProfessor = advisingProfessor;
    }

    public Transcript getTranscript() {
        return transcript;
    }

    public void setTranscript(Transcript transcript) {
        this.transcript = transcript;
    }

    public List<Course> getCurrentCourses() {
        return currentCourses;
    }

    public void setCurrentCourses(List<Course> currentCourses) {
        this.currentCourses = currentCourses;
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

    public int getYearOfEntry() {
        return yearOfEntry;
    }

    public void setYearOfEntry(int yearOfEntry) {
        this.yearOfEntry = yearOfEntry;
    }

    // TODO: to be removed:
    @Override
    public String toString() {
        return "Student{" +
                "advisingProfessor=" + advisingProfessor +
                ", transcript=" + transcript +
                ", currentCourses=" + currentCourses +
                ", assistingCourses=" + assistingCourses +
                ", degreeLevel=" + degreeLevel +
                ", studentStatus=" + studentStatus +
                ", yearOfEntry=" + yearOfEntry +
                '}';
    }
}