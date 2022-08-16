package shareables.models.pojos.abstractions;

import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.unitselection.UnitSelectionSession;

import java.util.ArrayList;
import java.util.List;

public class Department extends Identifiable {
    private String id;
    private List<String> courseIds;
    private List<String> professorIds;
    private List<String> studentIds;
    private List<UnitSelectionSession> unitSelectionSessions;
    private String deanId;
    private String deputyId;
    private DepartmentName departmentName;

    public Department() {

    }

    public Department(DepartmentName departmentName) {
        this.departmentName = departmentName;
        courseIds = new ArrayList<>();
        professorIds = new ArrayList<>();
        studentIds = new ArrayList<>();
        unitSelectionSessions = new ArrayList<>();
        initializeId();
    }

    public void addToCourseIDs(String courseId) {
        courseIds.add(courseId);
    }

    public void removeFromCourseIds(String courseId) {
        courseIds.removeIf(e -> e.equals(courseId));
    }

    public void addToProfessorIds(String professorId) {
        professorIds.add(professorId);
    }

    public void removeFromProfessorIds(String professorId) {
        professorIds.removeIf(e -> e.equals(professorId));
    }

    public void addToStudentIds(String studentId) {
        studentIds.add(studentId);
    }

    public void removeFromStudentIds(String studentId) {
        studentIds.removeIf(e -> e.equals(studentId));
    }

    public void addToUnitSelectionSessions(UnitSelectionSession unitSelectionSession) {
        unitSelectionSessions.add(unitSelectionSession);
    }

    public void removeFromUnitSelectionSession(String unitSelectionSessionId) {
        unitSelectionSessions.removeIf(e -> e.getId().equals(unitSelectionSessionId));
    }

    @Override
    protected void initializeId() {
        id = idGenerator.nextId(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCourseIds(List<String> courseIds) {
        this.courseIds = courseIds;
    }

    public List<String> getProfessorIds() {
        return professorIds;
    }

    public void setProfessorIds(List<String> professorIds) {
        this.professorIds = professorIds;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }

    public String getDeanId() {
        return deanId;
    }

    public void setDeanId(String deanId) {
        this.deanId = deanId;
    }

    public String getDeputyId() {
        return deputyId;
    }

    public void setDeputyId(String deputyId) {
        this.deputyId = deputyId;
    }

    public DepartmentName getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(DepartmentName departmentName) {
        this.departmentName = departmentName;
    }

    public List<String> getCourseIds() {
        return courseIds;
    }

    public List<UnitSelectionSession> getUnitSelectionSessions() {
        return unitSelectionSessions;
    }

    public void setUnitSelectionSessions(List<UnitSelectionSession> unitSelectionSessions) {
        this.unitSelectionSessions = unitSelectionSessions;
    }
}
