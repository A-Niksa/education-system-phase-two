package shareables.models.pojos.unitselection;

import shareables.models.idgeneration.IdentifiableWithTime;
import shareables.models.pojos.users.students.DegreeLevel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UnitSelectionSession extends IdentifiableWithTime {
    private String departmentId;
    private DegreeLevel intendedDegreeLevel;
    private int intendedYearOfEntry;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private List<StudentSelectionLog> studentSelectionLogs;
    private boolean isValidated;

    public UnitSelectionSession() {
        studentSelectionLogs = new ArrayList<>();
        isValidated = false;
        initializeId();
    }

    public void resetSession() {
        // TODO
        studentSelectionLogs.clear();
    }

    public void addToStudentSelectionLogs(StudentSelectionLog studentSelectionLog) {
        studentSelectionLogs.add(studentSelectionLog);
    }

    public void removeFromStudentSelectionLogs(String studentSelectionLogId) {
        studentSelectionLogs.removeIf(e -> e.getId().equals(studentSelectionLogId));
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public DegreeLevel getIntendedDegreeLevel() {
        return intendedDegreeLevel;
    }

    public void setIntendedDegreeLevel(DegreeLevel intendedDegreeLevel) {
        this.intendedDegreeLevel = intendedDegreeLevel;
    }

    public int getIntendedYearOfEntry() {
        return intendedYearOfEntry;
    }

    public void setIntendedYearOfEntry(int intendedYearOfEntry) {
        this.intendedYearOfEntry = intendedYearOfEntry;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(LocalDateTime startsAt) {
        this.startsAt = startsAt;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;
    }

    public List<StudentSelectionLog> getStudentSelectionLogs() {
        return studentSelectionLogs;
    }

    public void setStudentSelectionLogs(List<StudentSelectionLog> studentSelectionLogs) {
        this.studentSelectionLogs = studentSelectionLogs;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }
}