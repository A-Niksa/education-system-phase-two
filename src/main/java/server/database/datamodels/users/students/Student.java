package server.database.datamodels.users.students;

import server.database.datamodels.abstractions.Course;
import server.database.datamodels.abstractions.Transcript;
import server.database.datamodels.requests.Request;
import server.database.datamodels.users.User;
import server.database.datamodels.users.professors.Professor;
import server.database.idgeneration.IdGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Student extends User {
    private static int sequentialId = 0;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "advising_professor_id")
    private Professor advisingProfessor;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinTable(name = "Students_Transcripts")
    private Transcript transcript;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "current_courses_ids")
    private List<Course> currentCourses;
    // TODO:
//    @OneToMany(cascade = CascadeType.PERSIST)
//    @JoinTable(name = "Students_Requests")
//    private List<Request> sentRequests;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "assisting_course_id")
    private List<Course> assistingCourses; // list of courses where the student is a TA
    @Column
    private DegreeLevel degreeLevel;
    @Column
    private StudentStatus studentStatus;
    @Column
    private int yearOfEntry;

    public Student() {
        transcript = new Transcript();
        currentCourses = new ArrayList<>();
//        sentRequests = new ArrayList<>();
        assistingCourses = new ArrayList<>();
    }

    public void addToCurrentCourses(Course course) {
        currentCourses.add(course);
    }

    public void removeFromCurrentCourses(Course course) {
        currentCourses.removeIf(e -> e.getId().equals(course.getId()));
    }

//    public void addToSentRequests(Request request) {
//        sentRequests.add(request);
//    }
//
//    public void removeFromSentRequests(Request request) {
//        sentRequests.removeIf(e -> e.getId().equals(request.getId()));
//    }

    public void addToAssistingCourses(Course course) {
        assistingCourses.add(course);
    }

    public void removeFromAssistingCourses(Course course) {
        assistingCourses.removeIf(e -> e.getId().equals(course.getId()));
    }

    @Override
    public void initializeId() { // should only be called after the fields are filled (non-null)
        id = IdGenerator.generateId(this);
        transcript.setId(id);
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
}