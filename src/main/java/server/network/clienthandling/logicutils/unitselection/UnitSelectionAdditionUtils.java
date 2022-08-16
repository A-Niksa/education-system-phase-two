package server.network.clienthandling.logicutils.unitselection;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.unitselection.UnitSelectionSession;
import shareables.models.pojos.users.students.DegreeLevel;

import java.time.LocalDateTime;

public class UnitSelectionAdditionUtils {
    public static void addUnitSelectionSession(DatabaseManager databaseManager, LocalDateTime startsAt,
                                               LocalDateTime endsAt, int yearOfEntry, DegreeLevel degreeLevel,
                                               String departmentId) {
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
        UnitSelectionSession unitSelectionSession = getUnitSelectionSession(department, yearOfEntry, degreeLevel);

        if (unitSelectionSession == null) {
            UnitSelectionSession newUnitSelectionSession = new UnitSelectionSession();
            newUnitSelectionSession.setIntendedYearOfEntry(yearOfEntry);
            newUnitSelectionSession.setIntendedDegreeLevel(degreeLevel);
            newUnitSelectionSession.setStartsAt(startsAt);
            newUnitSelectionSession.setEndsAt(endsAt);
            department.addToUnitSelectionSessions(newUnitSelectionSession);
        } else {
            unitSelectionSession.resetSession();
            unitSelectionSession.setStartsAt(startsAt);
            unitSelectionSession.setEndsAt(endsAt);
        }
    }

    private static UnitSelectionSession getUnitSelectionSession(Department department, int yearOfEntry,
                                                                DegreeLevel degreeLevel) {
        return department.getUnitSelectionSessions().parallelStream()
                .filter(session -> session.getIntendedYearOfEntry() == yearOfEntry)
                .filter(session -> session.getIntendedDegreeLevel() == degreeLevel)
                .findAny().orElse(null);
    }
}
