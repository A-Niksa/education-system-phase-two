package server.database.testdata;

import server.database.management.DatabaseManager;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.abstractions.DepartmentName;
import shareables.models.pojos.users.professors.AcademicLevel;
import shareables.models.pojos.users.professors.AcademicRole;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.models.pojos.users.students.Student;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import java.util.List;

public abstract class DepartmentBuilder {
    protected ConfigFileIdentifier configIdentifier;
    protected boolean isGeneralCentersDepartment;
    protected Department department;
    protected Professor dean;
    protected Professor deputy;
    protected Professor normalProfessor;
    protected List<Student> students;
    protected List<Course> courses;

    protected DepartmentBuilder(ConfigFileIdentifier configIdentifier, boolean isGeneralCentersDepartment) {
        this.configIdentifier = configIdentifier;
        this.isGeneralCentersDepartment = isGeneralCentersDepartment;
    }

    public void buildDepartment(DatabaseManager databaseManager) {
        buildDepartmentBackbone();
        buildProfessors();
        buildStudents();

        if (isGeneralCentersDepartment) buildCourses(2);
        else buildCourses(3);

        saveDepartment(databaseManager);
    }

    private void buildCourses(int numberOfCourses) {
        for (int i = 0; i < numberOfCourses; i++) {
            String courseNumeral = EnumExtractionUtils.getIdentifiableNumeral(i) + "Course";

        }
    }

    private void buildDepartmentBackbone() {
        DepartmentName departmentName = EnumExtractionUtils.getDepartmentName(ConfigManager.getString(configIdentifier,
                "name"));
        department = new Department(departmentName);
    }

    private void buildProfessors() {
        buildDean();
        buildDeputy();
        buildNormalProfessor();
    }

    private void buildDean() {
        AcademicLevel academicLevel = EnumExtractionUtils.getAcademicLevel(ConfigManager.getString(configIdentifier,
                "deanAcademicLevel"));
        dean = new Professor(AcademicRole.DEAN, academicLevel, department.getId());
        dean.setFirstName(ConfigManager.getString(configIdentifier, "deanFirstName"));
        dean.setLastName(ConfigManager.getString(configIdentifier, "deanLastName"));
        dean.setNationalId(ConfigManager.getString(configIdentifier, "deanNationalId"));
        dean.setPhoneNumber(ConfigManager.getString(configIdentifier, "deanPhoneNumber"));
        dean.setEmailAddress(ConfigManager.getString(configIdentifier, "deanEmailAddress"));
        dean.setOfficeNumber(ConfigManager.getString(configIdentifier, "deanOfficeNumber"));
        dean.setPassword(ConfigManager.getString(configIdentifier, "deanPassword"));
    }

    private void buildDeputy() {
        AcademicLevel academicLevel = EnumExtractionUtils.getAcademicLevel(ConfigManager.getString(configIdentifier,
                "deputyAcademicLevel"));
        deputy = new Professor(AcademicRole.DEPUTY, academicLevel, department.getId());
        deputy.setFirstName(ConfigManager.getString(configIdentifier, "deputyFirstName"));
        deputy.setLastName(ConfigManager.getString(configIdentifier, "deputyLastName"));
        deputy.setNationalId(ConfigManager.getString(configIdentifier, "deputyNationalId"));
        deputy.setPhoneNumber(ConfigManager.getString(configIdentifier, "deputyPhoneNumber"));
        deputy.setEmailAddress(ConfigManager.getString(configIdentifier, "deputyEmailAddress"));
        deputy.setOfficeNumber(ConfigManager.getString(configIdentifier, "deputyOfficeNumber"));
        deputy.setPassword(ConfigManager.getString(configIdentifier, "deputyPassword"));
    }

    private void buildNormalProfessor() {
        AcademicLevel academicLevel = EnumExtractionUtils.getAcademicLevel(ConfigManager.getString(configIdentifier,
                "professorAcademicLevel"));
        normalProfessor = new Professor(AcademicRole.NORMAL, academicLevel, department.getId());
        normalProfessor.setFirstName(ConfigManager.getString(configIdentifier, "professorFirstName"));
        normalProfessor.setLastName(ConfigManager.getString(configIdentifier, "professorLastName"));
        normalProfessor.setNationalId(ConfigManager.getString(configIdentifier, "professorNationalId"));
        normalProfessor.setPhoneNumber(ConfigManager.getString(configIdentifier, "professorPhoneNumber"));
        normalProfessor.setEmailAddress(ConfigManager.getString(configIdentifier, "professorEmailAddress"));
        normalProfessor.setOfficeNumber(ConfigManager.getString(configIdentifier, "professorOfficeNumber"));
        normalProfessor.setPassword(ConfigManager.getString(configIdentifier, "professorPassword"));
    }

    private void buildStudents() {
        for (int i = 0; i < 3; i++) {
            String studentNumeral = EnumExtractionUtils.getIdentifiableNumeral(i) + "Student";

            int yearOfEntry = ConfigManager.getInt(configIdentifier, studentNumeral + "YearOfEntry");
            DegreeLevel degreeLevel = EnumExtractionUtils.getDegreeLevel(ConfigManager.getString(configIdentifier,
                    studentNumeral + "DegreeLevel"));

            Student student = new Student(yearOfEntry, degreeLevel, department.getId());
            student.setFirstName(ConfigManager.getString(configIdentifier, studentNumeral + "FirstName"));
            student.setLastName(ConfigManager.getString(configIdentifier, studentNumeral + "LastName"));
            student.setNationalId(ConfigManager.getString(configIdentifier, studentNumeral + "NationalId"));
            student.setPhoneNumber(ConfigManager.getString(configIdentifier, studentNumeral + "PhoneNumber"));
            student.setEmailAddress(ConfigManager.getString(configIdentifier, studentNumeral + "EmailAddress"));
            student.setPassword(ConfigManager.getString(configIdentifier, studentNumeral + "Password"));
            student.setPermittedToEnrol(true);

            String advisingProfessorName = ConfigManager.getString(configIdentifier, studentNumeral +
                    "AdvisingProfessor");
            Professor advisingProfessor = getProfessor(advisingProfessorName);

            student.setAdvisingProfessorId(advisingProfessor.getId());
            advisingProfessor.addToAdviseeStudentIds(student.getId());
        }
    }

    private Professor getProfessor(String professorName) {
        if (dean.fetchName().equals(professorName)) {
            return dean;
        } else if (deputy.fetchName().equals(professorName)) {
            return deputy;
        } else if (normalProfessor.fetchName().equals(professorName)) {
            return normalProfessor;
        }
        return null;
    }
}