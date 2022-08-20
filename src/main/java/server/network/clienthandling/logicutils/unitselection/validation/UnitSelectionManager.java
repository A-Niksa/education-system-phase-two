package server.network.clienthandling.logicutils.unitselection.validation;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.unitselection.UnitSelectionSession;
import shareables.utils.logging.MasterLogger;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class UnitSelectionManager {
    private ValidationTool validationTool;
    private List<UnitSelectionSession> unitSelectionSessions;
    private Deque<UnitSelectionSession> sessionsQueue;

    public UnitSelectionManager() {
        validationTool = new ValidationTool();
        unitSelectionSessions = new ArrayList<>();
        sessionsQueue = new ArrayDeque<>();
    }

    public void validateAnyUnitSelectionSessionsIfNecessary(DatabaseManager databaseManager) {
        updateQueue(databaseManager);

        UnitSelectionSession earliestSession = sessionsQueue.peek();
        if (earliestSession != null) {
            if (!earliestSession.isValidated() && hasBeenFinished(earliestSession)) {
                sessionsQueue.poll();

                validationTool.validate(databaseManager, earliestSession);
                earliestSession.setValidated(true);
                addStudentsToAcquiredCourses(databaseManager, validationTool, earliestSession);
                sendEnrolmentNotificationsToStudents(databaseManager, validationTool, earliestSession);
                removeSessionFromDepartment(databaseManager, earliestSession);
                // TODO: adding to CW

                MasterLogger.serverInfo("Unit selection session validated (ID: " + earliestSession.getId() + ")",
                        "validateAnyUnitSelectionSessionsIfNecessary", getClass());
            }
        }
    }

    private boolean hasBeenFinished(UnitSelectionSession earliestSession) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime sessionEndsAt = earliestSession.getEndsAt();
        return currentTime.compareTo(sessionEndsAt) > 0;
    }

    private void addStudentsToAcquiredCourses(DatabaseManager databaseManager, ValidationTool validationTool,
                                              UnitSelectionSession unitSelectionSession) {
        unitSelectionSession.getStudentSelectionLogs().parallelStream()
                .forEach(selectionLog -> {
                    validationTool.addStudentToAcquiredCourses(databaseManager, selectionLog);
                });
    }

    private void sendEnrolmentNotificationsToStudents(DatabaseManager databaseManager, ValidationTool validationTool,
                                                      UnitSelectionSession unitSelectionSession) {
        unitSelectionSession.getStudentSelectionLogs().parallelStream()
                .forEach(selectionLog -> {
                    validationTool.sendEnrolmentNotification(databaseManager, selectionLog);
                });
    }

    private void updateQueue(DatabaseManager databaseManager) {
        updateUnitSelectionSessions(databaseManager);
        updateSessionsQueue();
    }

    private void updateUnitSelectionSessions(DatabaseManager databaseManager) {
        List<Identifiable> departments = databaseManager.getIdentifiables(DatasetIdentifier.DEPARTMENTS);
        unitSelectionSessions = departments.stream()
                .map(identifiable -> (Department) identifiable)
                .flatMap(department -> department.getUnitSelectionSessions().stream())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void updateSessionsQueue() {
        sessionsQueue.clear();
        unitSelectionSessions.stream()
                .filter(session -> !session.isValidated())
                .forEach(session -> sessionsQueue.offer(session));
    }

    private void removeSessionFromDepartment(DatabaseManager databaseManager, UnitSelectionSession earliestSession) {
        Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, earliestSession.getDepartmentId());
        department.removeFromUnitSelectionSession(earliestSession.getId());
    }
}
