package server.database.datamodels.users;

import server.database.datamodels.abstractions.Course;
import server.database.datamodels.abstractions.Transcript;
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
    @JoinTable(name = "Student_Transcript")
    private Transcript transcript;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "current_courses_ids")
    private List<Course> currentCourses;
    @Column
    private DegreeLevel degreeLevel;
    @Column
    private StudentStatus studentStatus;
    @Column
    private int yearOfEntry;
    @Column
    private double GPA;

    public Student() {
        transcript = new Transcript();
        currentCourses = new ArrayList<>();
    }

    public void addToCurrentCourses(Course course) {
        currentCourses.add(course);
    }

    public void removeFromCurrentCourses(Course course) {
        currentCourses.removeIf(e -> e.getId().equals(course.getId()));
    }

    @Override
    public void initializeId() { // should only be called after the fields are filled (non-null)
        id = IdGenerator.generateId(this);
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

    public double getGPA() {
        return GPA;
    }

    public void setGPA(double GPA) {
        this.GPA = GPA;
    }
}