package client.locallogic.menus.addition;

import client.locallogic.general.EnumStringMapper;
import shareables.models.pojos.abstractions.TermIdentifier;
import shareables.models.pojos.users.professors.AcademicLevel;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.models.pojos.users.students.StudentStatus;
import shareables.network.blueprints.Blueprint;
import shareables.utils.timing.timekeeping.WeekTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class BlueprintGenerator {
    public static Blueprint generateCourseBlueprint(String courseName, String termIdentifierString,
                                                    String[] teachingProfessorNamesArray, int numberOfCredits, String degreeLevelString,
                                                    String firstClassWeekdayString, int firstClassStartHour,
                                                    int firstClassStartMinute, int firstClassEndHour, int firstClassEndMinute,
                                                    String secondClassWeekdayString, int secondClassStartHour,
                                                    int secondClassStartMinute, int secondClassEndHour, int secondClassEndMinute,
                                                    Date selectedExamDate, int examHour, int examMinute, String departmentId) {
        LocalDateTime examLocalDateTime = CourseBlueprintHelper.convertToLocalDateTime(selectedExamDate, examHour, examMinute);
        DegreeLevel degreeLevel = EnumStringMapper.getDegreeLevel(degreeLevelString);
        TermIdentifier termIdentifier = CourseBlueprintHelper.getTermIdentifier(termIdentifierString);
        ArrayList<WeekTime> weeklyClassTimes = CourseBlueprintHelper.getWeeklyClassTimes(firstClassWeekdayString, firstClassStartHour,
                firstClassStartMinute, firstClassEndHour, firstClassEndMinute, secondClassWeekdayString,
                secondClassStartHour, secondClassStartMinute, secondClassEndHour, secondClassEndMinute);

        Blueprint blueprint = new Blueprint();
        blueprint.put("courseName", courseName);
        blueprint.put("teachingProfessorNames", teachingProfessorNamesArray);
        blueprint.put("numberOfCredits", numberOfCredits);
        blueprint.put("degreeLevel", degreeLevel);
        blueprint.put("weeklyClassTimes", weeklyClassTimes);
        blueprint.put("examDate", examLocalDateTime);
        blueprint.put("departmentId", departmentId);
        blueprint.put("termIdentifier", termIdentifier);
        return blueprint;
    }

    public static Blueprint generateProfessorBlueprint(String password, String nationalId, String firstName, String lastName,
                                                       String phoneNumber, String emailAddress, String officeNumber,
                                                       String[] adviseeStudentIds, String academicLevelString,
                                                       String departmentId) {
        AcademicLevel academicLevel = ProfessorBlueprintHelper.getAcademicLevelWithString(academicLevelString);

        Blueprint blueprint = new Blueprint();
        blueprint.put("password", password);
        blueprint.put("nationalId", nationalId);
        blueprint.put("firstName", firstName);
        blueprint.put("lastName", lastName);
        blueprint.put("phoneNumber", phoneNumber);
        blueprint.put("emailAddress", emailAddress);
        blueprint.put("officeNumber", officeNumber);
        blueprint.put("adviseeStudentIds", adviseeStudentIds);
        blueprint.put("academicLevel", academicLevel);
        blueprint.put("departmentId", departmentId);
        return blueprint;
    }

    public static Blueprint generateStudentBlueprint(String password, String nationalId, String firstName, String lastName,
                                                     String phoneNumber, String emailAddress, int yearOfEntry,
                                                     String studentStatusString, String degreeLevelString,
                                                     String advisingProfessorName, String departmentId) {
        StudentStatus studentStatus = StudentBlueprintHelper.getStudentStatus(studentStatusString);
        DegreeLevel degreeLevel = EnumStringMapper.getDegreeLevel(degreeLevelString);

        Blueprint blueprint = new Blueprint();
        blueprint.put("password", password);
        blueprint.put("nationalId", nationalId);
        blueprint.put("firstName", firstName);
        blueprint.put("lastName", lastName);
        blueprint.put("phoneNumber", phoneNumber);
        blueprint.put("emailAddress", emailAddress);
        blueprint.put("yearOfEntry", yearOfEntry);
        blueprint.put("studentStatus", studentStatus);
        blueprint.put("degreeLevel", degreeLevel);
        blueprint.put("advisingProfessorName", advisingProfessorName);
        blueprint.put("departmentId", departmentId);
        return blueprint;
    }
}
