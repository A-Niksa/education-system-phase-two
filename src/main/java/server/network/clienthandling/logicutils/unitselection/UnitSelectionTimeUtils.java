package server.network.clienthandling.logicutils.unitselection;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.abstractions.UnitSelectionSession;
import shareables.models.pojos.users.students.Student;

import java.time.LocalDateTime;

public class UnitSelectionTimeUtils {
    public static UnitSelectionSession getStudentUnitSelection(DatabaseManager databaseManager, Student student) {
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, student.getDepartmentId());
        return department.getUnitSelectionSessions().parallelStream()
                .filter(session -> isCurrentTimeInTimeRange(session.getStartsAt(), session.getEndsAt()))
                .filter(session -> session.getIntendedDegreeLevel() == student.getDegreeLevel())
                .filter(session -> session.getIntendedYearOfEntry() == student.getYearOfEntry())
                .findAny().orElse(null);
    }
    
    private static boolean isCurrentTimeInTimeRange(LocalDateTime rangeStartsAt, LocalDateTime rangeEndsAt) {
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.compareTo(rangeStartsAt) >= 0 &&
                currentTime.compareTo(rangeEndsAt) < 0;
    }
}