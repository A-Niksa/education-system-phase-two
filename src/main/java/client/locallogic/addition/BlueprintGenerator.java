package client.locallogic.addition;

import client.locallogic.general.EnumStringMapper;
import shareables.models.pojos.abstractions.TermIdentifier;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.network.blueprints.Blueprint;
import shareables.utils.timing.timekeeping.WeekTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class BlueprintGenerator {
    public static Blueprint generateBlueprint(String courseName, String termIdentifierString,
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
}
