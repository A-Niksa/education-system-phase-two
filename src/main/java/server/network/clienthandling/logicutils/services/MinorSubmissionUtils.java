package server.network.clienthandling.logicutils.services;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.ClientHandler;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.abstractions.DepartmentName;
import shareables.models.pojos.academicrequests.AcademicRequestStatus;
import shareables.models.pojos.academicrequests.MinorRequest;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.MinorRequestDTO;
import shareables.network.DTOs.RequestDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import java.util.ArrayList;
import java.util.List;

public class MinorSubmissionUtils {
    public static List<RequestDTO> getStudentMinorRequestDTOs(DatabaseManager databaseManager, String studentId) {
        List<Identifiable> minorRequests = databaseManager.getIdentifiables(DatasetIdentifier.MINOR_REQUESTS);
        List<RequestDTO> minorRequestDTOs = new ArrayList<>();
        minorRequests.stream()
                .map(e -> (MinorRequest) e)
                .filter(e -> e.getRequestingStudentId().equals(studentId))
                .forEach(e -> {
                    MinorRequestDTO minorRequestDTO = new MinorRequestDTO();
                    minorRequestDTO.setId(e.getId());
                    minorRequestDTO.setOriginDepartmentId(e.getOriginDepartmentId());
                    minorRequestDTO.setTargetDepartmentId(e.getTargetDepartmentId());
                    minorRequestDTO.setAcademicRequestStatus(determineMinorRequestStatus(e));
                    minorRequestDTOs.add(minorRequestDTO);
                });
        return minorRequestDTOs;
    }

    private static AcademicRequestStatus determineMinorRequestStatus(MinorRequest minorRequest) {
        AcademicRequestStatus academicRequestStatusAtOrigin = minorRequest.getRequestStatusAtOrigin();
        AcademicRequestStatus academicRequestStatusAtTarget = minorRequest.getRequestStatusAtTarget();
        if (academicRequestStatusAtOrigin == AcademicRequestStatus.DECLINED ||
                academicRequestStatusAtTarget == AcademicRequestStatus.DECLINED) {
            return AcademicRequestStatus.DECLINED;
        } else if (academicRequestStatusAtOrigin == AcademicRequestStatus.ACCEPTED ||
            academicRequestStatusAtTarget == AcademicRequestStatus.ACCEPTED) {
            return AcademicRequestStatus.ACCEPTED;
        } else {
            return AcademicRequestStatus.SUBMITTED;
        }
    }

    public static boolean studentHasSufficientlyHighGPAForMinor(DatabaseManager databaseManager, String requestingStudentId) {
        double minimumMinorGPA = ConfigManager.getDouble(ConfigFileIdentifier.CONSTANTS, "minimumMinorGPA");
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, requestingStudentId);
        return student.fetchGPA() >= minimumMinorGPA;
    }

    public static boolean targetDepartmentIsSameAsCurrentDepartment(String originDepartmentId, String targetDepartmentNameString) {
        String targetDepartmentId = getDepartmentId(targetDepartmentNameString);
        return originDepartmentId.equals(targetDepartmentId);
    }

    private static String getDepartmentId(String departmentNameString) {
        String departmentId;
        // TODO: making this more elegant?
        switch (departmentNameString) {
            case "Mathematics":
                departmentId = "1";
                break;
            case "Physics":
                departmentId = "2";
                break;
            case "Economics":
                departmentId = "3";
                break;
            case "Chemistry":
                departmentId = "4";
                break;
            case "Aerospace Engineering":
                departmentId = "5";
                break;
            default:
                departmentId = null; // added for explicitness
        }
        return departmentId;
    }

    public static boolean isCurrentlyMinoringAtTargetDepartment(DatabaseManager databaseManager, String requestingStudentId,
                                                                String targetDepartmentNameString) {
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, requestingStudentId);
        String targetDepartmentId = getDepartmentId(targetDepartmentNameString);
        return student.getMinorDepartmentId().equals(targetDepartmentId);
    }

    public static void submitMinorRequest(DatabaseManager databaseManager, String requestingStudentId, String originDepartmentId,
                                          String targetDepartmentNameString) {
        String targetDepartmentId = getDepartmentId(targetDepartmentNameString);
        MinorRequest minorRequest = new MinorRequest();
        minorRequest.setRequestingStudentId(requestingStudentId);
        minorRequest.setOriginDepartmentId(originDepartmentId);
        minorRequest.setTargetDepartmentId(targetDepartmentId);
        databaseManager.save(DatasetIdentifier.MINOR_REQUESTS, minorRequest);
    }
}
