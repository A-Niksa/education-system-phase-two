package server.database.models.idgeneration;

import server.database.models.pojos.abstractions.Course;
import server.database.models.pojos.abstractions.Department;
import server.database.models.pojos.academicrequests.AcademicRequest;
import server.database.models.pojos.users.professors.Professor;
import server.database.models.pojos.users.students.Student;

import java.util.Date;

public class IdGenerator {
    public static String generateId(Student student) {
        String yearOfEntryPrefix = IdGetter.getYearOfEntryId(student.getYearOfEntry());
        String formattedDepartmentId = "10" + student.getDepartment().getId();
        String sequentialId = String.format(IdGetter.getDegreeLevelId(student.getDegreeLevel()) + "%02d",
                Student.getSequentialId());
        Student.incrementSequentialId();
        return yearOfEntryPrefix + formattedDepartmentId + sequentialId;
    }

    public static String generateId(Professor professor) {
        String academicRoleId = IdGetter.getAcademicRoleId(professor.getAcademicRole());
        String formattedDepartmentId = "20" + professor.getDepartment().getId(); // "20" implies that this id belongs to a prof
        String sequentialId = String.format(IdGetter.getAcademicLevelId(professor.getAcademicLevel()) + "%02d",
                Professor.getSequentialId());
        Professor.incrementSequentialId();
        return academicRoleId + "0" + formattedDepartmentId + sequentialId;
    }

    public static String generateId(Course course) {
        String departmentId = course.getDepartment().getId();
        String sequentialId = String.format("%03d", Course.getSequentialId());
        Course.incrementSequentialId();
        return departmentId + sequentialId;
    }

    public static String generateId(Department department) {
        return IdGetter.getDepartmentId(department.getDepartmentName());
    }

    public static String generateId(AcademicRequest academicRequest) {
        Date requestDate = academicRequest.getRequestDate();
        return requestDate.getTime() + "";
    }
}
