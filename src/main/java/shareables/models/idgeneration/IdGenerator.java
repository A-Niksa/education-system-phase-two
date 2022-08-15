package shareables.models.idgeneration;

import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.academicrequests.AcademicRequest;
import shareables.models.pojos.media.MediaFile;
import shareables.models.pojos.messaging.Conversation;
import shareables.models.pojos.messaging.Message;
import shareables.models.pojos.messaging.Messenger;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.Student;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class IdGenerator {
    private IdGetter idGetter;
    
    public IdGenerator() {
        idGetter = new IdGetter();
    }
    
    public String nextId(Student student, SequentialIdGenerator sequentialIdGenerator) {
        String yearOfEntryPrefix = idGetter.getYearOfEntryId(student.getYearOfEntry());
        String formattedDepartmentId = "10" + student.getDepartmentId();
        String degreeLevelId = idGetter.getDegreeLevelId(student.getDegreeLevel());
        String sequentialId = String.format("%02d", sequentialIdGenerator.nextSequentialId());
        return yearOfEntryPrefix + formattedDepartmentId + degreeLevelId + sequentialId;
    }

    public String nextId(Professor professor, SequentialIdGenerator sequentialIdGenerator) {
        String academicRoleId = idGetter.getAcademicRoleId(professor.getAcademicRole());
        String formattedDepartmentId = "20" + professor.getDepartmentId(); // "20" implies that this id belongs to a prof
        String academicLevelId = idGetter.getAcademicLevelId(professor.getAcademicLevel());
        String sequentialId = String.format("%02d", sequentialIdGenerator.nextSequentialId());
        return academicRoleId + "0" + formattedDepartmentId + academicLevelId + sequentialId;
    }

    public String nextId(Course course, SequentialIdGenerator sequentialIdGenerator) {
        String departmentId = course.getDepartmentId();
        String sequentialId = String.format("%03d", sequentialIdGenerator.nextSequentialId());
        String termId = course.getTermIdentifier().toString();
        String groupId = String.valueOf(course.getGroupNumber());
        return departmentId + sequentialId + termId + "0" + groupId;
    }

    public String nextId(Department department) {
        return idGetter.getDepartmentId(department.getDepartmentName());
    }

    public String nextId(IdentifiableWithTime identifiableWithTime, SequentialIdGenerator sequentialIdGenerator) {
        LocalDateTime currentDate = identifiableWithTime.getDate();
        String currentDateId = String.format("%06d", currentDate.toEpochSecond(ZoneOffset.UTC) % 1000000);
        String sequentialId = String.format("%03d", sequentialIdGenerator.nextSequentialId());
        return currentDateId + sequentialId;
    }

    public String nextId(MediaFile mediaFile, SequentialIdGenerator sequentialIdGenerator) {
        LocalDateTime currentDate = mediaFile.getDate();
        String currentDateId = String.format("%03d", currentDate.toEpochSecond(ZoneOffset.UTC) % 1000);
        String sequentialId = String.format("%03d", sequentialIdGenerator.nextSequentialId());
        return currentDateId + sequentialId;
    }

    public String nextId(IdentifiableWithTime identifiableWithTime) {
        LocalDateTime currentDate = identifiableWithTime.getDate();
        return currentDate.toEpochSecond(ZoneOffset.UTC) + "";
    }
}
