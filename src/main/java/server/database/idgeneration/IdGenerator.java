package server.database.idgeneration;

import server.database.datamodels.users.Student;

public class IdGenerator {
    public static String generateId(Student student) {
        String yearOfEntryPrefix = IdGetter.getYearOfEntryId(student.getYearOfEntry());
        String formattedDepartmentId = "10" + student.getDepartment().getId();
        String sequentialId = String.format(IdGetter.getDegreeLevelId(student.getDegreeLevel()) + "%02d",
                Student.getSequentialId());
        Student.incrementSequentialId();
        return yearOfEntryPrefix + formattedDepartmentId + sequentialId;
    }
}
