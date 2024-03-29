package server.network.clienthandling.logicutils.services;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.comparators.RequestDTOComparator;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.academicrequests.AcademicRequestStatus;
import shareables.models.pojos.academicrequests.MinorRequest;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.academicrequests.MinorRequestDTO;
import shareables.network.DTOs.academicrequests.RequestDTO;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import java.util.ArrayList;
import java.util.List;

import static server.network.clienthandling.logicutils.general.EnumStringMappingUtils.getDepartmentId;

public class MinorSubmissionUtils {
    private static RequestDTOComparator comparator = new RequestDTOComparator();

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
        minorRequestDTOs.sort(comparator);
        return minorRequestDTOs;
    }

    public static AcademicRequestStatus determineMinorRequestStatus(MinorRequest minorRequest) {
        AcademicRequestStatus academicRequestStatusAtOrigin = minorRequest.getRequestStatusAtOrigin();
        AcademicRequestStatus academicRequestStatusAtTarget = minorRequest.getRequestStatusAtTarget();
        if (academicRequestStatusAtOrigin == AcademicRequestStatus.DECLINED ||
                academicRequestStatusAtTarget == AcademicRequestStatus.DECLINED) {
            return AcademicRequestStatus.DECLINED;
        } else if (academicRequestStatusAtOrigin == AcademicRequestStatus.ACCEPTED &&
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

    public static boolean isCurrentlyMinoringAtTargetDepartment(DatabaseManager databaseManager, String requestingStudentId,
                                                                String targetDepartmentNameString) {
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, requestingStudentId);
        String targetDepartmentId = getDepartmentId(targetDepartmentNameString);
        return student.getMinorDepartmentId() != null &&
                student.getMinorDepartmentId().equals(targetDepartmentId);
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

    public static boolean studentIsMinoringSomewhere(DatabaseManager databaseManager, String requestingStudentId) {
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, requestingStudentId);
        return student.getMinorDepartmentId() != null;
    }
}
