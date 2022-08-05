package server.network.clienthandling.logicutils.enrolment;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import server.network.clienthandling.logicutils.services.MinorSubmissionUtils;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.academicrequests.AcademicRequest;
import shareables.models.pojos.academicrequests.AcademicRequestStatus;
import shareables.models.pojos.academicrequests.MinorRequest;
import shareables.models.pojos.users.professors.Professor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RequestRedirectingUtils {
    /**
     * normal pertains to DROPPING_OUT. normal in the sense that they are:
     * 1) saved in the database
     * 2) abide by the design in the AcademicRequest class
     */
    private static List<AcademicRequest> getNormalAcademicRequests(DatabaseManager databaseManager) {
        List<AcademicRequest> normalAcademicRequests = new ArrayList<>();
        normalAcademicRequests.addAll(getAcademicRequestsWithDatasetIdentifier(databaseManager,
                DatasetIdentifier.DEFENSE_REQUESTS));
        normalAcademicRequests.addAll(getAcademicRequestsWithDatasetIdentifier(databaseManager,
                DatasetIdentifier.DROPPING_OUT_REQUESTS));
        return normalAcademicRequests;
    }

    private static List<AcademicRequest> getAcademicRequestsWithDatasetIdentifier(DatabaseManager databaseManager,
                                                                                  DatasetIdentifier datasetIdentifier) {
        return databaseManager.getIdentifiables(datasetIdentifier).stream()
                .map(e -> (AcademicRequest) e)
                .filter(academicRequest -> academicRequest.getRequestStatus() == AcademicRequestStatus.SUBMITTED)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static List<MinorRequest> getMinorRequests(DatabaseManager databaseManager) {
        return databaseManager.getIdentifiables(DatasetIdentifier.MINOR_REQUESTS)
                .stream()
                .map(e -> (MinorRequest) e)
                .filter(minorRequest -> {
                    return MinorSubmissionUtils.determineMinorRequestStatus(minorRequest) ==
                            AcademicRequestStatus.SUBMITTED;
                }).collect(Collectors.toCollection(ArrayList::new));
    }

    public static void redirectAcademicRequestsToNewDeputy(DatabaseManager databaseManager, String previousDeputyId,
                                                           String newDeputyId) {
        Professor possibleDean = IdentifiableFetchingUtils.getProfessor(databaseManager, previousDeputyId);
        if (possibleDean != null) {
            possibleDean.setTemporaryDeputy(false);
        }

        // for requests that are of DROPPING_OUT type:
        redirectNormalAcademicRequestsToNewDeputy(databaseManager, previousDeputyId, newDeputyId);
    }

    private static void redirectNormalAcademicRequestsToNewDeputy(DatabaseManager databaseManager, String previousDeputyId,
                                                                  String newDeputyId) {
        List<AcademicRequest> normalAcademicRequests = getNormalAcademicRequests(databaseManager);
        normalAcademicRequests.stream()
                .filter(academicRequest -> academicRequest.getReceivingProfessorId().equals(previousDeputyId))
                .forEach(academicRequest -> academicRequest.setReceivingProfessorId(newDeputyId));
    }

    public static void redirectAcademicRequestsToDeanIfNecessary(DatabaseManager databaseManager, String professorToRemoveId,
                                                                 String departmentId) {
        if (ProfessorEditingUtils.isProfessorDepartmentDeputy(databaseManager, professorToRemoveId, departmentId)) {
            Department department = IdentifiableFetchingUtils.getDepartment(databaseManager, departmentId);
            // deputy responsibilities temporarily delegated to dean:
            Professor dean = IdentifiableFetchingUtils.getProfessor(databaseManager, department.getDeanId());
            dean.setTemporaryDeputy(true);
            department.setDeputyId(dean.getId());
            redirectAcademicRequestsToNewDeputy(databaseManager, professorToRemoveId, dean.getId());
        }
    }
}
