package server.database.testdata.builders.departmentbuilders;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.database.testdata.utils.EnumExtractionUtils;
import shareables.models.pojos.abstractions.Course;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.abstractions.DepartmentName;
import shareables.models.pojos.abstractions.TermIdentifier;
import shareables.models.pojos.users.professors.AcademicLevel;
import shareables.models.pojos.users.professors.AcademicRole;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.models.pojos.users.students.Student;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.timing.timekeeping.DayTime;
import shareables.utils.timing.timekeeping.WeekTime;
import shareables.utils.timing.timekeeping.Weekday;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class DepartmentBuilder {
    protected ConfigFileIdentifier configIdentifier;
    protected DepartmentName departmentName;
    protected Department department;
    protected Professor dean;
    protected Professor deputy;
    protected Professor normalProfessor;
    protected List<Student> students;
    protected List<Course> courses;

    protected DepartmentBuilder(ConfigFileIdentifier configIdentifier, DepartmentName departmentName) {
        this.configIdentifier = configIdentifier;
        this.departmentName = departmentName;
    }

    public void buildDepartment(DatabaseManager databaseManager) {
        buildDepartmentBackbone();
        buildProfessors();
        buildStudents();

        if (departmentName == DepartmentName.GENERAL_CENTERS) buildCourses(2);
        else if (departmentName == DepartmentName.MATHEMATICS) buildCourses(4);
        else buildCourses(3);

        if (departmentName == DepartmentName.PHYSICS) addStudentsToCourses();

        // TODO: to be removed -> 
        if (departmentName == DepartmentName.MATHEMATICS) {
            courses.get(0).addToStudentIds(students.get(0).getId());
        }

        saveDepartment(databaseManager);
    }

    private void addStudentsToCourses() {
        courses.get(0)
                .addToStudentIds(
                        students.get(0).getId()
                );
        courses.get(1)
                .addToStudentIds(
                        students.get(1).getId()
                );
    }

    private void buildCourses(int numberOfCourses) {
        courses = new ArrayList<>();

        for (int i = 0; i < numberOfCourses; i++) {
            String courseNumeral = EnumExtractionUtils.getIdentifiableNumeral(i) + "Course";

            int groupNumber = ConfigManager.getInt(configIdentifier, courseNumeral + "GroupNumber");
            TermIdentifier termIdentifier = getTermIdentifier(courseNumeral);

            Course course = new Course(department.getId(), termIdentifier, groupNumber);
            course.setCourseName(ConfigManager.getString(configIdentifier, courseNumeral + "Name"));
            course.setNumberOfCredits(ConfigManager.getInt(configIdentifier, courseNumeral + "NumberOfCredits"));
            course.setCourseCapacity(ConfigManager.getInt(configIdentifier, courseNumeral + "Capacity"));

            DegreeLevel degreeLevel = EnumExtractionUtils.getDegreeLevel(ConfigManager.getString(configIdentifier,
                    courseNumeral + "DegreeLevel"));
            course.setDegreeLevel(degreeLevel);

            List<Professor> teachingProfessors = getProfessors(ConfigManager.getString(configIdentifier,
                    courseNumeral + "TeachingProfessors"));
            teachingProfessors.forEach(e -> course.addToTeachingProfessorIds(e.getId()));

            List<Student> teachingAssistants = getStudents(ConfigManager.getString(configIdentifier, courseNumeral +
                    "TeachingAssistants"));
            teachingAssistants.forEach(e -> course.addToTeachingAssistantIds(e.getId()));

            List<WeekTime> courseWeekTimes = getCourseWeekTimes(courseNumeral);
            courseWeekTimes.forEach(course::addToWeeklyClassTimes);

            LocalDateTime examDate = getExamDate(courseNumeral);
            course.setExamDate(examDate);

            courses.add(course);

            department.addToCourseIDs(course.getId());
        }
    }

    private LocalDateTime getExamDate(String courseNumeral) {
        int examYear = ConfigManager.getInt(configIdentifier, courseNumeral + "ExamDateYear");
        int examMonth = ConfigManager.getInt(configIdentifier, courseNumeral + "ExamDateMonth");
        int examDay = ConfigManager.getInt(configIdentifier, courseNumeral + "ExamDateDay");
        int examHour = ConfigManager.getInt(configIdentifier, courseNumeral + "ExamDateHour");
        int examMinute = ConfigManager.getInt(configIdentifier, courseNumeral + "ExamDateMinute");

        return LocalDateTime.of(examYear, examMonth, examDay, examHour, examMinute);
    }

    private List<WeekTime> getCourseWeekTimes(String courseNumeral) {
        WeekTime firstWeekTime = getWeekTime(courseNumeral, "First");
        WeekTime secondWeekTime = getWeekTime(courseNumeral, "Second");
        return new ArrayList<>(List.of(firstWeekTime, secondWeekTime));
    }

    private WeekTime getWeekTime(String courseNumeral, String weekTimeNumeral) {
        Weekday weekday = EnumExtractionUtils.getWeekday(ConfigManager.getString(configIdentifier,
                courseNumeral + weekTimeNumeral + "Weekday"));
        String dayTimeNumeral = weekTimeNumeral + "DayTime";
        int startingHour = ConfigManager.getInt(configIdentifier, courseNumeral + dayTimeNumeral + "StartHour");
        int startingMinute = ConfigManager.getInt(configIdentifier, courseNumeral + dayTimeNumeral + "StartMinute");
        int startingSecond = ConfigManager.getInt(configIdentifier, courseNumeral + dayTimeNumeral + "StartSecond");
        int endingHour = ConfigManager.getInt(configIdentifier, courseNumeral + dayTimeNumeral + "EndHour");
        int endingMinute = ConfigManager.getInt(configIdentifier, courseNumeral + dayTimeNumeral + "EndMinute");
        int endingSecond = ConfigManager.getInt(configIdentifier, courseNumeral + dayTimeNumeral + "EndSecond");

        return new WeekTime(weekday, new DayTime(startingHour, startingMinute, startingSecond),
                new DayTime(endingHour, endingMinute, endingSecond));
    }

    private List<Student> getStudents(String studentNames) {
        String[] partitionedStudentNames = studentNames.split(", ");
        List<Student> students = new ArrayList<>();
        for (String studentName : partitionedStudentNames) {
            Student student = getStudent(studentName);
            students.add(student);
        }
        return students;
    }

    private Student getStudent(String studentName) {
        return students.stream()
                .filter(student -> student.fetchName().equals(studentName))
                .findAny().orElse(null);
    }

    private List<Professor> getProfessors(String professorNames) {
        String[] partitionedProfessorNames = professorNames.split(", ");
        List<Professor> professors = new ArrayList<>();
        for (String professorName : partitionedProfessorNames) {
            Professor professor = getProfessor(professorName);
            professors.add(professor);
        }
        return professors;
    }

    private TermIdentifier getTermIdentifier(String courseNumeral) {
        int termYear = ConfigManager.getInt(configIdentifier, courseNumeral + "TermYear");
        int termHalf = ConfigManager.getInt(configIdentifier, courseNumeral + "TermHalf");
        return new TermIdentifier(termYear, termHalf);
    }

    private void buildDepartmentBackbone() {
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

        department.setDeanId(dean.getId());
        department.addToProfessorIds(dean.getId());
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

        department.setDeputyId(deputy.getId());
        department.addToProfessorIds(deputy.getId());
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

        department.addToProfessorIds(normalProfessor.getId());
    }

    private void buildStudents() {
        students = new ArrayList<>();

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

            students.add(student);

            department.addToStudentIds(student.getId());
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

    private void saveDepartment(DatabaseManager databaseManager) {
        databaseManager.save(DatasetIdentifier.DEPARTMENTS, department);

        databaseManager.save(DatasetIdentifier.PROFESSORS, deputy);
        databaseManager.save(DatasetIdentifier.PROFESSORS, dean);
        databaseManager.save(DatasetIdentifier.PROFESSORS, normalProfessor);

        students.forEach(e -> databaseManager.save(DatasetIdentifier.STUDENTS, e));

        courses.forEach(e -> databaseManager.save(DatasetIdentifier.COURSES, e));
    }
}