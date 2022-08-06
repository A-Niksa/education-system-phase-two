import server.database.datasets.DatasetIdentifier;
import shareables.models.pojos.abstractions.*;
import shareables.models.pojos.users.professors.AcademicLevel;
import shareables.models.pojos.users.professors.AcademicRole;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.models.pojos.users.students.Student;
import server.database.management.DatabaseManager;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigIdSupplier;
import shareables.utils.config.ConfigManager;
import shareables.utils.timing.timekeeping.DayTime;
import shareables.utils.timing.timekeeping.WeekTime;
import shareables.utils.timing.timekeeping.Weekday;

import java.io.File;
import java.time.LocalDateTime;

public class Main {
    private static final DatabaseManager manager = new DatabaseManager();

    public static void main(String[] args) {
        ConfigIdSupplier.resetCurrentClientId();
        manager.getDatabaseWriter().purgeDirectory(new File(ConfigManager.getString(ConfigFileIdentifier.ADDRESSES, "datasetsFolderPath")));
        createTestData(); // TODO: cleaning the directories
//        manager.loadDatabase();
//        Student student = (Student) manager.get(DatasetIdentifier.STUDENTS, "19101100");
//        System.out.println(student);
    }

    private static void createTestData() {
        Department mathDepartment = new Department(DepartmentName.MATHEMATICS);
        Professor khazayi = new Professor(AcademicRole.DEPUTY, AcademicLevel.ASSOCIATE, mathDepartment.getId());
        khazayi.setNationalId("015033903");
        khazayi.setFirstName("Shahram");
        khazayi.setLastName("Khazayi");
        khazayi.setPhoneNumber("09129730021");
        khazayi.setEmailAddress("khazayi@sharif.edu");
        khazayi.setOfficeNumber("105");
        khazayi.setPassword("1234");
        Student hamidi = new Student(2019, DegreeLevel.UNDERGRADUATE, mathDepartment.getId());
        hamidi.setNationalId("0150802202");
        hamidi.setFirstName("Aref");
        hamidi.setLastName("Hamidi");
        hamidi.setPhoneNumber("09192302103");
        hamidi.setEmailAddress("hamidi@sharif.edu");
        hamidi.setAdvisingProfessorId(khazayi.getId());
        hamidi.setPassword("1234");
        hamidi.updateLastLogin();
        hamidi.setEnrolmentTime(LocalDateTime.now());
        khazayi.addToAdviseeStudentIds(hamidi.getId());
        mathDepartment.setDeputyId(khazayi.getId());
        mathDepartment.addToStudentIds(hamidi.getId());
        Professor fanaei = new Professor(AcademicRole.DEAN, AcademicLevel.FULL, mathDepartment.getId());
        fanaei.setNationalId("015033904");
        fanaei.setFirstName("Hamidreza");
        fanaei.setLastName("Fanaei");
        fanaei.setPhoneNumber("09129730321");
        fanaei.setEmailAddress("fanaei@sharif.edu");
        fanaei.setOfficeNumber("107");
        fanaei.setPassword("1234");
        mathDepartment.setDeanId(fanaei.getId());
        mathDepartment.addToProfessorIds(khazayi.getId());
        mathDepartment.addToProfessorIds(fanaei.getId());
        Student rezaei = new Student(2018, DegreeLevel.PHD, mathDepartment.getId());
        rezaei.setNationalId("0152902202");
        rezaei.setFirstName("Arash");
        rezaei.setLastName("Rezaei");
        rezaei.setPhoneNumber("09192302110");
        rezaei.setEmailAddress("rezaei@sharif.edu");
        rezaei.setPassword("5678");
        mathDepartment.addToStudentIds(rezaei.getId());
        Course complexAnalysis = new Course(mathDepartment.getId(), new TermIdentifier(2022, 2));
        complexAnalysis.setCourseName("Complex Analysis");
        complexAnalysis.addToTeachingProfessorIds(khazayi.getId());
        complexAnalysis.addToTeachingProfessorIds(fanaei.getId());
        complexAnalysis.addToStudentIds(hamidi.getId());
        complexAnalysis.addToStudentIds(rezaei.getId());
//        complexAnalysis.addToTAs(rezaei);
        complexAnalysis.setNumberOfCredits(4);
//        complexAnalysis.setActive(true);
        complexAnalysis.setDegreeLevel(DegreeLevel.UNDERGRADUATE);
        complexAnalysis.setExamDate(LocalDateTime.of(2022, 11, 21, 9, 30));
        WeekTime firstWeekTime = new WeekTime(Weekday.SUNDAY, new DayTime(14, 30, 0),
                new DayTime(16, 30, 0));
        WeekTime secondWeekTime = new WeekTime(Weekday.TUESDAY, new DayTime(14, 30, 0),
                new DayTime(16, 30, 0));
        complexAnalysis.addToWeeklyClassTimes(firstWeekTime);
        complexAnalysis.addToWeeklyClassTimes(secondWeekTime);
//        hamidi.getTranscript().put(complexAnalysis.getId(), new Score(false, 20.0));
//        hamidi.getTranscript().setGPA(20.0);
        // TODO: being careful not to add a student twice to a course in course picking stuff
        mathDepartment.addToCourseIDs(complexAnalysis.getId());
        Course realAnalysis = new Course(mathDepartment.getId(), new TermIdentifier(2022, 2));
        realAnalysis.setCourseName("Real Analysis");
        realAnalysis.addToTeachingProfessorIds(fanaei.getId());
        realAnalysis.addToStudentIds(hamidi.getId());
        realAnalysis.addToTeachingAssistantIds(rezaei.getId());
        realAnalysis.setNumberOfCredits(3);
        realAnalysis.setDegreeLevel(DegreeLevel.GRADUATE);
        realAnalysis.setExamDate(LocalDateTime.of(2022, 11, 21, 9, 30));
        realAnalysis.addToWeeklyClassTimes(firstWeekTime);
        realAnalysis.addToWeeklyClassTimes(secondWeekTime);
        mathDepartment.addToCourseIDs(realAnalysis.getId());

        Department physicsDepartment = new Department(DepartmentName.PHYSICS);
        Professor rahvar = new Professor(AcademicRole.DEPUTY, AcademicLevel.FULL, physicsDepartment.getId());
        rahvar.setNationalId("015053903");
        rahvar.setFirstName("Sohrab");
        rahvar.setLastName("Rahvar");
        rahvar.setPhoneNumber("09125730021");
        rahvar.setEmailAddress("rahvar@sharif.edu");
        rahvar.setOfficeNumber("205");
        rahvar.setPassword("1234");
        physicsDepartment.setDeputyId(rahvar.getId());
        physicsDepartment.addToProfessorIds(rahvar.getId());
        Course analyticalMechanics = new Course(physicsDepartment.getId(), new TermIdentifier(2022, 2));
        analyticalMechanics.setDegreeLevel(DegreeLevel.UNDERGRADUATE);
        analyticalMechanics.setCourseName("Analytical Mechanics");
        analyticalMechanics.addToTeachingProfessorIds(rahvar.getId());
        analyticalMechanics.addToStudentIds(hamidi.getId());
        analyticalMechanics.addToTeachingAssistantIds(rezaei.getId());
        analyticalMechanics.setNumberOfCredits(3);
        analyticalMechanics.setExamDate(LocalDateTime.of(2022, 11, 21, 9, 30));
        analyticalMechanics.addToWeeklyClassTimes(firstWeekTime);
        analyticalMechanics.addToWeeklyClassTimes(secondWeekTime);
        analyticalMechanics.addToStudentIds(rezaei.getId());
        physicsDepartment.addToCourseIDs(analyticalMechanics.getId());
//        hamidi.getTranscript().put(analyticalMechanics.getId(), new Score(false, 19.0));

        manager.save(DatasetIdentifier.STUDENTS, hamidi);
        manager.save(DatasetIdentifier.STUDENTS, rezaei);
        manager.save(DatasetIdentifier.PROFESSORS, khazayi);
        manager.save(DatasetIdentifier.PROFESSORS, fanaei);
        manager.save(DatasetIdentifier.COURSES, complexAnalysis);
        manager.save(DatasetIdentifier.COURSES, realAnalysis);
        manager.save(DatasetIdentifier.COURSES, analyticalMechanics);
        manager.save(DatasetIdentifier.DEPARTMENTS, mathDepartment);
        manager.save(DatasetIdentifier.PROFESSORS, rahvar);
        manager.save(DatasetIdentifier.DEPARTMENTS, physicsDepartment);
        manager.saveDatabase();
    }
}
