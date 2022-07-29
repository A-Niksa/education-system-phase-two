import server.database.models.pojos.abstractions.Course;
import server.database.models.pojos.abstractions.Department;
import server.database.models.pojos.abstractions.DepartmentName;
import server.database.models.pojos.users.User;
import server.database.models.pojos.users.professors.AcademicLevel;
import server.database.models.pojos.users.professors.AcademicRole;
import server.database.models.pojos.users.professors.Professor;
import server.database.models.pojos.users.students.DegreeLevel;
import server.database.models.pojos.users.students.Student;
import server.database.models.pojos.users.students.StudentStatus;
import server.database.management.DatabaseManager;

import java.util.Date;

public class Main {
    private static final DatabaseManager manager = new DatabaseManager();

    public static void main(String[] args) {
        createTestData();
        Student hamidi = (Student) manager.fetch(User.class, "19101100");
        System.out.println(hamidi);
    }

    private static void createTestData() {
        Department mathDepartment = new Department();
        mathDepartment.setDepartmentName(DepartmentName.MATHEMATICS);
        mathDepartment.initializeId();
        Professor khazayi = new Professor();
        khazayi.setDepartment(mathDepartment);
        khazayi.setAcademicLevel(AcademicLevel.ASSOCIATE);
        khazayi.setAcademicRole(AcademicRole.DEPUTY);
        khazayi.initializeId();
        khazayi.setNationalId("015033903");
        khazayi.setFirstName("Shahram");
        khazayi.setLastName("Khazayi");
        khazayi.setPhoneNumber("09129730021");
        khazayi.setEmailAddress("khazayi@sharif.edu");
        khazayi.setRoomNumber("105");
        khazayi.setPassword("1234");
        Student hamidi = new Student();
        hamidi.setDepartment(mathDepartment);
        hamidi.setDegreeLevel(DegreeLevel.UNDERGRADUATE);
        hamidi.setStudentStatus(StudentStatus.CURRENTLY_STUDYING);
        hamidi.setYearOfEntry(2019);
        hamidi.initializeId();
        hamidi.setNationalId("0150802202");
        hamidi.setFirstName("Aref");
        hamidi.setLastName("Hamidi");
        hamidi.setPhoneNumber("09192302103");
        hamidi.setEmailAddress("hamidi@sharif.edu");
        hamidi.setAdvisingProfessor(khazayi);
        hamidi.setPassword("1234");
        hamidi.setLastLogin(new Date());
        khazayi.addToStudentsUnderAdvice(hamidi);
        mathDepartment.setDeputy(khazayi);
        mathDepartment.addToStudents(hamidi);
        Professor fanaei = new Professor();
        fanaei.setDepartment(mathDepartment);
        fanaei.setAcademicLevel(AcademicLevel.FULL);
        fanaei.setAcademicRole(AcademicRole.DEAN);
        fanaei.initializeId();
        fanaei.setNationalId("015033904");
        fanaei.setFirstName("Hamidreza");
        fanaei.setLastName("Fanaei");
        fanaei.setPhoneNumber("09129730321");
        fanaei.setEmailAddress("fanaei@sharif.edu");
        fanaei.setRoomNumber("107");
        fanaei.setPassword("1234");
        mathDepartment.setDean(fanaei);
        mathDepartment.addToProfessors(khazayi);
        mathDepartment.addToProfessors(fanaei);
        Student rezaei = new Student();
        rezaei.setDepartment(mathDepartment);
        rezaei.setDegreeLevel(DegreeLevel.UNDERGRADUATE);
        rezaei.setStudentStatus(StudentStatus.CURRENTLY_STUDYING);
        rezaei.setYearOfEntry(2018);
        rezaei.initializeId();
        rezaei.setNationalId("0152902202");
        rezaei.setFirstName("Arash");
        rezaei.setLastName("Rezaei");
        rezaei.setPhoneNumber("09192302110");
        rezaei.setEmailAddress("rezaei@sharif.edu");
        rezaei.setAdvisingProfessor(fanaei);
        rezaei.setPassword("5678");
        rezaei.setStudentStatus(StudentStatus.DROPPED_OUT);
        Course complexAnalysis = new Course();
        complexAnalysis.setDepartment(mathDepartment);
        complexAnalysis.initializeId();
        complexAnalysis.addToTeachingProfessors(khazayi);
        complexAnalysis.addToTeachingProfessors(fanaei);
        complexAnalysis.addToStudents(hamidi);
        complexAnalysis.addToTAs(rezaei);
        complexAnalysis.setNumberOfCredits(4);
        complexAnalysis.setTermIdentifier("20222");
        complexAnalysis.setActive(true);

        manager.save(mathDepartment);
        manager.save(khazayi);
        manager.save(fanaei);
        manager.save(hamidi.getTranscript());
        manager.save(rezaei.getTranscript());
        manager.save(hamidi);
        manager.save(rezaei);
        manager.save(complexAnalysis);
    }
}
