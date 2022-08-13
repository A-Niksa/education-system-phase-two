package shareables.models.pojos.users.professors;

import server.database.datasets.DatasetIdentifier;
import shareables.models.idgeneration.SequentialIdGenerator;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.UserIdentifier;
import shareables.models.pojos.users.students.Student;

import java.util.ArrayList;
import java.util.List;

public class Professor extends User {
    private static SequentialIdGenerator sequentialIdGenerator;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator(DatasetIdentifier.PROFESSORS);
    }

    private List<String> adviseeStudentIds;
    private String officeNumber;
    private AcademicLevel academicLevel;
    private AcademicRole academicRole;
    private boolean isTemporaryDeputy;

    public Professor() {
    }

    public Professor(AcademicRole academicRole, AcademicLevel academicLevel, String departmentId) {
        super(UserIdentifier.PROFESSOR);
        this.academicRole = academicRole;
        this.academicLevel = academicLevel;
        this.departmentId = departmentId;
        adviseeStudentIds = new ArrayList<>();
        isTemporaryDeputy = false; // default value
        initializeId();
        initializeMessenger(id);
        initializeNotificationsManager(id);
    }

    public void addToAdviseeStudentIds(String studentId) {
        adviseeStudentIds.add(studentId);
    }

    public void removeFromAdviseeStudentIds(String studentId) {
        adviseeStudentIds.removeIf(e -> e.equals(studentId));
    }

    @Override
    protected void initializeId() {
        id = idGenerator.nextId(this, sequentialIdGenerator);
    }

    public static SequentialIdGenerator getSequentialIdGenerator() {
        return sequentialIdGenerator;
    }

    public static void setSequentialIdGenerator(SequentialIdGenerator sequentialIdGenerator) {
        Professor.sequentialIdGenerator = sequentialIdGenerator;
    }

    public List<String> getAdviseeStudentIds() {
        return adviseeStudentIds;
    }

    public void setAdviseeStudentIds(List<String> adviseeStudentIds) {
        this.adviseeStudentIds = adviseeStudentIds;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    public AcademicLevel getAcademicLevel() {
        return academicLevel;
    }

    public void setAcademicLevel(AcademicLevel academicLevel) {
        this.academicLevel = academicLevel;
    }

    public AcademicRole getAcademicRole() {
        return academicRole;
    }

    public void setAcademicRole(AcademicRole academicRole) {
        this.academicRole = academicRole;
    }

    public boolean isTemporaryDeputy() {
        return isTemporaryDeputy;
    }

    public void setTemporaryDeputy(boolean temporaryDeputy) {
        isTemporaryDeputy = temporaryDeputy;
    }
}
